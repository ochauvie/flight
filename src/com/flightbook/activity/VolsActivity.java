package com.flightbook.activity;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.VolsAdapter;
import com.flightbook.listener.VolsAdapterListener;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbBackup;
import com.flightbook.sqllite.DbImport;
import com.flightbook.sqllite.DbVol;
import com.flightbook.tools.Chart;
import com.flightbook.tools.SimpleFileDialog;
import com.flightbook.widget.FlightWidgetProvider;

import android.app.ActionBar;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class VolsActivity extends ListActivity implements DialogReturn, VolsAdapterListener, View.OnClickListener {//}, ActionBar.OnNavigationListener{


    private MyDialogInterface myInterface;
	private DbVol dbVol = new DbVol(this);
	private ArrayList<Vol> vols;
	private int selectItim = -1;
	private VolsAdapter adapter;
	private TextView totalVol, nbVol, nbDate;
    private TtsProviderFactory ttsProviderImpl;

    private static final int FILE_SELECT_CODE = 0;


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
        nbDate = (TextView) footer.findViewById(R.id.nbDate);

        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        // Recuperation de la liste des vols
        dbVol.open();
        	vols = dbVol.getVols();
        dbVol.close();

        // Mise à jour du footer
        majFooter();
        	
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

    private void majFooter() {
        totalVol.setText(getTotalVol());
        nbVol.setText(getNbVol());
        nbDate.setText(getNbDate() + " " + getString(R.string.day));
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

    /**
     * Total nombre de date
     * @return
     */
    private String getNbDate() {
        int total = 0;
        Date cDate = null;
        if (vols!=null) {
            for (Vol vol:vols) {
                if (cDate==null || vol.getDateVol().compareTo(cDate)>0) {
                    total++;
                    cDate = vol.getDateVol();
                }
            }
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
        majFooter();
    }

    @Override
    // Filtre la liste avec la date selectionne
    public void onClickDate(Vol vol, int position) {
        selectItim = position;
        if (vols!=null) {
            for (int i=vols.size()-1; i>=0; i--) {
                vols.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        dbVol.open();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
        vols.addAll(dbVol.getVolsByDate(sdf.format(vol.getDateVol())));
        dbVol.close();
        adapter.notifyDataSetChanged();
        majFooter();
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
    		myInterface.getListener().onDialogCompleted(true, null);
    	    dialog.dismiss();
    	  }
    	});
    	builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(false, null);
    		dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	@Override
	public void onDialogCompleted(boolean answer, String type) {
		if (answer && selectItim!=-1) {
			Vol flight = vols.get(selectItim);
			dbVol.open();
				dbVol.deleteVol(flight);
			dbVol.close();
	        vols.remove(selectItim);
	        adapter.notifyDataSetChanged();
            majFooter();
            updateMyWidgets();
		}
	}


    @Override
    // Action sur le click du header: suppression du filtre sur la liste
    public void onClick(View v) {
        reloadAllFlight();
    }

    private void reloadAllFlight() {
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
        majFooter();
    }

    /**
     * Make pie chart for char type
     * @param chartType
     */
    private void viewPieChart(String chartType) {
        String title = getString(R.string.title_activity_chart_nb);
        if (Chart.CHART_TIME.equals(chartType)) {
            title = getString(R.string.title_activity_chart_time);
        }
        Chart chart = new Chart(getBaseContext(), vols, chartType, title);
        ttsProviderImpl.say(title);
        startActivity(chart.getIntentPieChart());
    }


    /**
     * Make bar chart by machine
     * @param chartType
     */
    private void viewChartByMachine(String chartType) {
        String title = getString(R.string.title_activity_chart_nb);
        if (Chart.CHART_TIME.equals(chartType)) {
            title = getString(R.string.title_activity_chart_time);
        }
        Chart chart = new Chart(getBaseContext(), vols, chartType, title);
        ttsProviderImpl.say(title);
        startActivity(chart.getIntentChartByMachine());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vols, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }


    /**
     * Call when menu item is selected
     */
        public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pieChartByNb:
                viewPieChart(Chart.CHART_NB);
                return true;
            case R.id.barChartByNb:
                viewChartByMachine(Chart.CHART_NB);
                return true;
            case R.id.pieChartByTotal:
                viewPieChart(Chart.CHART_TIME);
                return true;
            case R.id.barChartByTotal:
                viewChartByMachine(Chart.CHART_TIME);
                return true;
            case R.id.send:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                DbBackup dbBackup = new DbBackup(this);
                sendIntent.putExtra(Intent.EXTRA_TEXT, dbBackup.getStbVols(vols).toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.importVols:
                importVols();
                return true;
        }
        return false;}


    /**
     * Update widget
     */
    private void updateMyWidgets() {
        Intent intent = new Intent(this, FlightWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), FlightWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }


    /**
     * Import flights from file
     */
    private void importVols() {
        SimpleFileDialog FileOpenDialog = new SimpleFileDialog(this, "FileOpen",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
                        File file = new File(chosenDir);
                        // Initiate the upload
                        DbImport dbImport = new DbImport(VolsActivity.this);
                        String result = dbImport.importVols(file);
                        if (result!=null) {
                            Toast.makeText(VolsActivity.this, result, Toast.LENGTH_LONG).show();
                        } else {
                            // Update the list
                            reloadAllFlight();
                            // Update widget
                            updateMyWidgets();
                            Toast.makeText(VolsActivity.this, getString(R.string.menu_import_ok), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                FileOpenDialog.Default_File_Name = "";
                FileOpenDialog.chooseFile_or_Dir();
    }


    @Override
    public void onBackPressed() {
        // Nothings
    }

}
