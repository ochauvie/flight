package com.olivier.activity;

import com.olivier.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;


public class MeteoActivity extends Activity {
	
	private ImageButton mButton;
	private WebView webView;
	private EditText editTextOaci;
	private ProgressDialog mProgress;

	
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
		webView.loadUrl("http://cunimb.net/decodemet.php?station=" + editTextOaci.getText().toString());
		
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

		mButton = (ImageButton) findViewById(R.id.btnLaunch);
		mButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String oaci = editTextOaci.getText().toString();
        		if (oaci!=null && !"".equals(oaci)) {
        			String url = "http://cunimb.net/decodemet.php?station=" + oaci.toUpperCase();
        			//String url = "http://www.aviationweather.gov/adds/metars/?station_ids=" + oaci.toUpperCase() + "&std_trans=translated&chk_metars=on&hoursStr=most+recent+only&submitmet=Submit";
        			webView.loadUrl(url);
        		}
        	}
        });
			
	}
	
}