package com.olivier.activity;

import java.util.List;


import com.olivier.R;
import com.olivier.nfc.ParsedNdefRecord;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

@Deprecated
public class RActivity extends Activity {
	
	 static final String TAG = "ViewTag";
	 private NfcAdapter mAdapter;

	    /**
	     * This activity will finish itself in this amount of time if the user
	     * doesn't do anything.
	     */
	    static final int ACTIVITY_TIMEOUT_MS = 1 * 1000;

	    TextView mTitle;

	    LinearLayout mTagContent;
	
    @SuppressLint("NewApi")
	@Override
	public void onResume() {
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    		mAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }
	    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_viewer);
        mTagContent = (LinearLayout) findViewById(R.id.list);
        mTitle = (TextView) findViewById(R.id.title);
		resolveIntent(getIntent());
		
		
	}
	
	private void resolveIntent(Intent intent) {
        // Parse the intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            // When a tag is discovered we send it to the service to be save. We
            // include a PendingIntent for the service to call back onto. This
            // will cause this activity to be restarted with onNewIntent(). At
            // that time we read it from the database and view it.
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                
            }
            // Setup the views
            setTitle(R.string.title_scanned_tag);
            //buildTagViews(msgs);
        } else {
            Log.e(TAG, "Unknown intent " + intent);
            finish();
            return;
        }
    }
	
	
	
}