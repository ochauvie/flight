package com.flightbook.activity;

import com.flightbook.R;
import com.flightbook.speech.TtsProviderFactory;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class MeteoActivity extends Activity {
	
	private ImageButton mButtonCunimb, mButtonADDS, mButtonAllMetarSat;
	private WebView webView;
	private EditText editTextOaci;
	private ProgressDialog mProgress;
    private TtsProviderFactory ttsProviderImpl;

    private String meteoCunimbUrlStart = "http://cunimb.net/decodemet.php?station=";
    private String meteoCunimbUrlEnd = "";

    private String meteoADDSUrlStart = "http://www.aviationweather.gov/adds/metars/?station_ids=";
    private String meteoADDSUrlEnd = "&std_trans=translated&chk_metars=on&hoursStr=most+recent+only&submitmet=Submit";

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
        if (isNetworkAvailable()) {
            mProgress = ProgressDialog.show(this, getString(R.string.download), getString(R.string.wainting));
        }

        // Add a WebViewClient for WebView, which actually handles loading data from web
		webView = (WebView) findViewById(R.id.webview);
            ttsProviderImpl.say(getString(R.string.meteo_add_say) + " "+ editTextOaci.getText().toString());
        if (isNetworkAvailable()) {
            webView.loadUrl(meteoADDSUrlStart + editTextOaci.getText().toString() + meteoADDSUrlEnd);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.internet_ko), Toast.LENGTH_LONG).show();
        }

		webView.setWebViewClient(new WebViewClient() {
            // load url
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isNetworkAvailable()) {
                    view.loadUrl(url);
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.internet_ko), Toast.LENGTH_LONG).show();
                }
                return true;
            }
 
            // when finish loading page
            public void onPageFinished(WebView view, String url) {
                if (mProgress!=null) {
                    if (mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                }
            }
        });

		mButtonCunimb = (ImageButton) findViewById(R.id.btnLaunchCunimb);
		mButtonCunimb.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String oaci = editTextOaci.getText().toString();
        		if (oaci!=null && !"".equals(oaci)) {
        			String url = meteoCunimbUrlStart + oaci.toUpperCase() + meteoCunimbUrlEnd;
                    ttsProviderImpl.say(getString(R.string.meteo_cunimb_say) + " " + oaci.toUpperCase());
                    if (isNetworkAvailable()) {
                        webView.loadUrl(url);
                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.internet_ko), Toast.LENGTH_LONG).show();
                    }
        		}
        	}
        });

        mButtonADDS = (ImageButton) findViewById(R.id.btnLaunchADDS);
        mButtonADDS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oaci = editTextOaci.getText().toString();
                if (oaci!=null && !"".equals(oaci)) {
                    String url = meteoADDSUrlStart + oaci.toUpperCase() + meteoADDSUrlEnd;
                    ttsProviderImpl.say(getString(R.string.meteo_add_say) + " " + oaci.toUpperCase());
                    if (isNetworkAvailable()) {
                        webView.loadUrl(url);
                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.internet_ko), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mButtonAllMetarSat = (ImageButton) findViewById(R.id.btnLaunchAllMetarSat);
        mButtonAllMetarSat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oaci = editTextOaci.getText().toString();
                if (oaci!=null && !"".equals(oaci)) {
                    String url = meteoAllMetarSatUrlStart + oaci.toUpperCase() + meteoAllMetarSatUrlEnd;
                    ttsProviderImpl.say(getString(R.string.meteo_allmetar_say) + " " + oaci.toUpperCase());
                    if (isNetworkAvailable()) {
                        webView.loadUrl(url);
                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.internet_ko), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
			
	}

    @Override
    public void onBackPressed() {
        // Nothings
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
	
}