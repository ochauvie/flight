package com.carnetvol.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.carnetvol.R;
import com.carnetvol.model.Accu;
import com.carnetvol.model.TypeAccu;
import com.carnetvol.sqllite.DbAccu;

import java.util.ArrayList;

@TargetApi(14)
public class AddAccuActivity extends Activity {

	private DbAccu dbAccu = new DbAccu(this);
	private Accu accu = null;

	private EditText nom;
	private TextView log;
	private ImageButton save, close;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_accu);

	        log = (TextView) findViewById(R.id.textViewLog);
	        log.setText("");
	        log.setTextColor(Color.RED);
	        
	        nom = (EditText) findViewById(R.id.editTextName);


	        // Get accu in parameter
	        initView();
	        
	        // Close view
	        close = (ImageButton) findViewById(R.id.close);
	        close.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		Intent accusActivity = new Intent(getApplicationContext(),AccusActivity.class);
                    // TODO passer les extra
	            	startActivity(accusActivity);
	            	 finish();
	        	}
	        });
	       
	        // Save view
	        save = (ImageButton) findViewById(R.id.save);
	        save.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		
	        		Editable edName = nom.getText();
	        		if (edName==null || "".equals(edName.toString())) {
	        			log.setText(R.string.name_mandatory);
	        		} else {
	        			try {
	        				if (accu==null) {
	        					accu = new Accu();
	    	        		} 
		        			accu.setNom(edName.toString());
                            accu.setType(TypeAccu.LIPO);    // TODO

		        			dbAccu.open();
                                if (accu.getId()!=0) {
                                    dbAccu.updateAccu(accu);
                                } else {
                                    dbAccu.insertAccu(accu);
                                }
                            dbAccu.close();
		        			
		        			log.setText(R.string.accu_save);
		        			
		        			Intent accusActivity = new Intent(getApplicationContext(),AccusActivity.class);
			            	startActivity(accusActivity);
                            // TODO  passer les extra
			            	finish();
	        			} catch (NumberFormatException nfe) {
	        				log.setText(R.string.number_format_ko);
	        			}
	        		}
	        	}
	        });

	        
	    }
	    
	    private void initView() {
	    	Bundle bundle = getIntent().getExtras();
	        if (bundle!=null) {
	            accu = (Accu)bundle.getSerializable(Accu.class.getName());
                if (accu!=null) {
                    nom.setText(accu.getNom());
                }

	        }
	    }

	    RadioButton.OnClickListener myOptionOnClickListener =
	    	new RadioButton.OnClickListener() {
	    		  @Override
	    		  public void onClick(View v) {
	    		  }
	    };
	    

		@Override
	    public void onBackPressed() {
			// Nothings
		}
}
