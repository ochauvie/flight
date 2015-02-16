package com.flightbook.activity;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.flightbook.R;
import com.flightbook.model.Aeronef;
import com.flightbook.sqllite.DbAeronef;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

@TargetApi(14)
public class AddAeronefActivity extends Activity {

	private DbAeronef dbAeronef = new DbAeronef(this);
	private Aeronef aeronef = null;

	private EditText name, wingSpan, weight, engine, firstFlight, comment;
	private RadioButton optPlaneur, optAvion, optParamoteur, optHelico, optAuto, optDivers;
	private RadioGroup rg1, rg2 ,rg3;
	    
	private Context ctx;
	private Tag mytag;
	private NfcAdapter adapter;
    private PendingIntent pendingIntent;
	private IntentFilter writeTagFilters[];
	private boolean writeMode;
    private Menu menu = null;

		// Listener to synchronize radio groups 
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
	        
	        // NFC writer
	        ctx=this;
            adapter = NfcAdapter.getDefaultAdapter(this);  // adapter is null if no NFC
			pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
			if (adapter!=null) {
                tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
                writeTagFilters = new IntentFilter[]{tagDetected};
            }
	        
	        rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
	    	rg2 = (RadioGroup) findViewById(R.id.radioGroup2);
	    	rg3 = (RadioGroup) findViewById(R.id.radioGroup3);
	        rg1.setOnCheckedChangeListener(listener1);
	        rg2.setOnCheckedChangeListener(listener2);
	        rg3.setOnCheckedChangeListener(listener3);
	        
	        name = (EditText) findViewById(R.id.editTextAeronef);
	        wingSpan = (EditText) findViewById(R.id.editTextWingSpan);
	        weight = (EditText) findViewById(R.id.editTextWeight);
	        engine = (EditText) findViewById(R.id.editTextEngine);
	        firstFlight = (EditText) findViewById(R.id.editTextFirstFlight);
	        comment = (EditText) findViewById(R.id.editTextComment);
	        
	        optPlaneur = (RadioButton) findViewById(R.id.option_planeur);
                optPlaneur.setTextColor(Aeronef.getColor(Aeronef.T_PLANEUR));
	        optAvion = (RadioButton) findViewById(R.id.option_avion);
                optAvion.setTextColor(Aeronef.getColor(Aeronef.T_AVION));
	        optParamoteur = (RadioButton) findViewById(R.id.option_paramoteur);
                optParamoteur.setTextColor(Aeronef.getColor(Aeronef.T_PARAMOTEUR));
	        optHelico = (RadioButton) findViewById(R.id.option_helico);
                optHelico.setTextColor(Aeronef.getColor(Aeronef.T_HELICO));
	        optAuto = (RadioButton) findViewById(R.id.option_auto);
                optAuto.setTextColor(Aeronef.getColor(Aeronef.T_AUTO));
	        optDivers = (RadioButton) findViewById(R.id.option_divers);
                optDivers.setTextColor(Aeronef.getColor(Aeronef.T_DIVERS));
	    
	        // Get aeronef in parameter
	        initView();

	        // Not visible if no nfc on device
            if (adapter==null && this.menu!=null) {
                MenuItem item = this.menu.findItem(R.id.nfc);
                item.setVisible(false);
            }

            // Hide keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        }
	    
	    private void initView() {
	    	Bundle bundle = getIntent().getExtras();
	        if (bundle!=null) {
                aeronef = (Aeronef)bundle.getSerializable(Aeronef.class.getName());
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

	    RadioButton.OnClickListener myOptionOnClickListener =
	    	new RadioButton.OnClickListener() {
	    		  @Override
	    		  public void onClick(View v) {
	    		  }
	    };
	    
	   
		@Override
		protected void onNewIntent(Intent intent){
			if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
				mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);    
				Toast.makeText(this, R.string.nfc_tag_detected, Toast.LENGTH_LONG ).show();
			}
	    }
			
	    
	    @Override
	    protected void onResume()
	    {
	        super.onResume();
	        WriteModeOn(); // NFC
	    }

	    @Override
		public void onPause(){
			super.onPause();
			WriteModeOff();
		}

	    
	    private void WriteModeOn(){
			writeMode = true;
            if (adapter!=null) {
                adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
            }
		}

		private void WriteModeOff(){
			writeMode = false;
            if (adapter!=null) {
                adapter.disableForegroundDispatch(this);
            }
		}
		
		private int write(String name, String type, Tag tag) throws IOException, FormatException {
			NdefRecord appRecord = NdefRecord.createApplicationRecord("com.flightbook");
			NdefRecord[] records = {createRecord(name, "CDV_NAME_"), createRecord(type, "CDV_TYPE_"), appRecord};
			NdefMessage  message = new NdefMessage(records);
			// Get an instance of Ndef for the tag.
			Ndef ndef = Ndef.get(tag);
			if (ndef==null) {
				// Tag not formated ?
				return -1;
			} else {
				// Enable I/O	
				ndef.connect();
				// Write the message
				ndef.writeNdefMessage(message);
				// Close the connection
				ndef.close();
			}
			return 0;
		}
		
		private NdefRecord createRecord(String text, String prefix) throws UnsupportedEncodingException {
			String lang       = prefix;
			byte[] textBytes  = text.getBytes();
			byte[] langBytes  = lang.getBytes("US-ASCII");
			int    langLength = langBytes.length;
			int    textLength = textBytes.length;
			byte[] payload    = new byte[1 + langLength + textLength];

			// set status byte (see NDEF spec for actual bits)
			payload[0] = (byte) langLength;

			// copy langbytes and textbytes into payload
			System.arraycopy(langBytes, 0, payload, 1,              langLength);
			System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

			//NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);
			String mimeType = "application/com.flightbook";
			byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));
			NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,  mimeBytes,  new byte[0], payload);
			return recordNFC;
		}

		@Override
	    public void onBackPressed() {
			// Nothings
		}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savenfcclose, menu);
        this.menu = menu;

        // Si pas de NFC sur le device ou si l'aeronef n'est pas encore enregistré, pas de tag
        if (adapter==null || aeronef==null) {
            MenuItem item = this.menu.findItem(R.id.nfc);
            item.setVisible(false);
        }
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                onSave();
                return true;
            case R.id.close:
                Intent hangarActivity = new Intent(getApplicationContext(),HangarActivity.class);
                startActivity(hangarActivity);
                finish();
                return true;
            case R.id.nfc:
                onNfc();
                return true;
        }
        return false;
    }

    private void onSave() {
        Editable edName = name.getText();
        if (edName==null || "".equals(edName.toString())) {
            Toast.makeText(getBaseContext(), getString(R.string.name_mandatory), Toast.LENGTH_LONG).show();
        } else if (!optPlaneur.isChecked() && !optAvion.isChecked() &&
                !optParamoteur.isChecked() && !optHelico.isChecked() &&
                !optAuto.isChecked() && !optDivers.isChecked()) {
            Toast.makeText(getBaseContext(), getString(R.string.type_mandatory), Toast.LENGTH_LONG).show();
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

                dbAeronef.open();
                if (aeronef.getId()!=0) {
                    dbAeronef.updateAeronef(aeronef);
                } else {
                    dbAeronef.insertAeronef(aeronef);
                }
                dbAeronef.close();

                Toast.makeText(getBaseContext(), getString(R.string.aeronef_save), Toast.LENGTH_LONG).show();

                Intent hangarActivity = new Intent(getApplicationContext(),HangarActivity.class);
                startActivity(hangarActivity);
                finish();
            } catch (NumberFormatException nfe) {
                Toast.makeText(getBaseContext(), getString(R.string.number_format_ko), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onNfc() {
        if (aeronef!=null) {
            String aerName = aeronef.getName();
            String aerType = aeronef.getType();
            if (aerName!= null && aerType!= null) {
                if(mytag==null){
                    Toast.makeText(ctx, R.string.nfc_approch_tag, Toast.LENGTH_LONG ).show();
                } else {
                    try {
                        int result = write(aerName, aerType, mytag);
                        if (result!=0) {
                            Toast.makeText(ctx, R.string.nfc_write_ko_format, Toast.LENGTH_LONG ).show();
                        } else {
                            Toast.makeText(ctx, R.string.nfc_write_ok, Toast.LENGTH_LONG ).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(ctx, R.string.nfc_write_ko, Toast.LENGTH_LONG ).show();
                        //e.printStackTrace();
                    } catch (FormatException e) {
                        Toast.makeText(ctx, R.string.nfc_write_ko , Toast.LENGTH_LONG ).show();
                        //e.printStackTrace();
                    }
                }
            }
        } else {
            Toast.makeText(ctx, R.string.nfc_save_before, Toast.LENGTH_LONG ).show();
        }
    }
}
