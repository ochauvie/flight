package com.flightbook.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.VolsAdapter;
import com.flightbook.listener.VolsAdapterListener;
import com.flightbook.model.Vol;
import com.flightbook.model.Aeronef;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbVol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class VolsActivity extends ListActivity implements DialogReturn, VolsAdapterListener, View.OnClickListener {

    private static final String CHART_TIME = "TIME";
    private static final String CHART_NB = "NB";

    private MyDialogInterface myInterface;
	private DbVol dbVol = new DbVol(this);
	private ArrayList<Vol> vols;
	private int selectItim = -1;
	private VolsAdapter adapter;
	private TextView totalVol, nbVol;
    private ImageButton butChartTime,butChartNb ;
    private TtsProviderFactory ttsProviderImpl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vols);

        ttsProviderImpl = TtsProviderFactory.getInstance();
        
        View header = getLayoutInflater().inflate(R.layout.activity_header_vols, null);
            // Ajout d'un listener sur la selection du header pour supprimer le filtre sur la liste)
            header.setOnClickListener(this);
        View footer = getLayoutInflater().inflate(R.layout.activity_footer_vols, null);
        ListView listView = getListView();
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        totalVol = (TextView) footer.findViewById(R.id.totalVol);
        nbVol = (TextView) footer.findViewById(R.id.nbVol);

        butChartTime = (ImageButton) findViewById(R.id.viewChartTime);
        butChartTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewChart(VolsActivity.CHART_TIME);
            }
        });

        butChartNb = (ImageButton) findViewById(R.id.viewChartNb);
        butChartNb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewChart(VolsActivity.CHART_NB);
            }
        });


        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        // recuperation de la liste des vols
        dbVol.open();
        	vols = dbVol.getVols();
        dbVol.close();

        // Mise à jour du footer
        totalVol.setText(getTotalVol());
        nbVol.setText(getNbVol());
        	
    	// Creation et initialisation de l'Adapter pour les vols
        adapter = new VolsAdapter(this, vols);
            
        // Ecoute des evevnements sur votre liste
        adapter.addListener(this);

        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);

        // Animation
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
        				this, R.anim.list_layout_controller);
        listView.setLayoutAnimation(controller);
    }

    /**
     * Total heures de vol
     * @return
     */
    private String getTotalVol() {
    	int total = 0;
    	if (vols!=null) {
	    	for (Vol vol:vols) {
	        	total = total + vol.getMinutesVol();
	        }
    	}
   		int heu = total/60;
   		int min = total % 60;
   		String sMin = String.valueOf(min);
   		if (min<10) {
   			sMin = "0" + sMin;
   		}
   		return String.valueOf(heu + "h" + sMin);
    }

    /**
     * Total nombre de vol
     * @return
     */
    private String getNbVol() {
        int total = 0;
        if (vols!=null) {
            total = vols.size();
        }
        return String.valueOf(total);
    }

	@Override
	public void onClickVol(Vol vol, int position) {
		selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.note);
    	builder.setTitle(vol.getAeronef());
    	
    	builder.setMessage("     " + getString(R.string.note) +" : \n\n" 
    						+ vol.getNote() + "\n\n" 
    						+ "     " + getString(R.string.lieu) + ": \n\n" 
    						+ vol.getLieu());
    	builder.setInverseBackgroundForced(true);
    	builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
	}


    @Override
    // Filtre la liste avec l'aeronef selectionne
    public void onClickName(Vol vol, int position) {
        selectItim = position;
        if (vols!=null) {
            for (int i=vols.size()-1; i>=0; i--) {
                vols.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        dbVol.open();
        vols.addAll(dbVol.getVolsByMachine(vol.getAeronef()));
        dbVol.close();
        adapter.notifyDataSetChanged();
        totalVol.setText(getTotalVol());
        nbVol.setText(getNbVol());
    }


    @Override
	public void onClickDelete(Vol vol, int position) {
		selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.FRANCE);
		  String sDate = sdf.format(vol.getDateVol());
    	builder.setTitle(sDate + " "
    			+ vol.getAeronef() + "\n" 
    			+ vol.getMinutesVol() + " min"
    			+ " (" + vol.getMinutesMoteur() + ":" + vol.getSecondsMoteur() + ")");
    	builder.setInverseBackgroundForced(true);
    	builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(true);
    	    dialog.dismiss();
    	  }
    	});
    	builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(false);
    		dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	@Override
	public void onDialogCompleted(boolean answer) {
		if (answer && selectItim!=-1) {
			Vol flight = vols.get(selectItim);
			dbVol.open();
				dbVol.deleteVol(flight);
			dbVol.close();
	        vols.remove(selectItim);
	        adapter.notifyDataSetChanged();
	        totalVol.setText(getTotalVol());
            nbVol.setText(getNbVol());
		}
	}


    @Override
    // Action sur le click du header: suppression du filtre sur la liste
    public void onClick(View v) {
        if (vols!=null) {
            for (int i=vols.size()-1; i>=0; i--) {
                vols.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        dbVol.open();
            vols.addAll(dbVol.getVols());
        dbVol.close();
        adapter.notifyDataSetChanged();
        totalVol.setText(getTotalVol());
        nbVol.setText(getNbVol());
    }

    // See more at: http://www.achartengine.org/index.html
    private void viewChart(String chartType) {

        CategorySeries distributionSeries = new CategorySeries(" Vols ");
        double dataPlaneur = 0;
        double dataAvion = 0;
        double dataParamoteur = 0;
        double dataHelico = 0;
        double dataAuto = 0;
        double dataDivers = 0;
        double dataAutre = 0;

        if (vols!=null) {
            for (Vol vol : vols) {
                String type = vol.getType();
              if (Aeronef.T_PLANEUR.equals(type)) {
                  if (VolsActivity.CHART_TIME.equals(chartType)) {
                      dataPlaneur = dataPlaneur + vol.getMinutesVol();
                  } else {
                      dataPlaneur++;
                  }
              } else if (Aeronef.T_AVION.equals(type)) {
                  if (VolsActivity.CHART_TIME.equals(chartType)) {
                      dataAvion = dataAvion + vol.getMinutesVol();
                  } else {
                      dataAvion++;
                  }
              } else if (Aeronef.T_PARAMOTEUR.equals(type)) {
                  if (VolsActivity.CHART_TIME.equals(chartType)) {
                      dataParamoteur = dataParamoteur + vol.getMinutesVol();
                  } else {
                      dataParamoteur++;
                  }

              } else if (Aeronef.T_HELICO.equals(type)) {
                  if (VolsActivity.CHART_TIME.equals(chartType)) {
                      dataHelico = dataHelico + vol.getMinutesVol();
                  } else {
                      dataHelico++;
                  }
              } else if (Aeronef.T_AUTO.equals(type)) {
                  if (VolsActivity.CHART_TIME.equals(chartType)) {
                      dataAuto = dataAuto + vol.getMinutesVol();
                  } else {
                      dataAuto++;
                  }
              } else if (Aeronef.T_DIVERS.equals(type)) {
                  if (VolsActivity.CHART_TIME.equals(chartType)) {
                      dataDivers = dataDivers + vol.getMinutesVol();
                  } else {
                      dataDivers++;
                  }
              } else {
                  if (VolsActivity.CHART_TIME.equals(chartType)) {
                      dataAutre = dataAutre + vol.getMinutesVol();
                  } else {
                      dataAutre++;
                  }
              }
            }
        }

        if (dataPlaneur>0) {
            distributionSeries.add(Aeronef.T_PLANEUR, dataPlaneur);
        }
        if (dataAvion>0) {
            distributionSeries.add(Aeronef.T_AVION, dataAvion);
        }
        if (dataParamoteur>0) {
            distributionSeries.add(Aeronef.T_PARAMOTEUR, dataParamoteur);
        }
        if (dataHelico>0) {
            distributionSeries.add(Aeronef.T_HELICO, dataHelico);
        }
        if (dataAuto>0) {
            distributionSeries.add(Aeronef.T_AUTO, dataAuto);
        }
        if (dataDivers>0) {
            distributionSeries.add(Aeronef.T_DIVERS, dataDivers);
        }
        if (dataAutre>0) {
            distributionSeries.add("Inconnu", dataAutre);
        }


        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distributionSeries.getItemCount();i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(Aeronef.getColor(distributionSeries.getCategory(i)));
            seriesRenderer.setDisplayChartValues(true);

            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Temps (min)");
        defaultRenderer.setChartTitleTextSize(25);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setShowLabels(false);
        defaultRenderer.setLabelsTextSize(25);
        defaultRenderer.setLegendTextSize(25);
        defaultRenderer.setDisplayValues(true);

        String title = getString(R.string.title_activity_chart_nb);
        if (VolsActivity.CHART_TIME.equals(chartType)) {
            title = getString(R.string.title_activity_chart_time);
        }

        // Creating an intent to plot bar chart using dataset and multipleRenderer
        Intent intent = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries , defaultRenderer, title);
        ttsProviderImpl.say(title);

        // Start Activity
        startActivity(intent);

    }

}
