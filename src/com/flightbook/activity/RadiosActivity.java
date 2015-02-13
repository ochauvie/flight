package com.flightbook.activity;

import java.util.ArrayList;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.RadiosAdapter;
import com.flightbook.listener.RadiosAdapterListener;
import com.flightbook.model.Radio;
import com.flightbook.sqllite.DbRadio;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dbRadio.open();
        	radios = dbRadio.getRadios();
        dbRadio.close();

        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        // Creation et initialisation de l'Adapter pour les aeronefs
        adapter = new RadiosAdapter(this, radios);
        adapter.addListener(this);

        //Initialisation de la liste avec les donnees
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

        // Hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
			dbRadio.open();
				dbRadio.deleteRadio(radios.get(selectItim));
			dbRadio.close();
	        radios.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}

    @Override
    public void onBackPressed() {
        // Nothings
    }
    
}
