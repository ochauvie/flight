package com.olivier.activity;




import java.util.ArrayList;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.AeronefsAdapter;
import com.olivier.listener.AeronefAdapterListener;
import com.olivier.model.Aeronef;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class HangarActivity extends ListActivity  implements DialogReturn, AeronefAdapterListener {

	private ImageButton addAeronef;
	private ImageButton close;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Aeronef> aeronefs;
	
	AeronefsAdapter adapter;
	private MyDialogInterface myInterface;
	int selectItim = -1;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangar);
    
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbAeronef.open();
        aeronefs = dbAeronef.getAeronefs();
        dbAeronef.close();
        
        // Création et initialisation de l'Adapter pour les personnes
        adapter = new AeronefsAdapter(this, aeronefs);
            
        // Ecoute des évènements sur votre liste
        adapter.addListener(this);
        
        // Récupération du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
        
        // Open view add aeronef
        addAeronef = (ImageButton) findViewById(R.id.addAeronef);
        addAeronef.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), AddAeronefActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
        	}
        });
        
        // Close view aeronef selection
        close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
            	startActivity(volActivity);
            	finish();
        	}
        });
        
    }
    
    
    @Override
	public void onClickName(Aeronef item, int position) {
    	Aeronef sel = aeronefs.get(position);
    	Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
    	volActivity.putExtra("aeronef", sel.getName());
    	volActivity.putExtra("type", sel.getType());
    	startActivity(volActivity);
    	finish();
	}
    
	@Override
	public void onClickNameToDelete(Aeronef item, int position) {
		Aeronef sel = aeronefs.get(position);
		selectItim = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(sel.getName() + "\n" + "(" + sel.getType() + ")");
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
			dbAeronef.open();
	        dbAeronef.deleteAeronef(aeronefs.get(selectItim));
	        dbAeronef.close();
	        
	        aeronefs.remove(selectItim);
	        adapter.notifyDataSetChanged();
			/*
	        Intent hangarActivity = new Intent(getApplicationContext(),HangarActivity.class);
	    	startActivity(hangarActivity);
	    	finish();
	    	*/
		}
	}

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }

}
