package com.olivier.activity;


import com.olivier.R;
import com.olivier.model.Potar;
import com.olivier.model.Radio;
import com.olivier.model.Switch;
import com.olivier.sqllite.DbAeronef;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddSwitchPotarActivity extends Activity {

	private DbAeronef dbAeronef = new DbAeronef(this);
	private int radioId;

	private EditText name, action, up, center, down;
	private TextView log;
	private RadioButton optSwitch, optPotar;
	private ImageButton save, close;
			
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_switch_potar);
	        
	        log = (TextView) findViewById(R.id.textViewLog);
	        log.setText("");
	        log.setTextColor(Color.RED);
	        
	        name = (EditText) findViewById(R.id.editName);
	        action = (EditText) findViewById(R.id.editAction);
	        up = (EditText) findViewById(R.id.editUp);
	        center = (EditText) findViewById(R.id.editCenter);
	        down = (EditText) findViewById(R.id.editDown);
	        
	        optSwitch = (RadioButton) findViewById(R.id.option_switch);
	        optPotar = (RadioButton) findViewById(R.id.option_potar);
	        
	        Bundle bundle = getIntent().getExtras();
	        if (bundle!=null) {
	        	radioId = bundle.getInt(Radio.RADIO_ID);
	        }
	        
	        // Close view
	        close = (ImageButton) findViewById(R.id.close);
	        close.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
	            	radioActivity.putExtra(Radio.RADIO_ID, radioId);
	            	startActivity(radioActivity);
	            	finish();
	        	}
	        });
	       
	        // Save view
	        save = (ImageButton) findViewById(R.id.save);
	        save.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		
	        		Editable edName = name.getText();
	        		if (edName==null || "".equals(edName.toString())) {
	        			log.setText(getString(R.string.name_mandatory));
	        		} else if (!optSwitch.isChecked() && !optPotar.isChecked() ) {
	        			log.setText(getString(R.string.inter_potar_mandatory));
	        		} else {
	        			
	        			if (optSwitch.isChecked()) {
	        				Switch sw = new Switch();
	        				sw.setName(name.getText().toString());
	        				sw.setAction(action.getText().toString());
	        				sw.setUp(up.getText().toString());
	        				sw.setCenter(center.getText().toString());
	        				sw.setDown(down.getText().toString());
	        				dbAeronef.open();
	        					dbAeronef.addSwitchToRadio(radioId, sw);
	        				dbAeronef.close();
	        				
	        			} else if (optPotar.isChecked()) {
	        				Potar potar = new Potar();
	        				potar.setName(name.getText().toString());
	        				potar.setAction(action.getText().toString());
	        				potar.setUp(up.getText().toString());
	        				potar.setCenter(center.getText().toString());
	        				potar.setDown(down.getText().toString());
	        				dbAeronef.open();
	        					dbAeronef.addPotarToRadio(radioId, potar);
	        				dbAeronef.close();
	        			}
	        			
	        			log.setText(getString(R.string.save_ok));
	        			
	        			Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
		            	radioActivity.putExtra(Radio.RADIO_ID, radioId);
		            	startActivity(radioActivity);
		            	finish();
	        			
	        		}
	        	}
	        });
	        
	        
	    }
	    

	    RadioButton.OnClickListener myOptionOnClickListener =
	    	new RadioButton.OnClickListener() {
	    		  @Override
	    		  public void onClick(View v) {
	    		  }
	    };
	    
}
