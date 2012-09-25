package com.olivier.activity;

import java.util.Date;

import com.olivier.R;
import com.olivier.model.Vol;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class VolActivity extends Activity {

	private DbAeronef dbAeronef = new DbAeronef(this);
	private ImageButton saveButton;
	private ImageButton deleteButton;
	private EditText aeronef;
	private EditText minVol;
	private EditText minMot;
	private EditText secMot;
	private EditText note;
	private ImageButton selectAeronef;
	private ImageButton viewVol;
	
	private TextView editText1;
	private String typeAeronef;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol);
        
        aeronef = (EditText)  findViewById(R.id.editTextAeronef);
        minVol = (EditText)  findViewById(R.id.editTextMinVol);
        minMot = (EditText)  findViewById(R.id.editTextMinMot);
        secMot = (EditText)  findViewById(R.id.editTextSecMot);
        note = (EditText)  findViewById(R.id.editTextNote);
        editText1 = (TextView)  findViewById(R.id.editText1);
        editText1.setTextColor(Color.RED);
        
        aeronef.setText(null);
        minVol.setText(null);
        minMot.setText(null);
        secMot.setText(null);
        
        // Init aeronef selection by AeronefActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String sAeronef = bundle.getString("aeronef");
        	typeAeronef = bundle.getString("type");
        	if (sAeronef!=null) {aeronef.setText(sAeronef);}
        }
        
        // Save flight
        saveButton = (ImageButton) findViewById(R.id.saveVol);
        saveButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String sAronef = aeronef.getText().toString();
        		if (!"".equals(sAronef) && sAronef!=null) {
	        		Vol vol = new Vol();
	        		vol.setAeronef(sAronef);
	        		vol.setType(typeAeronef);
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
	        		vol.setNote(note.getText().toString());
	        		
	        		dbAeronef.open();
	        		dbAeronef.insertVol(vol);
	        		String sSecondsMOteur = String.valueOf(vol.getSecondsMoteur());
	        		if (vol.getSecondsMoteur()<10) {sSecondsMOteur="0"+sSecondsMOteur;}
	        		String result = "\nVol ajouté: \n\n" + vol.getAeronef() 
							+ " : " + vol.getMinutesVol() + " min dont "
							+ " " + vol.getMinutesMoteur() + ":" + sSecondsMOteur + " moteur \n"
							+  vol.getNote();
	        		editText1.setText(result);
	        		dbAeronef.close();
        		} else {
        			editText1.setText("Il faut choisir un aéronef");
        		}
        	}
        });        

        // Reset screen
        deleteButton = (ImageButton) findViewById(R.id.deleteVol);
        deleteButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		minVol.setText(null);
                minMot.setText(null);
                secMot.setText(null);
                note.setText(null);
                editText1.setText("");
        	}
        });        
        
        // Find aeronef
        selectAeronef = (ImageButton) findViewById(R.id.selectAeronef);
        selectAeronef.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), HangarActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
        	}
        });

        // View flights
        viewVol = (ImageButton) findViewById(R.id.viewVol);
        viewVol.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), VolsActivity.class);
                startActivityForResult(myIntent, 0);
        	}
        });
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
