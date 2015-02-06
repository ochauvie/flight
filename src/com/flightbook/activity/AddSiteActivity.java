package com.flightbook.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.flightbook.R;
import com.flightbook.model.Site;
import com.flightbook.sqllite.DbSite;


import java.util.ArrayList;

@TargetApi(14)
public class AddSiteActivity extends Activity {

	private DbSite dbSite = new DbSite(this);
	private Site site = null;

	private EditText name, comment;
	private TextView log;
	private ImageButton save, close;
    private CheckBox siteDefault;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_site);

	        log = (TextView) findViewById(R.id.textViewLog);
	        log.setText("");
	        log.setTextColor(Color.RED);
	        
	        name = (EditText) findViewById(R.id.editTextName);
	        comment = (EditText) findViewById(R.id.editTextComment);
            siteDefault = (CheckBox) findViewById(R.id.site_default);
	        

	        // Get site in parameter
	        initView();
	        
	        // Close view
	        close = (ImageButton) findViewById(R.id.close);
	        close.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		Intent siteActivity = new Intent(getApplicationContext(),SiteActivity.class);
                    // TODO passer les extra
	            	startActivity(siteActivity);
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
                            if (siteDefault.isChecked()) {
                                site.setIsDefault(1);
                            } else {
                                site.setIsDefault(0);
                            }

		        			dbSite.open();
                                if (site.getId()!=0) {
                                    dbSite.updateSite(site);
                                } else {
                                    dbSite.insertSite(site);
                                }
                                if (site.getIsDefault()==1) {
                                    ArrayList<Site> oldSites = dbSite.getSites();
                                    for (Site oldSite:oldSites) {
                                        if (oldSite.getIsDefault()==1 && !oldSite.getName().equals(site.getName()))
                                        {
                                            oldSite.setIsDefault(0);
                                            dbSite.updateSite(oldSite);
                                        }
                                    }
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
	        	site = (Site)bundle.getSerializable(Site.class.getName());
                if (site!=null) {
                    name.setText(site.getName());
                    comment.setText(site.getComment());
                    siteDefault.setChecked(false);
                    if (site.getIsDefault()==1) {
                        siteDefault.setChecked(true);
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
