package com.flightbook.activity;

import java.util.ArrayList;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.AeronefsAdapter;
import com.flightbook.listener.AeronefAdapterListener;
import com.flightbook.model.Accu;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Site;
import com.flightbook.model.Vol;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ListView;

public class HangarActivity extends ListActivity  implements DialogReturn, AeronefAdapterListener {

	private ImageButton addAeronef;
	private ImageButton close;
	private ListView listView;
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Aeronef> aeronefs;
	private AeronefsAdapter adapter;
	private MyDialogInterface myInterface;
	private int selectItim = -1;
	private TtsProviderFactory ttsProviderImpl;

    private Site currentSite = null;
    private Accu currentAccu = null;
    private String sFlightDate = null;
    private String sMinVol = null;
    private String sMinMot = null;
    private String sSecMot = null;
    private String sNote = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangar);
        
        ttsProviderImpl = TtsProviderFactory.getInstance();
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbAeronef.open();
        aeronefs = dbAeronef.getAeronefs();
        dbAeronef.close();
        
        listView = getListView();
        
        // Creation et initialisation de l'Adapter pour les aeronefs
        adapter = new AeronefsAdapter(this, aeronefs);
            
        // Ecoute des evenements sur votre liste
        adapter.addListener(this);
        
        // Recuperation du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);
        
        // Animation
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
        				this, R.anim.list_layout_controller);
        listView.setLayoutAnimation(controller);
        
        
        // Open view add aeronef
        addAeronef = (ImageButton) findViewById(R.id.addAeronef);
        addAeronef.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), AddAeronefActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
        	}
        });
        
        // Close view aeronef selection
        close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
                volActivity.putExtra(Site.class.getName(), currentSite);
                volActivity.putExtra(Accu.class.getName(), currentAccu);
                volActivity.putExtra(Vol.DATE, sFlightDate);
                volActivity.putExtra(Vol.MIN_VOL, sMinVol);
                volActivity.putExtra(Vol.MIN_MOTEUR, sMinMot);
                volActivity.putExtra(Vol.SEC_MOTEUR, sSecMot);
                volActivity.putExtra(Vol.NOTE, sNote);
            	startActivity(volActivity);
            	finish();
        	}
        });
        
        ttsProviderImpl.say(getString(R.string.selectAeronef));

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            currentAccu = (Accu)bundle.getSerializable(Accu.class.getName());
            currentSite = (Site)bundle.getSerializable(Site.class.getName());
            sFlightDate = bundle.getString(Vol.DATE);
            sMinVol = bundle.getString(Vol.MIN_VOL);
            sMinMot = bundle.getString(Vol.MIN_MOTEUR);
            sSecMot = bundle.getString(Vol.SEC_MOTEUR);
            sNote = bundle.getString(Vol.NOTE);
        }
        
    }
    
    
    @Override
	public void onClickName(Aeronef item, int position) {
    	Aeronef sel = aeronefs.get(position);
    	Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
    	volActivity.putExtra(Aeronef.class.getName(), sel);
        volActivity.putExtra(Site.class.getName(), currentSite);
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
	public void onClickType(Aeronef item, int position) {
    	Aeronef sel = aeronefs.get(position);
    	Intent addAeronefActivity = new Intent(getApplicationContext(), AddAeronefActivity.class);
    	addAeronefActivity.putExtra(Aeronef.class.getName(), sel);
    	startActivity(addAeronefActivity);
    	finish();
	}
    
	@Override
	public void onClickNameToDelete(Aeronef item, int position) {
		Aeronef sel = aeronefs.get(position);
		selectItim = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(sel.getName() + "\n" + "(" + sel.getType() + ")");
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
			dbAeronef.open();
	        dbAeronef.deleteAeronef(aeronefs.get(selectItim));
	        dbAeronef.close();
	        
	        aeronefs.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}

    
	@Override
    public void onBackPressed() {
		// Nothings
	}

}
