package com.olivier.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.olivier.R;
import com.olivier.model.Vol;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class VolActivity extends Activity {

	private DbAeronef dbAeronef = new DbAeronef(this);
	private Button saveButton;
	private Button deleteButton;
	private EditText aeronef;
	private EditText minVol;
	private EditText minMot;
	private EditText secMot;
	private Button selectAeronef;
	private Button viewVol;
	
	private TextView editText1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol);
        
        aeronef = (EditText)  findViewById(R.id.editTextAeronef);
        minVol = (EditText)  findViewById(R.id.editTextMinVol);
        minMot = (EditText)  findViewById(R.id.editTextMinMot);
        secMot = (EditText)  findViewById(R.id.editTextSecMot);
        editText1 = (TextView)  findViewById(R.id.editText1);
        
        aeronef.setText(null);
        minVol.setText(null);
        minMot.setText(null);
        secMot.setText(null);
        
        // Init aeronef selection by AeronefActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String sAeronef = bundle.getString("aeronef");
        	if (sAeronef!=null) {
        		aeronef.setText(sAeronef);	
        	}
        }
        
        // Save flight
        saveButton = (Button) findViewById(R.id.saveVol);
        saveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String sAronef = aeronef.getText().toString();
        		if (!"".equals(sAronef) && sAronef!=null) {
	        		Vol vol = new Vol();
	        		vol.setAeronef(sAronef);
	        		try {
	        			vol.setMinutesVol(Integer.valueOf(minVol.getText().toString()));
	        		} catch (NumberFormatException e) {
	        			vol.setMinutesVol(0);
	        		}
	        		try {
	        			vol.setMinutesMoteur(Integer.valueOf(minMot.getText().toString()));
	        		} catch (NumberFormatException e) {
	        			vol.setMinutesMoteur(0);
	        		}
	        		try {
	        			vol.setSecondsMoteur(Integer.valueOf(secMot.getText().toString()));
	        		} catch (NumberFormatException e) {
	        			vol.setSecondsMoteur(0);
	        		}
	        		vol.setDateVol(new Date());
	        		
	        		dbAeronef.open();
	        		dbAeronef.insertVol(vol);
	        		String result = "Vol ajouté: \n" + vol.getAeronef() 
							+ " : " + vol.getMinutesVol() + " min dont "
							+ " " + vol.getMinutesMoteur() + ":" + vol.getSecondsMoteur() + " moteur \n";
	        		editText1.setText(result);
	        		dbAeronef.close();
        		} else {
        			editText1.setText("Il faut choisir un aéronef");
        		}
        	}
        });        

        // Reset screen
        deleteButton = (Button) findViewById(R.id.deleteVol);
        deleteButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		minVol.setText(null);
                minMot.setText(null);
                secMot.setText(null);
        	}
        });        
        
        // Find aeronef
        selectAeronef = (Button) findViewById(R.id.selectAeronef);
        selectAeronef.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), AeronefActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
        	}
        });

        // View flights
        viewVol = (Button) findViewById(R.id.viewVol);
        viewVol.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), VolsActivity.class);
                startActivityForResult(myIntent, 0);
        		
        		//editText1.setText(afficheVols());
        	}
        });
    }

    private String afficheVols() {
    	String result = "LISTE DES VOLS \n";
    	dbAeronef.open();
    	ArrayList<Vol> vols = dbAeronef.getVols();
    	dbAeronef.close();
		if (vols!=null && vols.size()>0) {
			for (Vol flight:vols) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.FRANCE);
				String sDate = sdf.format(flight.getDateVol());
				result = result + sDate + " - " + flight.getAeronef() 
						+ " : " + flight.getMinutesVol() + " min dont "
						+ " " + flight.getMinutesMoteur() + ":" + flight.getSecondsMoteur() + " moteur \n";
			}
		} else {
			result = "Aucun vol enregistré";
		}
    	return result;
    }

    
    @Override
    public void onStop() {
    	super.onStop();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }

    
    
    
    
}
