package com.olivier.activity;

import com.olivier.R;
import com.olivier.model.Aeronef;
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

public class AddAeronefActivity extends Activity {

	private DbAeronef dbAeronef = new DbAeronef(this);

	EditText name;
	TextView log;
	RadioButton optPlaneur, optAvion, optParamoteur, optHelico, optAuto, optDivers;
	ImageButton save, close;
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_aeronef);
	        
	        log = (TextView) findViewById(R.id.textViewLog);
	        log.setText("");
	        log.setTextColor(Color.RED);
	        
	        name = (EditText) findViewById(R.id.editTextAeronef);
	        
	        optPlaneur = (RadioButton) findViewById(R.id.option_planeur);
	        optAvion = (RadioButton) findViewById(R.id.option_avion);
	        optParamoteur = (RadioButton) findViewById(R.id.option_paramoteur);
	        optHelico = (RadioButton) findViewById(R.id.option_helico);
	        optAuto = (RadioButton) findViewById(R.id.option_auto);
	        optDivers = (RadioButton) findViewById(R.id.option_divers);
	    
	        // Close view
	        close = (ImageButton) findViewById(R.id.close);
	        close.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		Intent hangarActivity = new Intent(getApplicationContext(),HangarActivity.class);
	            	startActivity(hangarActivity);
	            	 finish();
	        	}
	        });
	        
	        // Save view
	        save = (ImageButton) findViewById(R.id.save);
	        save.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		Editable edName = name.getText();
	        		if (edName==null || "".equals(edName.toString())) {
	        			log.setText("Il faut donner un nom !");
	        		} else if (!optPlaneur.isChecked() && !optAvion.isChecked() &&
	        				!optParamoteur.isChecked() && !optHelico.isChecked() &&
	        				!optAuto.isChecked() && !optDivers.isChecked()) {
	        			log.setText("Il faut selectionner un type de machine !");
	        		} else {
	        				        			
	        			log.setText("Machine sauvegarder");
	        			
	        			Aeronef aeronef = new Aeronef();
	        			aeronef.setName(edName.toString());
	        			if (optPlaneur.isChecked()) {
	        				aeronef.setType(Aeronef.T_PLANEUR);
	        			} else if (optAvion.isChecked()) {
	        				aeronef.setType(Aeronef.T_AVION);
	        			} else if (optParamoteur.isChecked()) {
	        				aeronef.setType(Aeronef.T_PARAMOTEUR);
	        			} else if (optHelico.isChecked()) {
	        				aeronef.setType(Aeronef.T_HELICO);
	        			} else if (optAuto.isChecked()) {
	        				aeronef.setType(Aeronef.T_AUTO);
	        			} else if (optDivers.isChecked()) {
	        				aeronef.setType(Aeronef.T_DIVERS);
	        			}
	        			
	        			dbAeronef.open();
	        			dbAeronef.insertAeronef(aeronef);
	        			dbAeronef.close();
	        			
	        			Intent hangarActivity = new Intent(getApplicationContext(),HangarActivity.class);
		            	startActivity(hangarActivity);
		            	finish();
	        		}
	        	}
	        });
	        
	        
	    }

	    RadioButton.OnClickListener myOptionOnClickListener =
	    	new RadioButton.OnClickListener() {
	    		  @Override
	    		  public void onClick(View v) {
	    		   // TODO Auto-generated method stub
	    		  }
	    };
	    
}
