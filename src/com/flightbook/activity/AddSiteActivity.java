package com.flightbook.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flightbook.R;
import com.flightbook.model.Site;
import com.flightbook.sqllite.DbSite;


import java.util.ArrayList;

@TargetApi(14)
public class AddSiteActivity extends Activity {

	private DbSite dbSite = new DbSite(this);
	private Site site = null;

	private EditText name, comment;
	private CheckBox siteDefault;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_site);

	        name = (EditText) findViewById(R.id.editTextName);
	        comment = (EditText) findViewById(R.id.editTextComment);
            siteDefault = (CheckBox) findViewById(R.id.site_default);

	        // Get site in parameter
	        initView();

            // Hide keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.saveclose, menu);
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
                Intent siteActivity = new Intent(getApplicationContext(),SiteActivity.class);
                // TODO passer les extra
                startActivity(siteActivity);
                finish();
                return true;
        }
        return false;
    }

    private void onSave() {
        Editable edName = name.getText();
        if (edName==null || "".equals(edName.toString())) {
            Toast.makeText(getBaseContext(), getString(R.string.name_mandatory), Toast.LENGTH_LONG).show();
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

                Toast.makeText(getBaseContext(), getString(R.string.site_save), Toast.LENGTH_LONG).show();

                Intent siteActivity = new Intent(getApplicationContext(),SiteActivity.class);
                startActivity(siteActivity);
                // TODO  passer les extra
                finish();
            } catch (NumberFormatException nfe) {
                Toast.makeText(getBaseContext(), getString(R.string.number_format_ko), Toast.LENGTH_LONG).show();
            }
        }
    }
}
