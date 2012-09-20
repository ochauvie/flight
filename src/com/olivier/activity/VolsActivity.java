package com.olivier.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.model.Vol;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VolsActivity extends ListActivity  implements DialogReturn {

	MyDialogInterface myInterface;
	MyDialogInterface.DialogReturn dialogReturn;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Vol> vols;
	ArrayList<String> listVols = new ArrayList<String>();
	int selectItim = -1;
	ListView listView;
	
	
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

    
    
	@Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	listView = l;
    	selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(l.getContext());
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete); 
    	builder.setTitle(R.string.deletel);
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

    
	@SuppressWarnings("rawtypes")
	@Override
	public void onDialogCompleted(boolean answer) {
		if (answer && selectItim!=-1) {
			Vol flight = vols.get(selectItim);
	    	dbAeronef.open();
	        dbAeronef.deleteVol(flight);
	        dbAeronef.close();
	        listVols.remove(selectItim);
	    	((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
		}
		
	}

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }

    
}
