package com.flightbook.activity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.flightbook.R;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbBackup;
import com.flightbook.sqllite.DbVol;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


@TargetApi(10)
public class SplashActivity extends Activity implements MyDialogInterface.DialogReturn {

    private Tag mytag;
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
	private IntentFilter writeTagFilters[];
	private boolean writeMode;
 	private ImageView imgStart;
    private ImageView imgWelcome;

    private MyDialogInterface myInterface;
 	private TtsProviderFactory ttsProviderImpl;

    private DbVol dbVol = new DbVol(this);
    private ArrayList<Vol> vols;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        // Init Speech
        ttsProviderImpl = TtsProviderFactory.getInstance();
        if (ttsProviderImpl != null) {
            ttsProviderImpl.init(this.getApplication().getApplicationContext());
        }
        
        adapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
		writeTagFilters = new IntentFilter[] { tagDetected };
		
		imgStart = (ImageView) findViewById(R.id.imgStart);
		imgStart.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				SplashActivity.this.finish();
		        Intent volActivity = new Intent(SplashActivity.this, VolActivity.class);
		        SplashActivity.this.startActivity(volActivity);
			}
		});


        imgWelcome = (ImageView) findViewById(R.id.imgWelcome);
        imgWelcome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                backupDb();
            }
        });
		
		processIntent(getIntent());
		
    }
	
    @Override
    protected void onResume()
    {
        super.onResume();
        WriteModeOn(); // NFC
        processIntent(getIntent());
    }
    
    
	@Override
	protected void onNewIntent(Intent intent){
		setIntent(intent);
	}
	
	private void processIntent(Intent intent) {
		String aeronefName = null;
    	String aeronefType = null;
    	
    	// ACTION_NDEF_DISCOVERED when application start 
    	// ACTION_TAG_DISCOVERED  when read tag in splash screen
		if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);    
			Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vib.vibrate(500);
			//Toast.makeText(this, "Tag detecte", Toast.LENGTH_LONG ).show();
			
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				for (int i = 0; i < rawMsgs.length; i++) {
					NdefMessage message  = (NdefMessage) rawMsgs[i];
					
					for (NdefRecord record:message.getRecords()) {
						byte[] id = record.getId();
						short tnf = record.getTnf();
						byte[] type = record.getType();
						try {
							String msg = new String( record.getPayload() , "Cp1252" );
							if (msg!=null) {msg.trim();}
							if (msg!=null && msg.startsWith("\tCDV_NAME_")) {
								aeronefName = msg.substring(10, msg.length());
							}
							if (msg!=null && msg.startsWith("\tCDV_TYPE_")) {
								aeronefType = msg.substring(10, msg.length());
							}
							//Toast.makeText(this, "Msg: " + msg, Toast.LENGTH_LONG ).show();
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		// Start VolActivity if we have a aeronef
		if (aeronefType!=null && aeronefName!=null) {
			SplashActivity.this.finish();
	        Intent volActivity = new Intent(SplashActivity.this, VolActivity.class);
	        volActivity.putExtra(Aeronef.NAME, aeronefName);
	    	volActivity.putExtra(Aeronef.TYPE, aeronefType);
	        SplashActivity.this.startActivity(volActivity);
		}
	}
    
    @Override
	public void onPause(){
		super.onPause();
		WriteModeOff();
	}
    
    private void WriteModeOn(){
		writeMode = true;
		if (adapter!= null) {
			adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
		}
	}

	private void WriteModeOff(){
		writeMode = false;
		if (adapter!= null) {
			adapter.disableForegroundDispatch(this);
		}
	}

    private void backupDb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.save);
        builder.setTitle("Data base backup");
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
    // Backup datata base
    public void onDialogCompleted(boolean answer) {
        if (answer) {
            DbBackup dbBackup = new DbBackup(this);
            try {
                dbBackup.doBackup();
                Toast.makeText(getBaseContext(),
                        "Done writing SD 'CarnetVolBackup.txt'",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
