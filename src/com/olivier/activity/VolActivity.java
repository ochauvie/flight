package com.olivier.activity;

import java.util.Date;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.model.Aeronef;
import com.olivier.model.Vol;
import com.olivier.speech.TtsProviderFactory;
import com.olivier.sqllite.DbAeronef;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class VolActivity extends Activity implements DialogReturn, OnTouchListener {
	
	private MyDialogInterface myInterface;
	private DbAeronef dbAeronef = new DbAeronef(this);
	private RelativeLayout relativeLayout;
	private ImageButton saveButton, deleteButton;
	private EditText aeronef, minVol, minMot, secMot, note, lieu;
	private Double latitude, longitude, altitude;
	private String lieuGps;
	private ImageButton selectAeronef, butGps;
	private float downXValue;
	private TextView editText1;
	private String typeAeronef;
	
	private TtsProviderFactory ttsProviderImpl; 
	
	private AlphaAnimation alphaAnimation;
	
	private LocationManager locationMgr = null;
	private LocationListener onLocationChange = new LocationListener()
		{
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
	
			@Override
			public void onProviderEnabled(String provider) {
			}
	
			@Override
			public void onProviderDisabled(String provider) {
			}
	
			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				altitude = location.getAltitude();
				lieuGps = "Lat: " + latitude + "\n" +
						  "Long: " + longitude + "\n" +
						  "Alt: " + altitude;
			}
		};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol);
        
        ttsProviderImpl = TtsProviderFactory.getInstance();
        
               
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        alphaAnimation = new AlphaAnimation(0.0f , 1.0f ) ;
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(1200);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutVol);
        relativeLayout.startAnimation(alphaAnimation);
        
        aeronef = (EditText)  findViewById(R.id.editTextAeronef);
        minVol = (EditText)  findViewById(R.id.editTextMinVol);
        minVol.requestFocus();
        minMot = (EditText)  findViewById(R.id.editTextMinMot);
        secMot = (EditText)  findViewById(R.id.editTextSecMot);
        note = (EditText)  findViewById(R.id.editTextNote);
        lieu = (EditText)  findViewById(R.id.editTextLieu);
        editText1 = (TextView)  findViewById(R.id.editText1);
        editText1.setTextColor(Color.RED);
        
        // Gps
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100, onLocationChange);
		locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, onLocationChange);
        
		// Add these two lines
        relativeLayout.setOnTouchListener((OnTouchListener) this); 
        
        // Init aeronef selection by HangarActivity or SplashActivity (Nfc tag)
        initPage();
        
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
	        		vol.setDateVol(new Date()); // TODO ; ajouter un champ pour pouvoir saisir une date ant�rieure
	        		vol.setNote(note.getText().toString());
	        		vol.setLieu(lieu.getText().toString());
	        		
	        		dbAeronef.open();
	        			dbAeronef.insertVol(vol);
	        		dbAeronef.close();
	        		
	        		resetPage();
	        		
	        		String sSecondsMOteur = String.valueOf(vol.getSecondsMoteur());
	        		if (vol.getSecondsMoteur()<10) {sSecondsMOteur="0"+sSecondsMOteur;}
	        		
	        		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	            	builder.setCancelable(true);
	            	builder.setIcon(R.drawable.recorder); 
	            	builder.setTitle("Enregistrement effectu�");
	            	ttsProviderImpl.say("enregistrement effectu� pour " + vol.getAeronef());
	            	String result = "Machine: \t" + vol.getAeronef() + "\n" 
							+ "Vol: \t\t\t\t" + vol.getMinutesVol() + " min \n"
							+ "Moteur: \t\t" + vol.getMinutesMoteur() + ":" + sSecondsMOteur + "\n"
							+ "Note: \t\t\t" +  vol.getNote() + "\n" 
							+ vol.getLieu();
	        		
	            	builder.setMessage(result);
	            	builder.setInverseBackgroundForced(true);
	            	builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
	            	  @Override
	            	  public void onClick(DialogInterface dialog, int which) {
	            		dialog.dismiss();
	            	  }
	            	});
	            	
	            	AlertDialog alert = builder.create();
	            	alert.show();
	        		
        		} else {
        			editText1.setText("Il faut choisir une machine");
        			ttsProviderImpl.say("Il faut choisir une machine");
        		}
        	}
        });        

        // Reset screen
        deleteButton = (ImageButton) findViewById(R.id.deleteVol);
        deleteButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		resetPage();
        	}
        });        
        
        // Find aeronef
        selectAeronef = (ImageButton) findViewById(R.id.selectAeronef);
        selectAeronef.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), HangarActivity.class);
                startActivity(myIntent);
                finish();
        	}
        });

        
        // Position
        butGps = (ImageButton) findViewById(R.id.gps);
        butGps.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		lieu.setText(lieuGps);
        	}
        }); 
    }
    
    /**
     * Init the view data
     */
    private void initPage() {
    	aeronef.setText(null);
        minVol.setText(null);
        minMot.setText(null);
        secMot.setText(null);
        
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String sAeronef = bundle.getString(Aeronef.NAME);
        	typeAeronef = bundle.getString(Aeronef.TYPE);
        	if (sAeronef!=null) {
        		aeronef.setText(sAeronef);
        		ttsProviderImpl.say(sAeronef);
        	}
        }
    }
    
    /**
     * Clean the view data
     */
    private void resetPage() {
    	minVol.setText(null);
        minMot.setText(null);
        secMot.setText(null);
        note.setText(null);
        lieu.setText(null);
        editText1.setText("");
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    	locationMgr.removeUpdates(onLocationChange);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    		ActionBar actionBar = getActionBar();
    		actionBar.setDisplayHomeAsUpEnabled(true);
    	}
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
          case R.id.meteo:
              ttsProviderImpl.say("m�t�o");
        	  Intent myIntent = new Intent(VolActivity.this, MeteoActivity.class);
             startActivityForResult(myIntent, 0);
             return true;
          case R.id.radio:
        	  ttsProviderImpl.say("programme radio");
        	  Intent radiosActivity = new Intent(VolActivity.this, RadiosActivity.class);
          		startActivity(radiosActivity);
              return true;
          case R.id.carte:
        	  ttsProviderImpl.say("carte");
        	  if (latitude!=null && longitude!=null) {
        		  Intent carteActivity = new Intent(VolActivity.this, CarteActivity.class);
            	  carteActivity.putExtra("latitude", latitude*1000000);
          		  carteActivity.putExtra("longitude", longitude*1000000);
          		  startActivity(carteActivity);  
        	  }
              return true;
         case R.id.vols:
        	 ttsProviderImpl.say("liste des enregistrements");
        	 Intent volsActivity = new Intent(VolActivity.this, VolsActivity.class);
             startActivityForResult(volsActivity, 0);
             return true;
         case R.id.checklists:
        	 ttsProviderImpl.say("checklist");
        	 Intent checklistsActivity = new Intent(VolActivity.this, ChecklistsActivity.class);
             startActivityForResult(checklistsActivity, 0);
             return true;
       }
       return false;}
    
	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		 // Get the action that was done on this touch event
        switch (arg1.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                // Store the X value when the user's finger was pressed down
                downXValue = arg1.getX();
                break;
            }

            case MotionEvent.ACTION_UP: {
                // Get the X value when the user released his/her finger
                float currentX = arg1.getX();            

                // Going backwards: pushing stuff to the right
                if (downXValue < currentX) {
                	Intent myIntent = new Intent(v.getContext(), VolsActivity.class);
                    startActivity(myIntent);
                }

                // Going forwards: pushing stuff to the left
                if (downXValue > currentX) {
                	Intent myIntent = new Intent(v.getContext(), MeteoActivity.class);
                    startActivity(myIntent);
                }
                break;
            }
        }

        // If you return false, these actions will not be recorded
        return true;
	}
	
	@Override
    public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.fermeture);
    	builder.setTitle(R.string.close);
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
		if (answer) {
			finish();
		}
	}
	
}
