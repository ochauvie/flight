package com.olivier.activity;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.RadioAdapter;
import com.olivier.listener.SwitchPotarAdapterListener;
import com.olivier.model.Potar;
import com.olivier.model.Radio;
import com.olivier.model.Switch;
import com.olivier.sqllite.DbAeronef;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class RadioActivity extends ListActivity implements DialogReturn, SwitchPotarAdapterListener{

	private ListView listView;
	private ImageButton addSwitchPotar;
	private Radio radio;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private RadioAdapter adapter;	
	private MyDialogInterface myInterface;
	
	private int itemId;
	private String typeItem;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        // Get radio to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	int radioId = bundle.getInt("radioId");
        	dbAeronef.open();
            radio = dbAeronef.getRadioById(radioId);
            dbAeronef.close();
        }
        
        // View title
        setTitle(radio.getName());
        
        listView = getListView();
        
        // Création et initialisation de l'Adapter pour les aeronefs
        adapter = new RadioAdapter(this, radio);
        // Ecoute des évènements sur votre liste
        adapter.addListener(this);
        
        
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
        
        // Close view
        addSwitchPotar = (ImageButton) findViewById(R.id.addSwitchPotar);
        addSwitchPotar.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent addSwitchPotarActivity = new Intent(getApplicationContext(), AddSwitchPotarActivity.class);
        		addSwitchPotarActivity.putExtra("radioId", radio.getId());
            	startActivity(addSwitchPotarActivity);
            	finish();
        	}
        });
        
    }


	@Override
	public void onClickLayout(Object item, int position) {
		itemId = -1;
		String name = null;
		typeItem = null;
		if (item instanceof Switch) {
			itemId = ((Switch) item).getId();
			name = ((Switch) item).getName();
			typeItem = "Inter";
		} else if (item instanceof Potar) {
			itemId = ((Potar) item).getId();
			name = ((Potar) item).getName();
			typeItem = "Potar";
		}
		
		if (itemId!=-1) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setCancelable(true);
	    	builder.setIcon(R.drawable.delete);
	    	builder.setTitle(typeItem + "\n" + name);
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
	}

	@Override
	public void onDialogCompleted(boolean answer) {
		if (answer && itemId!=-1) {
			dbAeronef.open();
				if ("Inter".equals(typeItem)) {
					dbAeronef.deleteSwitch(itemId);
				} else if ("Potar".equals(typeItem)) {
					dbAeronef.deletePotar(itemId);
				}
	        dbAeronef.close();
	        
	        Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
	        radioActivity.putExtra("radioId", radio.getId());
        	startActivity(radioActivity);
        	finish();
		}
	}
    
}
