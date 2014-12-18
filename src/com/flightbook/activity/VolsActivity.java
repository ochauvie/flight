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
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class VolsActivity extends ListActivity implements DialogReturn, VolsAdapterListener, View.OnClickListener {

	private MyDialogInterface myInterface;
	private DbVol dbVol = new DbVol(this);
	private ArrayList<Vol> vols;
	private int selectItim = -1;
	private VolsAdapter adapter;
	private TextView totalVol, nbVol;
    private ImageButton butChart;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vols);
        
        View header = getLayoutInflater().inflate(R.layout.activity_header_vols, null);
            // Ajout d'un listener sur la selection du header pour supprimer le filtre sur la liste)
            header.setOnClickListener(this);
        View footer = getLayoutInflater().inflate(R.layout.activity_footer_vols, null);
        ListView listView = getListView();
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        totalVol = (TextView) footer.findViewById(R.id.totalVol);
        nbVol = (TextView) footer.findViewById(R.id.nbVol);
        // Position GPS
        butChart = (ImageButton) findViewById(R.id.viewChart);
        butChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewChart(v);
            }
        });


        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbVol.open();
        	vols = dbVol.getVols();
        dbVol.close();
        
        totalVol.setText(getTotalVol());
        nbVol.setText(getNbVol());
        	
    	// Creation et initialisation de l'Adapter pour les vols
        adapter = new VolsAdapter(this, vols);
            
        // Ecoute des evevnements sur votre liste
        adapter.addListener(this);
        
        // Recuperation du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);

        
        // Animation
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
        				this, R.anim.list_layout_controller);
        listView.setLayoutAnimation(controller);
    }

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

    // See more at: http://www.survivingwithandroid.com
    private void viewChart(View v) {

        // Color of each Pie Chart Sections
        int[] colors = { Color.RED, Color.BLUE, Color.MAGENTA, Color.BLACK, Color.GRAY, Color.LTGRAY, Color.YELLOW};

        CategorySeries distributionSeries = new CategorySeries(" Vols ");
        double timePlaneur = 0;
        double timeAvion = 0;
        double timeParamoteur = 0;
        double timeHelico = 0;
        double timeAuto = 0;
        double timeDivers = 0;
        double timeAutre = 0;

        if (vols!=null) {
            for (Vol vol : vols) {
                String type = vol.getType();
              if (Aeronef.T_PLANEUR.equals(type)) {
                  timePlaneur = timePlaneur + vol.getMinutesVol();
              } else if (Aeronef.T_AVION.equals(type)) {
                  timeAvion = timeAvion + vol.getMinutesVol();
              } else if (Aeronef.T_PARAMOTEUR.equals(type)) {
                  timeParamoteur = timeParamoteur + vol.getMinutesVol();
              } else if (Aeronef.T_HELICO.equals(type)) {
                  timeHelico = timeHelico + vol.getMinutesVol();
              } else if (Aeronef.T_AUTO.equals(type)) {
                  timeAuto = timeAuto + vol.getMinutesVol();
              } else if (Aeronef.T_DIVERS.equals(type)) {
                  timeDivers = timeDivers + vol.getMinutesVol();
              } else {
                  timeAutre = timeAutre + vol.getMinutesVol();
              }
            }
        }

        distributionSeries.add(Aeronef.T_PLANEUR, timePlaneur);
        distributionSeries.add(Aeronef.T_AVION, timeAvion);
        distributionSeries.add(Aeronef.T_PARAMOTEUR, timeParamoteur);
        distributionSeries.add(Aeronef.T_HELICO, timeHelico);
        distributionSeries.add(Aeronef.T_AUTO, timeAuto);
        distributionSeries.add(Aeronef.T_DIVERS, timeDivers);
        distributionSeries.add("Inconnu", timeAutre);


        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distributionSeries.getItemCount();i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Temps");
        defaultRenderer.setChartTitleTextSize(25);
        defaultRenderer.setZoomButtonsVisible(true);
        //defaultRenderer.setShowLabels(false);
        defaultRenderer.setLabelsTextSize(25);
        defaultRenderer.setLegendTextSize(25);
        defaultRenderer.setDisplayValues(true);
        
        // Creating an intent to plot bar chart using dataset and multipleRenderer
        Intent intent = ChartFactory.getPieChartIntent(getBaseContext(), distributionSeries , defaultRenderer, getString(R.string.title_activity_chart));

        // Start Activity
        startActivity(intent);

    }

}
