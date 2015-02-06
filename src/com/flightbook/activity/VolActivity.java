package com.flightbook.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.model.Accu;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Site;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbSite;
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
    private DbSite dbSite = new DbSite(this);
	private RelativeLayout relativeLayout;
	private ImageButton saveButton, deleteButton;
	private EditText aeronef, minVol, minMot, secMot, note, lieu, flightDate, accuPropultion;
	private Double latitude, longitude, altitude;
	private String lieuGps;
	private ImageButton selectAeronef, butGps, selectDate, butSite, butAccuPropultion;
	private float downXValue;
	private TextView log;
	private DatePickerDialog datePickerDialog = null;
	private TtsProviderFactory ttsProviderImpl;

    private Aeronef currentAeronef = null;
    private Accu currentAccu = null;
    private Site currentSite = null;

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
        //minVol.requestFocus();
        minMot = (EditText)  findViewById(R.id.editTextMinMot);
        secMot = (EditText)  findViewById(R.id.editTextSecMot);
        note = (EditText)  findViewById(R.id.editTextNote);
        lieu = (EditText)  findViewById(R.id.editTextLieu);
        log = (TextView)  findViewById(R.id.log);
        log.setTextColor(Color.RED);
        accuPropultion =  (EditText)  findViewById(R.id.textViewAccuPropultion);
        accuPropultion.setEnabled(false);

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
                    if (currentAeronef!=null) {
                        vol.setType(currentAeronef.getType());
                    }
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
	        		if (currentSite!= null) {
                        vol.setLieu(currentSite.getName());
                    }
                    vol.setAccuPropulsion(currentAccu);
	        		
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
                            + getString(R.string.accu_propultion) + ": \t\t" + (vol.getAccuPropulsion()!=null?vol.getAccuPropulsion().getNom():"") + "\n"
	            			+ getString(R.string.note) + ": \t\t\t" +  vol.getNote() + "\n"
                            + getString(R.string.lieu) + ": \t\t\t"+ (vol.getLieu()!=null?vol.getLieu():"");
	            	
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
                    log.setText(getString(R.string.aeronef_mandatory));
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
                startActivity(getIntentWithExtra(v.getContext(), HangarActivity.class));
                finish();
        	}
        });

        // Find accu
        butAccuPropultion = (ImageButton) findViewById(R.id.selectAccuPropultion);
        butAccuPropultion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntentWithExtra(v.getContext(), AccusActivity.class));
                finish();
            }
        });
        
        // Position GPS
        butGps = (ImageButton) findViewById(R.id.gps);
        butGps.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if (lieuGps==null || "".equals(lieuGps)) {
        			ttsProviderImpl.say(getString(R.string.position_ko));
        		}
        		lieu.setText(lieuGps);
        	}
        });

        // Liste Positions
        butSite = (ImageButton) findViewById(R.id.site);
        butSite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(getIntentWithExtra(v.getContext(), SiteActivity.class));
                finish();
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
        currentAccu = null;
        accuPropultion.setText(null);
        currentSite = null;

        String toSay = "";

        // Try to find default site
        dbSite.open();
        currentSite = dbSite.getDefaultSite();
        if (currentSite!=null) {
            lieu.setText(currentSite.getName());
            toSay = toSay + " "  + currentSite.getName();
        }
        dbSite.close();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {

        	currentAeronef = (Aeronef)bundle.getSerializable(Aeronef.class.getName());
        	if (currentAeronef!=null) {
        		aeronef.setText(currentAeronef.getName());
                toSay = currentAeronef.getName();
        	}

            currentSite = (Site)bundle.getSerializable(Site.class.getName());
            if (currentSite!=null) {
                lieu.setText(currentSite.getName());
                toSay = toSay + " "  + currentSite.getName();
            }

            String sFlightDate = bundle.getString(Vol.DATE);
            if (sFlightDate!=null) {
                flightDate.setText(sFlightDate);
            }
            String sMinVol = bundle.getString(Vol.MIN_VOL);
            if (sMinVol!=null) {
                minVol.setText(sMinVol);
            }
            String sMinMot = bundle.getString(Vol.MIN_MOTEUR);
            if (sMinMot!=null) {
                minMot.setText(sMinMot);
            }
            String sSecMot = bundle.getString(Vol.SEC_MOTEUR);
            if (sSecMot!=null) {
                secMot.setText(sSecMot);
            }
            String sNote = bundle.getString(Vol.NOTE);
            if (sNote!=null && !"".equals(sNote)) {
                note.setText(sNote);
            }

            currentAccu = (Accu)bundle.getSerializable(Accu.class.getName());
            if (currentAccu!=null) {
                accuPropultion.setText(currentAccu.getNom());
            }
        }

        if (!"".equals(toSay)) {
            ttsProviderImpl.say(toSay);
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
        note.setText("");
        lieu.setText("");
        currentSite = null;
        log.setText("");
        accuPropultion.setText(null);
        currentAccu = null;
        currentAeronef = null;
        aeronef.setText(null);
    }

    private Intent getIntentWithExtra(Context context, Class myClass) {
        Intent myIntent = new Intent(context, myClass);
        myIntent.putExtra(Aeronef.class.getName(), currentAeronef);
        myIntent.putExtra(Accu.class.getName(), currentAccu);
        myIntent.putExtra(Site.class.getName(), currentSite);
        myIntent.putExtra(Vol.DATE, flightDate.getText().toString());
        myIntent.putExtra(Vol.MIN_VOL, minVol.getText().toString());
        myIntent.putExtra(Vol.MIN_MOTEUR, minMot.getText().toString());
        myIntent.putExtra(Vol.SEC_MOTEUR, secMot.getText().toString());
        myIntent.putExtra(Vol.NOTE, note.getText().toString());
        return myIntent;
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
