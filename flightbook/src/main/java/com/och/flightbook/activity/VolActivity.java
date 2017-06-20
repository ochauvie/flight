package com.och.flightbook.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.och.flightbook.R;
import com.och.flightbook.activity.MyDialogInterface.DialogReturn;
import com.och.flightbook.model.Accu;
import com.och.flightbook.model.Aeronef;
import com.och.flightbook.model.GpsPosition;
import com.och.flightbook.model.Site;
import com.och.flightbook.model.Vol;
import com.och.flightbook.speech.TtsProviderFactory;
import com.och.flightbook.sqllite.DbSite;
import com.och.flightbook.sqllite.DbVol;
import com.och.flightbook.widget.FlightWidgetProvider;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class VolActivity extends Activity implements DialogReturn, OnTouchListener, OnDateSetListener {

    private static final String DIALOG_BACKUP = "BACKUP";
    private static final String DIALOG_EXIT = "EXIT";

	private MyDialogInterface myInterface;
	private EditText aeronef, minVol, minMot, secMot, note, lieu, flightDate, accuPropultion;
	private Double latitude, longitude, altitude;
	private String lieuGps;
	private float downXValue;
	private DatePickerDialog datePickerDialog = null;
	private TtsProviderFactory ttsProviderImpl;

    private Aeronef currentAeronef = null;
    private Accu currentAccu = null;
    private Site currentSite = null;

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
						  getString(R.string.lon) + ": " + longitude;// + "\n" +
						  //getString(R.string.alt) + ": " + altitude;
			}
		};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageButton selectAeronef, butGps, selectDate, butSite, butAccuPropultion;

        setContentView(R.layout.activity_vol);
        
        ttsProviderImpl = TtsProviderFactory.getInstance();
               
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f , 1.0f ) ;
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(1200);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutVol);
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
        accuPropultion =  (EditText)  findViewById(R.id.textViewAccuPropultion);
        accuPropultion.setEnabled(false);

        // Gps
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationMgr!=null) {
        	List<String> providers = locationMgr.getAllProviders();

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    if (providers!=null) {
                        for (String prov : providers) {
                            if (LocationManager.NETWORK_PROVIDER.equals(prov)) {
                                locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100, onLocationChange);
                            }
                            if (LocationManager.GPS_PROVIDER.equals(prov)) {
                                locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100, onLocationChange);
                            }
                        }
                    }
        	}
        }
        
		// Add these two lines
        relativeLayout.setOnTouchListener(this);
        
        // Init aeronef selection by HangarActivity or SplashActivity (Nfc tag)
        initPage();
        

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
                    if (ttsProviderImpl != null) {
        			    ttsProviderImpl.say(getString(R.string.position_ko));
                     }
                    Toast.makeText(getBaseContext(),getString(R.string.position_ko), Toast.LENGTH_LONG).show();
        		} else {
                    lieu.setText(lieuGps);
                }

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

        // Hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        currentSite = DbSite.getDefaultSite();
        if (currentSite!=null) {
            lieu.setText(currentSite.getName());
            toSay = toSay + " "  + currentSite.getName();
        }

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

        if (!"".equals(toSay) && ttsProviderImpl != null) {
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
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationMgr.removeUpdates(onLocationChange);
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
       // MenuItem item = menu.findItem(R.id.carte);
        //item.setVisible(false);
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
          case R.id.meteo:
              if (ttsProviderImpl != null) {
                  ttsProviderImpl.say(getString(R.string.meteo));
              }
        	  Intent myIntent = new Intent(VolActivity.this, MeteoActivity.class);
              startActivityForResult(myIntent, 0);
              return true;
          case R.id.radio:
              if (ttsProviderImpl != null) {
                  ttsProviderImpl.say(getString(R.string.radio));
              }
        	  Intent radiosActivity = new Intent(VolActivity.this, RadiosActivity.class);
          	  startActivity(radiosActivity);
              return true;
          case R.id.carte:
              if (ttsProviderImpl != null) {
                  ttsProviderImpl.say(getString(R.string.carte));
              }
        	  if (latitude!=null && longitude!=null) {
        		  Intent carteActivity = new Intent(VolActivity.this, CarteActivity.class);
                  carteActivity.putExtra(GpsPosition.class.getName(), new GpsPosition(latitude, longitude, 0));
          		  startActivity(carteActivity);
        	  } else {
                  if (ttsProviderImpl != null) {
                      ttsProviderImpl.say(getString(R.string.position_ko));
                  }
                  Toast.makeText(getBaseContext(),getString(R.string.position_ko), Toast.LENGTH_LONG).show();
        	  }
              return true;
         case R.id.vols:
             if (ttsProviderImpl != null) {
                 ttsProviderImpl.say(getString(R.string.title_activity_vols));
             }
        	 Intent volsActivity = new Intent(VolActivity.this, VolsActivity.class);
             startActivityForResult(volsActivity, 0);
             return true;
         case R.id.checklists:
             if (ttsProviderImpl != null) {
                 ttsProviderImpl.say(getString(R.string.checklist));
             }
        	 Intent checklistsActivity = new Intent(VolActivity.this, ChecklistsActivity.class);
             startActivityForResult(checklistsActivity, 0);
             return true;
         case R.id.backup:
             Intent exportActivity = new Intent(VolActivity.this, ExportActivity.class);
             startActivityForResult(exportActivity, 0);
             return true;
         case R.id.save:
             saveFlight();
             return true;
         case R.id.reset:
             resetPage();
             return true;
         case R.id.close:
              exitApp();
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


    private void updateMyWidgets() {
        Intent intent = new Intent(this, FlightWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), FlightWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);
    }

    private void saveFlight() {
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

            DbVol.insertVol(vol);

            resetPage();

            String sSecondsMOteur = String.valueOf(vol.getSecondsMoteur());
            if (vol.getSecondsMoteur()<10) {sSecondsMOteur="0"+sSecondsMOteur;}

            AlertDialog.Builder builder = new AlertDialog.Builder(VolActivity.this);
            builder.setCancelable(true);
            builder.setIcon(R.drawable.recorder);
            builder.setTitle(getString(R.string.save_ok));
            String say = getString(R.string.save_ok) + " " + getString(R.string.st_for) + " " + vol.getAeronef();
            if (ttsProviderImpl != null) {
                ttsProviderImpl.say(say);
            }

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
            Toast.makeText(getBaseContext(), getString(R.string.aeronef_mandatory), Toast.LENGTH_LONG).show();
            if (ttsProviderImpl != null) {
                ttsProviderImpl.say(getString(R.string.aeronef_mandatory));
            }
        }


        updateMyWidgets();
    }


    @Override
    public void onDialogCompleted(boolean answer, String type) {
        if (DIALOG_EXIT.equals(type)) {
            if (answer) {
                if (ttsProviderImpl != null) {
                    ttsProviderImpl.say(getString(R.string.bye));
                }
                finish();
            }
        }
    }

    private void exitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.fermeture);
        builder.setTitle(R.string.close);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myInterface.getListener().onDialogCompleted(true, DIALOG_EXIT);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myInterface.getListener().onDialogCompleted(false, DIALOG_EXIT);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        // Nothings
    }

}
