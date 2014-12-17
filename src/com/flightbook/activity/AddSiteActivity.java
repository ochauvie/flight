package com.flightbook.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.flightbook.R;
import com.flightbook.model.Site;
import com.flightbook.sqllite.DbSite;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

@TargetApi(14)
public class AddSiteActivity extends Activity {

	private DbSite dbSite = new DbSite(this);
	private Site site = null;

	private EditText name, comment;
	private TextView log;
	private ImageButton save, close;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_site);

	        log = (TextView) findViewById(R.id.textViewLog);
	        log.setText("");
	        log.setTextColor(Color.RED);
	        
	        name = (EditText) findViewById(R.id.editTextName);
	        comment = (EditText) findViewById(R.id.editTextComment);
	        

	        // Get site in parameter
	        initView();
	        
	        // Close view
	        close = (ImageButton) findViewById(R.id.close);
	        close.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		Intent hangarActivity = new Intent(getApplicationContext(),SiteActivity.class);
                    // TODO passer les extra
	            	startActivity(hangarActivity);
	            	 finish();
	        	}
	        });
	       
	        // Save view
	        save = (ImageButton) findViewById(R.id.save);
	        save.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		
	        		Editable edName = name.getText();
	        		if (edName==null || "".equals(edName.toString())) {
	        			log.setText(R.string.name_mandatory);
	        		} else {
	        			try {
	        				if (site==null) {
	        					site = new Site();
	    	        		} 
		        			site.setName(edName.toString());
		        			site.setComment(comment.getText().toString());

		        			dbSite.open();
		        			if (site.getId()!=0) {
		        				dbSite.updateSite(site);
		        			} else {
		        				dbSite.insertSite(site);
		        			}
		        			dbSite.close();
		        			
		        			log.setText(R.string.site_save);
		        			
		        			Intent siteActivity = new Intent(getApplicationContext(),SiteActivity.class);
			            	startActivity(siteActivity);
                            // TODO  passer les extra
			            	finish();
	        			} catch (NumberFormatException nfe) {
	        				log.setText(R.string.number_format_ko);
	        			}
	        		}
	        	}
	        });

	        
	    }
	    
	    private void initView() {
	    	Bundle bundle = getIntent().getExtras();
	        if (bundle!=null) {
	        	int siteId = bundle.getInt(Site.ID);
	        	if (siteId!=0) {
	        		dbSite.open();
	        			site = dbSite.getSiteById(siteId);
	        		dbSite.close();
	        		if (site!=null) {
	        			name.setText(site.getName());
	        	        comment .setText(site.getComment());
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
	    public void onBackPressed() {
			// Nothings
		}
}
