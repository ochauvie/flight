package com.flightbook.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.Toast;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.SitesAdapter;
import com.flightbook.listener.SiteAdapterListener;
import com.flightbook.model.Accu;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Site;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbBackup;
import com.flightbook.sqllite.DbJsonImport;
import com.flightbook.sqllite.DbSite;
import com.flightbook.tools.SimpleFileDialog;

import java.io.File;
import java.util.ArrayList;

public class SiteActivity extends ListActivity  implements DialogReturn, SiteAdapterListener {

	private ListView listView;
	private DbSite dbSite = new DbSite(this);
	private ArrayList<Site> sites;
	private SitesAdapter adapter;
	private MyDialogInterface myInterface;
	private int selectItim = -1;
	private TtsProviderFactory ttsProviderImpl;
	private Aeronef currentAeronef = null;
    private Accu currentAccu = null;
    private String sFlightDate = null;
    private String sMinVol = null;
    private String sMinMot = null;
    private String sSecMot = null;
    private String sNote = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);
        
        ttsProviderImpl = TtsProviderFactory.getInstance();
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbSite.open();
        sites = dbSite.getSites();
        dbSite.close();

        listView = getListView();
        
        // Creation et initialisation de l'Adapter pour les sites
        adapter = new SitesAdapter(this, sites);
            
        // Ecoute des evenements sur votre liste
        adapter.addListener(this);
        

        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);
        
        // Animation
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
        				this, R.anim.list_layout_controller);
        listView.setLayoutAnimation(controller);

        ttsProviderImpl.say(getString(R.string.selectSite));

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            currentAeronef = (Aeronef)bundle.getSerializable(Aeronef.class.getName());
            currentAccu = (Accu)bundle.getSerializable(Accu.class.getName());
            sFlightDate = bundle.getString(Vol.DATE);
            sMinVol = bundle.getString(Vol.MIN_VOL);
            sMinMot = bundle.getString(Vol.MIN_MOTEUR);
            sSecMot = bundle.getString(Vol.SEC_MOTEUR);
            sNote = bundle.getString(Vol.NOTE);
        }

    }
    
    
    @Override
	public void onClickName(Site item, int position) {
    	Site sel = sites.get(position);
        Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
    	volActivity.putExtra(Site.class.getName(), sel);
        volActivity.putExtra(Aeronef.class.getName(), currentAeronef);
        volActivity.putExtra(Accu.class.getName(), currentAccu);
        volActivity.putExtra(Vol.DATE, sFlightDate);
        volActivity.putExtra(Vol.MIN_VOL, sMinVol);
        volActivity.putExtra(Vol.MIN_MOTEUR, sMinMot);
        volActivity.putExtra(Vol.SEC_MOTEUR, sSecMot);
        volActivity.putExtra(Vol.NOTE, sNote);
        startActivity(volActivity);
    	finish();
	}
    

    
	@Override
	public void onClickNameToDelete(Site item, int position) {
		Site sel = sites.get(position);
		selectItim = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(sel.getName());
    	builder.setInverseBackgroundForced(true);
    	builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(true, null);
    	    dialog.dismiss();
    	  }
    	});
    	builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(false, null);
    		dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
		
		
	}
	
	@Override
	public void onDialogCompleted(boolean answer, String type) {
		if (answer && selectItim!=-1) {
			dbSite.open();
	        dbSite.deleteSite(sites.get(selectItim));
	        dbSite.close();
	        
	        sites.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}


    @Override
    public void onClickToUpdate(Site item, int position) {
        Intent addSiteActivity = new Intent(getApplicationContext(), AddSiteActivity.class);
        addSiteActivity.putExtra(Site.class.getName(), item);
        startActivity(addSiteActivity);
        finish();
    }


    @Override
    public void onBackPressed() {
		// Nothings
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addshareimportclose, menu);
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent myIntent = new Intent(getApplicationContext(), AddSiteActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
                return true;
            case R.id.close:
                Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
                volActivity.putExtra(Aeronef.class.getName(), currentAeronef);
                volActivity.putExtra(Accu.class.getName(), currentAccu);
                volActivity.putExtra(Vol.DATE, sFlightDate);
                volActivity.putExtra(Vol.MIN_VOL, sMinVol);
                volActivity.putExtra(Vol.MIN_MOTEUR, sMinMot);
                volActivity.putExtra(Vol.SEC_MOTEUR, sSecMot);
                volActivity.putExtra(Vol.NOTE, sNote);
                startActivity(volActivity);
                finish();
                return true;
            case R.id.send:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                DbBackup dbBackup = new DbBackup(this);
                sendIntent.putExtra(Intent.EXTRA_TEXT, dbBackup.getStbSites().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.importData:
                importSites();
                return true;

        }
        return false;
    }


    /**
     * Import sites from file
     */
    private void importSites() {
        SimpleFileDialog FileOpenDialog = new SimpleFileDialog(this, "FileOpen",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
                        File file = new File(chosenDir);
                        // Initiate the upload
                        DbJsonImport dbJsonImport = new DbJsonImport(SiteActivity.this);
                        String result = dbJsonImport.importSites(file);
                        if (result!=null) {
                            Toast.makeText(SiteActivity.this, result, Toast.LENGTH_LONG).show();
                        } else {
                            // Update the list
                            reloadAllSite();
                            Toast.makeText(SiteActivity.this, getString(R.string.menu_import_ok), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        FileOpenDialog.Default_File_Name = "";
        FileOpenDialog.chooseFile_or_Dir();
    }

    private void reloadAllSite() {
        if (sites!=null) {
            for (int i=sites.size()-1; i>=0; i--) {
                sites.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        if (sites!=null) {
            dbSite.open();
            sites.addAll(dbSite.getSites());
            dbSite.close();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(SiteActivity.this, getString(R.string.import_reload_list), Toast.LENGTH_LONG).show();
        }


    }

}
