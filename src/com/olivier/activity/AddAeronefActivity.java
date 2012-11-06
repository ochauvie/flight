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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class AddAeronefActivity extends Activity {

	private DbAeronef dbAeronef = new DbAeronef(this);
	
	private Aeronef aeronef = null;

	EditText name, wingSpan, weight, engine, firstFlight, comment;
	TextView log;
	RadioButton optPlaneur, optAvion, optParamoteur, optHelico, optAuto, optDivers;
	ImageButton save, close;
	
	RadioGroup rg1, rg2 ,rg3;
	    
	
		private OnCheckedChangeListener listener1 = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            if (checkedId != -1) {
	                rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
	                rg2.clearCheck(); // clear the second RadioGroup!
	                rg2.setOnCheckedChangeListener(listener2); //reset the listener
	                rg3.setOnCheckedChangeListener(null);
	                rg3.clearCheck(); 
	                rg3.setOnCheckedChangeListener(listener3);
	            }
	        }
	    };

	    private OnCheckedChangeListener listener2 = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            if (checkedId != -1) {
	                rg1.setOnCheckedChangeListener(null);
	                rg1.clearCheck();
	                rg1.setOnCheckedChangeListener(listener1);
	                rg3.setOnCheckedChangeListener(null);
	                rg3.clearCheck();
	                rg3.setOnCheckedChangeListener(listener3);
	
	            }
	        }
	    };
	    
	    private OnCheckedChangeListener listener3 = new OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	            if (checkedId != -1) {
	                rg1.setOnCheckedChangeListener(null);
	                rg1.clearCheck();
	                rg1.setOnCheckedChangeListener(listener1);
	                rg2.setOnCheckedChangeListener(null);
	                rg2.clearCheck();
	                rg2.setOnCheckedChangeListener(listener2);
	
	            }
	        }
	    };
	
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_aeronef);
	        
	        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
	    	rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
	    	rg3 = (RadioGroup) findViewById(R.id.radioGroup3);
	        rg1.setOnCheckedChangeListener(listener1);
	        rg2.setOnCheckedChangeListener(listener2);
	        rg3.setOnCheckedChangeListener(listener3);
	        
	        log = (TextView) findViewById(R.id.textViewLog);
	        log.setText("");
	        log.setTextColor(Color.RED);
	        
	        name = (EditText) findViewById(R.id.editTextAeronef);
	        wingSpan = (EditText) findViewById(R.id.editTextWingSpan);
	        weight = (EditText) findViewById(R.id.editTextWeight);
	        engine = (EditText) findViewById(R.id.editTextEngine);
	        firstFlight = (EditText) findViewById(R.id.editTextFirstFlight);
	        comment = (EditText) findViewById(R.id.editTextComment);
	        
	        optPlaneur = (RadioButton) findViewById(R.id.option_planeur);
	        optAvion = (RadioButton) findViewById(R.id.option_avion);
	        optParamoteur = (RadioButton) findViewById(R.id.option_paramoteur);
	        optHelico = (RadioButton) findViewById(R.id.option_helico);
	        optAuto = (RadioButton) findViewById(R.id.option_auto);
	        optDivers = (RadioButton) findViewById(R.id.option_divers);
	    
	        initView();
	        
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
	        			
	        			try {
	        				
	        				if (aeronef==null) {
	        					aeronef = new Aeronef();	
	    	        		} 
		        			
		        			aeronef.setName(edName.toString());
		        			aeronef.setWingSpan(Float.valueOf(wingSpan.getText().toString()));
		        			aeronef.setWeight(Float.valueOf(weight.getText().toString()));
		        			aeronef.setEngine(engine.getText().toString());
		        			aeronef.setFirstFlight(firstFlight.getText().toString());
		        			aeronef.setComment(comment.getText().toString());
		        			
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
		        			
		        			if (aeronef.getId()!=0) {
		        				updateAeronef();
		        			} else {
		        				saveAeronef();
		        			}
		        			
		        			log.setText("Machine sauvegarder");
		        			
		        			Intent hangarActivity = new Intent(getApplicationContext(),HangarActivity.class);
			            	startActivity(hangarActivity);
			            	finish();
	        			} catch (NumberFormatException nfe) {
	        				log.setText("Format de nombre non valide !");
	        			}
	        			
	        		}
	        	}
	        });
	        
	        
	    }
	    
	    private void initView() {
	    	Bundle bundle = getIntent().getExtras();
	        if (bundle!=null) {
	        	int aeronefId = bundle.getInt("aeronefId");
	        	if (aeronefId!=0) {
	        		dbAeronef.open();
	        		aeronef = dbAeronef.getAeronefById(aeronefId);
	        		dbAeronef.close();
	        		if (aeronef!=null) {
	        			name.setText(aeronef.getName());
	        	        wingSpan.setText(String.valueOf(aeronef.getWingSpan()));
	        	        weight.setText(String.valueOf(aeronef.getWeight()));
	        	        engine.setText(aeronef.getEngine());
	        	        firstFlight.setText(aeronef.getFirstFlight());
	        	        comment .setText(aeronef.getComment());
	        	        
	        	        if (Aeronef.T_PLANEUR.equals(aeronef.getType())) {
	        	        	optPlaneur.setChecked(true);
	        	        } else if (Aeronef.T_AVION.equals(aeronef.getType())) {
	        	        	optAvion.setChecked(true);
	        	        } else if (Aeronef.T_PARAMOTEUR.equals(aeronef.getType())) {
	        	        	optParamoteur.setChecked(true);
	        	        } else if (Aeronef.T_HELICO.equals(aeronef.getType())) {
	        	        	optHelico.setChecked(true);
	        	        } else if (Aeronef.T_AUTO.equals(aeronef.getType())) {
	        	        	optAuto.setChecked(true);
	        	        } else if (Aeronef.T_DIVERS.equals(aeronef.getType())) {
	        	        	optDivers.setChecked(true);
	        			}
	        		}
	        	}
	        }
	    }

	    RadioButton.OnClickListener myOptionOnClickListener =
	    	new RadioButton.OnClickListener() {
	    		  @Override
	    		  public void onClick(View v) {
	    		   // TODO Auto-generated method stub
	    		  }
	    };
	    
	    
	    private void saveAeronef() {
	    	dbAeronef.open();
			dbAeronef.insertAeronef(aeronef);
			dbAeronef.close();
	    }
	    
	    private void updateAeronef() {
	    	dbAeronef.open();
			dbAeronef.updateAeronef(aeronef);
			dbAeronef.close();
	    }
	    
}
