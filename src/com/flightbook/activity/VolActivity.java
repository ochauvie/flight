package com.flightbook.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbVol;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class VolActivity extends Activity implements DialogReturn, OnTouchListener, OnDateSetListener {
	
	private MyDialogInterface myInterface;
	private DbVol dbVol = new DbVol(this);
	private RelativeLayout relativeLayout;
	private ImageButton saveButton, deleteButton;
	private EditText aeronef, minVol, minMot, secMot, note, lieu, flightDate;
	private Double latitude, longitude, altitude;
	private String lieuGps;
	private ImageButton selectAeronef, butGps, selectDate;
	private float downXValue;
	private TextView editText1;
	private String typeAeronef;
	private DatePickerDialog datePickerDialog = null;
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
				lieuGps = getString(R.string.lat) + ": " + latitude + "\n" +
						  getString(R.string.lon) + ": " + longitude + "\n" +
						  getString(R.string.alt) + ": " + altitude;
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
        
        flightDate = (EditText)  findViewById(R.id.flightDate);
        flightDate.setEnabled(false);
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
        if (locationMgr!=null) {
        	List<String> providers = locationMgr.getAllProviders();
        	if (providers!=null) {
        		for (String prov:providers) {
        			if (LocationManager.NETWORK_PROVIDER.equals(prov)) {
        				locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100, onLocationChange);			
        			}
        			if (LocationManager.GPS_PROVIDER.equals(prov)) {
        				locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, onLocationChange);
        			}
        		}
        	}
        }
        
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
	        		try {
	        			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
	        			vol.setDateVol(sdf.parse(flightDate.getText().toString()));
					} catch (ParseException e) {
						vol.setDateVol(new Date());
					}
	        		vol.setNote(note.getText().toString());
	        		vol.setLieu(lieu.getText().toString());
	        		
	        		dbVol.open();
	        			dbVol.insertVol(vol);
	        		dbVol.close();
	        		
	        		resetPage();
	        		
	        		String sSecondsMOteur = String.valueOf(vol.getSecondsMoteur());
	        		if (vol.getSecondsMoteur()<10) {sSecondsMOteur="0"+sSecondsMOteur;}
	        		
	        		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	            	builder.setCancelable(true);
	            	builder.setIcon(R.drawable.recorder); 
	            	builder.setTitle(getString(R.string.save_ok));
	            	String say = getString(R.string.save_ok) + " " + getString(R.string.st_for) + " " + vol.getAeronef();
	            	ttsProviderImpl.say(say);
	            	
	            	String result = getString(R.string.aeronef) + ": \t" + vol.getAeronef() + "\n" 
	            			+ getString(R.string.vol) + ": \t\t\t\t" + vol.getMinutesVol() + " " + getString(R.string.min) + "\n"
	            			+ getString(R.string.moteur) + ": \t\t" + vol.getMinutesMoteur() + ":" + sSecondsMOteur + "\n"
	            			+ getString(R.string.note) + ": \t\t\t" +  vol.getNote() + "\n" 
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
        			
        			editText1.setText(getString(R.string.aeronef_mandatory));
        			ttsProviderImpl.say(getString(R.string.aeronef_mandatory));
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
        		if (lieuGps==null || "".equals(lieuGps)) {
        			ttsProviderImpl.say(getString(R.string.position_ko));
        		}
        		lieu.setText(lieuGps);
        	}
        }); 
        
        // Flight date
        selectDate = (ImageButton) findViewById(R.id.selectDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String sDate = flightDate.getText().toString();
        		String[] ssDate = sDate.split("/");
        		datePickerDialog = new DatePickerDialog(v.getContext(),
        				VolActivity.this,
        				Integer.parseInt(ssDate[2]),
                        Integer.parseInt(ssDate[1])-1,
                        Integer.parseInt(ssDate[0]));
        		datePickerDialog.show();
        	}
        });
    }
    
    /**
     * Init the view data
     */
    private void initPage() {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
		String sDate = sdf.format(new Date());
    	flightDate.setText(sDate);
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
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
		String sDate = sdf.format(new Date());
    	flightDate.setText(sDate);
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
              ttsProviderImpl.say(getString(R.string.meteo));
        	  Intent myIntent = new Intent(VolActivity.this, MeteoActivity.class);
             startActivityForResult(myIntent, 0);
             return true;
          case R.id.radio:
        	  ttsProviderImpl.say(getString(R.string.radio));
        	  Intent radiosActivity = new Intent(VolActivity.this, RadiosActivity.class);
          		startActivity(radiosActivity);
              return true;
          case R.id.carte:
        	  ttsProviderImpl.say(getString(R.string.carte));
        	  if (latitude!=null && longitude!=null) {
        		  Intent carteActivity = new Intent(VolActivity.this, CarteActivity.class);
            	  carteActivity.putExtra("latitude", latitude*1000000);
          		  carteActivity.putExtra("longitude", longitude*1000000);
          		  startActivity(carteActivity);  
        	  } else {
        		  ttsProviderImpl.say(getString(R.string.position_ko));
        	  }
              return true;
         case R.id.vols:
        	 ttsProviderImpl.say(getString(R.string.title_activity_vols));
        	 Intent volsActivity = new Intent(VolActivity.this, VolsActivity.class);
             startActivityForResult(volsActivity, 0);
             return true;
         case R.id.checklists:
        	 ttsProviderImpl.say(getString(R.string.checklist));
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
			ttsProviderImpl.say(getString(R.string.bye));
			finish();
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		String day = String.valueOf(dayOfMonth);
		if (day.length()<2) {day = "0" + day;}
		String month = String.valueOf(monthOfYear+1);
		if (month.length()<2) {month = "0" + month;}
		String y = String.valueOf(year);
		if (y.length()<2) {y = "0" + y;}
		flightDate.setText(day + "/" + month + "/" + y);
		datePickerDialog.hide();
	}
	
}
