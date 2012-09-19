package com.olivier.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.olivier.R;
import com.olivier.model.Vol;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class VolsActivity extends ListActivity  {

	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Vol> vols;
	ArrayList<String> listVols = new ArrayList<String>();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vols);
        
        dbAeronef.open();
        vols = dbAeronef.getVols();
        dbAeronef.close();
        
        if (vols!=null) {
        	
        	for (int i=0; i<vols.size(); i++) {
        		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.FRANCE);
        		Vol flight = vols.get(i);
				String sDate = sdf.format(flight.getDateVol());
				String sFlight = sDate + " - " + flight.getAeronef() 
						+ " : " + flight.getMinutesVol() + " min dont "
						+ " " + flight.getMinutesMoteur() + ":" + flight.getSecondsMoteur() + " moteur \n";
				listVols.add(sFlight);
			}
        
        	//Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, listVols);
        
        	//On attribue à notre listView l'adapter que l'on vient de créer
        	setListAdapter(adapter);
        	adapter.setNotifyOnChange(true);
        
        }
        
    }

    
    @SuppressWarnings("rawtypes")
	@Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.ic_launcher); // TODO 
    	builder.setTitle("Confirmation");
    	builder.setInverseBackgroundForced(true);
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		  // TODO
    	    dialog.dismiss();
    	  }
    	});
    	builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
   		    dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();

    	
	    	Vol flight = vols.get(position);
	    	dbAeronef.open();
	        dbAeronef.deleteVol(flight);
	        dbAeronef.close();
	        listVols.remove(position);
	    	((ArrayAdapter) l.getAdapter()).notifyDataSetChanged();
    	
       
    
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }

    
    
    
    
}
