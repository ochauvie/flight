package com.olivier.activity;

import java.util.ArrayList;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.RadiosAdapter;
import com.olivier.listener.RadiosAdapterListener;
import com.olivier.model.Radio;
import com.olivier.sqllite.DbRadio;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class RadiosActivity extends ListActivity implements DialogReturn, RadiosAdapterListener  {

	private ImageButton add;
	private EditText newRadioName;
	private DbRadio dbRadio = new DbRadio(this);
	private ArrayList<Radio> radios;
	private RadiosAdapter adapter;
	private MyDialogInterface myInterface;
	private int selectItim = -1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radios);
        
        dbRadio.open();
        	radios = dbRadio.getRadios();
        dbRadio.close();
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        // Création et initialisation de l'Adapter pour les aeronefs
        adapter = new RadiosAdapter(this, radios);
            
        // Ecoute des évènements sur votre liste
        adapter.addListener(this);
        
        // Récupération du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
        newRadioName =  (EditText)  findViewById(R.id.editTextNewRadio);
        
        // Open view add 
        add = (ImageButton) findViewById(R.id.butNew);
        add.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String newName = newRadioName.getText().toString();
        		if (newName!=null && !"".equals(newName)) {
        			Radio r = new Radio();
        			r.setName(newName);
        			dbRadio.open();
        				dbRadio.addRadio(r);
        			dbRadio.close();
        			
        			/* On ne connait pas le nouveau id
        			radios.add(r);
        			adapter.notifyDataSetChanged();
        			*/
        			Intent radiosActivity = new Intent(getApplicationContext(), RadiosActivity.class);
                	startActivity(radiosActivity);
                	finish();
        		}
        	}
        });
        
    }


	@Override
	public void onClickName(Radio item, int position) {
		Radio radio = radios.get(position);
    	Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
    	radioActivity.putExtra(Radio.RADIO_ID, radio.getId());
    	startActivity(radioActivity);
	}

	@Override
	public void onClickToDelete(Radio item, int position) {
		Radio sel = radios.get(position);
		selectItim = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(sel.getName() + "\n" );
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
			dbRadio.open();
				dbRadio.deleteRadio(radios.get(selectItim));
			dbRadio.close();
	        radios.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}

    
}
