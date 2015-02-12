package com.flightbook.activity;

import com.flightbook.R;
import com.flightbook.speech.TtsProviderFactory;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;


public class MeteoActivity extends Activity {
	
	private ImageButton mButtonCunimb;
    private ImageButton mButtonADDS;
    private ImageButton mButtonAllMetarSat;
	private WebView webView;
	private EditText editTextOaci;
	private ProgressDialog mProgress;
    private TtsProviderFactory ttsProviderImpl;

    private String meteoCunimbSay = "météo avec cunimb pour ";
    private String meteoCunimbUrlStart = "http://cunimb.net/decodemet.php?station=";
    private String meteoCunimbUrlEnd = "";

    private String meteoADDSay = "météo avec aviation weather pour ";
    private String meteoADDSUrlStart = "http://www.aviationweather.gov/adds/metars/?station_ids=";
    private String meteoADDSUrlEnd = "&std_trans=translated&chk_metars=on&hoursStr=most+recent+only&submitmet=Submit";

    private String meteoAllMetarSay = "météo avec metsat pour ";
    private String meteoAllMetarSatUrlStart = "http://fr.allmetsat.com/metar-taf/france.php?icao=";
    private String meteoAllMetarSatUrlEnd = "";


    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ttsProviderImpl = TtsProviderFactory.getInstance();

		editTextOaci = (EditText) findViewById(R.id.editTextOaci);
		editTextOaci.setText(R.string.defaultOaci);

		
		// Progress dialog
        mProgress = ProgressDialog.show(this, getString(R.string.download), getString(R.string.wainting));

        // Add a WebViewClient for WebView, which actually handles loading data from web
		webView = (WebView) findViewById(R.id.webview);
            ttsProviderImpl.say(meteoADDSay + editTextOaci.getText().toString());
		webView.loadUrl(meteoADDSUrlStart + editTextOaci.getText().toString() + meteoADDSUrlEnd);

		
		webView.setWebViewClient(new WebViewClient() {
            // load url
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
 
            // when finish loading page
            public void onPageFinished(WebView view, String url) {
                if(mProgress.isShowing()) {
                    mProgress.dismiss();
                }
            }
        });

		mButtonCunimb = (ImageButton) findViewById(R.id.btnLaunchCunimb);
		mButtonCunimb.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String oaci = editTextOaci.getText().toString();
        		if (oaci!=null && !"".equals(oaci)) {
        			String url = meteoCunimbUrlStart + oaci.toUpperCase() + meteoCunimbUrlEnd;
                    ttsProviderImpl.say(meteoCunimbSay + oaci.toUpperCase());
        			webView.loadUrl(url);
        		}
        	}
        });

        mButtonADDS = (ImageButton) findViewById(R.id.btnLaunchADDS);
        mButtonADDS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oaci = editTextOaci.getText().toString();
                if (oaci!=null && !"".equals(oaci)) {
                    String url = meteoADDSUrlStart + oaci.toUpperCase() + meteoADDSUrlEnd;
                    ttsProviderImpl.say(meteoADDSay + oaci.toUpperCase());
                    webView.loadUrl(url);
                }
            }
        });

        mButtonAllMetarSat = (ImageButton) findViewById(R.id.btnLaunchAllMetarSat);
        mButtonAllMetarSat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oaci = editTextOaci.getText().toString();
                if (oaci!=null && !"".equals(oaci)) {
                    String url = meteoAllMetarSatUrlStart + oaci.toUpperCase() + meteoAllMetarSatUrlEnd;
                    ttsProviderImpl.say(meteoAllMetarSay + oaci.toUpperCase());
                    webView.loadUrl(url);
                }
            }
        });
			
	}

    @Override
    public void onBackPressed() {
        // Nothings
    }
	
}