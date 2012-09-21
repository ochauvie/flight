package com.olivier.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.VolsAdapter;
import com.olivier.listener.VolsAdapterListener;
import com.olivier.model.Vol;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.Menu;

public class VolsActivity extends ListActivity implements DialogReturn, VolsAdapterListener  {

	MyDialogInterface myInterface;
	MyDialogInterface.DialogReturn dialogReturn;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Vol> vols;
	int selectItim = -1;
	VolsAdapter adapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vols);
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbAeronef.open();
        vols = dbAeronef.getVols();
        dbAeronef.close();
        
        if (vols!=null) {
        	
        	// Création et initialisation de l'Adapter pour les personnes
            adapter = new VolsAdapter(this, vols);
                
            // Ecoute des évènements sur votre liste
            adapter.addListener(this);
            
            // Récupération du composant ListView
            //ListView list = (ListView)findViewById(R.id.ListViewHangar);
                
            //Initialisation de la liste avec les données
            setListAdapter(adapter);
        }
    }

	@Override
	public void onClickName(Vol vol, int position) {
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
	    	dbAeronef.open();
	        dbAeronef.deleteVol(flight);
	        dbAeronef.close();
	        vols.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }

}
