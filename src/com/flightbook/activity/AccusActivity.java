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


import com.flightbook.adapter.AccusAdapter;
import com.flightbook.listener.AccuAdapterListener;
import com.flightbook.model.Accu;
import com.flightbook.model.Aeronef;

import com.flightbook.model.Site;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbAccu;

import java.util.ArrayList;

public class AccusActivity extends ListActivity  implements DialogReturn, AccuAdapterListener {

	private ImageButton addAccu;
	private ImageButton close;
	private ListView listView;
	private DbAccu dbAccu = new DbAccu(this);
	private ArrayList<Accu> accus;
	private AccusAdapter adapter;
	private MyDialogInterface myInterface;
	private int selectItim = -1;
	private TtsProviderFactory ttsProviderImpl;

    private Aeronef currentAeronef = null;
    private Site currentSite = null;
    private String sFlightDate = null;
    private String sMinVol = null;
    private String sMinMot = null;
    private String sSecMot = null;
    private String sNote = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accus);
        
        ttsProviderImpl = TtsProviderFactory.getInstance();
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        dbAccu.open();
        accus = dbAccu.getAccus();
        dbAccu.close();

        View header = getLayoutInflater().inflate(R.layout.activity_header_accus, null);
        listView = getListView();
        listView.addHeaderView(header);
        
        // Creation et initialisation de l'Adapter pour les accus
        adapter = new AccusAdapter(this, accus);
            
        // Ecoute des evenements sur votre liste
        adapter.addListener(this);
        

        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);
        
        // Animation
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
        				this, R.anim.list_layout_controller);
        listView.setLayoutAnimation(controller);
        
        
        // Open view add accu
        addAccu = (ImageButton) findViewById(R.id.addAccu);
        addAccu.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), AddAccuActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
        	}
        });
        
        // Close view site selection
        close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
                volActivity.putExtra(Aeronef.class.getName(), currentAeronef);
                volActivity.putExtra(Site.class.getName(), currentSite);
                volActivity.putExtra(Vol.DATE, sFlightDate);
                volActivity.putExtra(Vol.MIN_VOL, sMinVol);
                volActivity.putExtra(Vol.MIN_MOTEUR, sMinMot);
                volActivity.putExtra(Vol.SEC_MOTEUR, sSecMot);
                volActivity.putExtra(Vol.NOTE, sNote);
                startActivity(volActivity);
            	finish();
        	}
        });
        
        ttsProviderImpl.say(getString(R.string.selectAccu));

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            currentAeronef = (Aeronef)bundle.getSerializable(Aeronef.class.getName());
            currentSite = (Site)bundle.getSerializable(Site.class.getName());
            sFlightDate = bundle.getString(Vol.DATE);
            sMinVol = bundle.getString(Vol.MIN_VOL);
            sMinMot = bundle.getString(Vol.MIN_MOTEUR);
            sSecMot = bundle.getString(Vol.SEC_MOTEUR);
            sNote = bundle.getString(Vol.NOTE);
        }

    }
    
    
    @Override
	public void onClickName(Accu item, int position) {
    	Accu sel = accus.get(position);
        Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
    	volActivity.putExtra(Accu.class.getName(), sel);
        volActivity.putExtra(Aeronef.class.getName(), currentAeronef);
        volActivity.putExtra(Site.class.getName(), currentSite);
        volActivity.putExtra(Vol.DATE, sFlightDate);
        volActivity.putExtra(Vol.MIN_VOL, sMinVol);
        volActivity.putExtra(Vol.MIN_MOTEUR, sMinMot);
        volActivity.putExtra(Vol.SEC_MOTEUR, sSecMot);
        volActivity.putExtra(Vol.NOTE, sNote);
        startActivity(volActivity);
    	finish();
	}
    

    
	@Override
	public void onClickToDelete(Accu item, int position) {
		Accu sel = accus.get(position);
		selectItim = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(sel.getNom());
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
			dbAccu.open();
            dbAccu.deleteAccu(accus.get(selectItim));
            dbAccu.close();
	        
	        accus.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}


    @Override
    public void onClickToUpdate(Accu item, int position) {
        Intent addAccuActivity = new Intent(getApplicationContext(), AddAccuActivity.class);
        addAccuActivity.putExtra(Accu.class.getName(), item);
        startActivity(addAccuActivity);
        finish();
    }


    @Override
    public void onBackPressed() {
		// Nothings
	}



}