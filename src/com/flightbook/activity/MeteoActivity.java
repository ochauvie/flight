package com.flightbook.activity;

import com.flightbook.R;

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
	private WebView webView;
	private EditText editTextOaci;
	private ProgressDialog mProgress;

    private String meteoCunimbUrlStart = "http://cunimb.net/decodemet.php?station=";
    private String meteoCunimbUrlEnd = "";

    private String meteoADDSUrlStart = "http://www.aviationweather.gov/adds/metars/?station_ids=";
    private String meteoADDSUrlEnd = "&std_trans=translated&chk_metars=on&hoursStr=most+recent+only&submitmet=Submit";


    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo);

		editTextOaci = (EditText) findViewById(R.id.editTextOaci);
		editTextOaci.setText(R.string.defaultOaci);
		
		// Progress dialog
        mProgress = ProgressDialog.show(this, getString(R.string.download), getString(R.string.wainting));

        // Add a WebViewClient for WebView, which actually handles loading data from web
		webView = (WebView) findViewById(R.id.webview);
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
                    webView.loadUrl(url);
                }
            }
        });
			
	}
	
}