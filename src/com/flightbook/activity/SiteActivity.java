package com.flightbook.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ListView;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.SitesAdapter;
import com.flightbook.listener.SiteAdapterListener;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Site;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbSite;

import java.util.ArrayList;

public class SiteActivity extends ListActivity  implements DialogReturn, SiteAdapterListener {

	private ImageButton addSite;
	private ImageButton close;
	private ListView listView;
	private DbSite dbSite = new DbSite(this);
	private ArrayList<Site> sites;
	private SitesAdapter adapter;
	private MyDialogInterface myInterface;
	private int selectItim = -1;
	private TtsProviderFactory ttsProviderImpl;
	private String sAeronefName = null;
    private String sType = null;
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
        
        
        // Open view add site
        addSite = (ImageButton) findViewById(R.id.addSite);
        addSite.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), AddSiteActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
        	}
        });
        
        // Close view site selection
        close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
                volActivity.putExtra(Aeronef.NAME, sAeronefName);
                volActivity.putExtra(Aeronef.TYPE, sType);
                volActivity.putExtra(Vol.DATE, sFlightDate);
                volActivity.putExtra(Vol.MIN_VOL, sMinVol);
                volActivity.putExtra(Vol.MIN_MOTEUR, sMinMot);
                volActivity.putExtra(Vol.SEC_MOTEUR, sSecMot);
                volActivity.putExtra(Vol.NOTE, sNote);
                startActivity(volActivity);
            	finish();
        	}
        });
        
        ttsProviderImpl.say(getString(R.string.selectSite));

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            sAeronefName = bundle.getString(Aeronef.NAME);
            sType = bundle.getString(Aeronef.TYPE);
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
    	volActivity.putExtra(Site.NAME, sel.getName());
        volActivity.putExtra(Aeronef.NAME, sAeronefName);
        volActivity.putExtra(Aeronef.TYPE, sType);
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
    		myInterface.getListener().onDialogCompleted(true);
    	    dialog.dismiss();
    	  }
    	});
    	builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(false);
    		dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
		
		
	}
	
	@Override
	public void onDialogCompleted(boolean answer) {
		if (answer && selectItim!=-1) {
			dbSite.open();
	        dbSite.deleteSite(sites.get(selectItim));
	        dbSite.close();
	        
	        sites.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}

    
	@Override
    public void onBackPressed() {
		// Nothings
	}



}