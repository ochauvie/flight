package com.flightbook.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.VolsAdapter;
import com.flightbook.listener.VolsAdapterListener;
import com.flightbook.model.Vol;
import com.flightbook.sqllite.DbVol;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;

public class VolsActivity extends ListActivity implements DialogReturn, VolsAdapterListener, View.OnClickListener {

	private MyDialogInterface myInterface;
	private DbVol dbVol = new DbVol(this);
	private ArrayList<Vol> vols;
	private int selectItim = -1;
	private VolsAdapter adapter;
	private TextView totalVol;
	

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



        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbVol.open();
        	vols = dbVol.getVols();
        dbVol.close();
        
        totalVol.setText(getTotalVol());
        	
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
    }
}
