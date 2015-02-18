package com.flightbook.activity;


import java.io.UnsupportedEncodingException;

import com.flightbook.R;
import com.flightbook.model.Aeronef;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbAeronef;
import com.flightbook.sqllite.init.DbApplicationContext;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
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



@TargetApi(10)
public class SplashActivity extends Activity {

    private Tag mytag;
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
	private IntentFilter writeTagFilters[];
	private boolean writeMode;
 	private ImageView imgStart;
 	private TtsProviderFactory ttsProviderImpl;


	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Init db application context
        DbApplicationContext.getInstance().init(getApplicationContext());
        // Init db application context

        // Init Speech
        ttsProviderImpl = TtsProviderFactory.getInstance();
        if (ttsProviderImpl != null) {
            ttsProviderImpl.init(this.getApplication().getApplicationContext());
        }
        
        adapter = NfcAdapter.getDefaultAdapter(this);  // adapter is null if no NFC
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

        Intent volActivity = new Intent(SplashActivity.this, VolActivity.class);

		// Start VolActivity if we have a aeronef
		if (aeronefType!=null && aeronefName!=null) {

            Aeronef aeronef = DbAeronef.getAeronefByNameAndType(aeronefName, aeronefType);
            if (aeronef!=null) {
                volActivity.putExtra(Aeronef.class.getName(), aeronef);
            }
		}

        SplashActivity.this.finish();
        SplashActivity.this.startActivity(volActivity);

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





}
