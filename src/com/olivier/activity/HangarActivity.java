package com.olivier.activity;

import java.util.ArrayList;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.AeronefsAdapter;
import com.olivier.listener.AeronefAdapterListener;
import com.olivier.model.Aeronef;
import com.olivier.speech.TtsProviderFactory;
import com.olivier.sqllite.DbAeronef;

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
        
        // Création et initialisation de l'Adapter pour les aeronefs
        adapter = new AeronefsAdapter(this, aeronefs);
            
        // Ecoute des évènements sur votre liste
        adapter.addListener(this);
        
        // Récupération du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les données
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
            	startActivity(volActivity);
            	finish();
        	}
        });
        
        ttsProviderImpl.say(getString(R.string.selectAeronef));
        
    }
    
    
    @Override
	public void onClickName(Aeronef item, int position) {
    	Aeronef sel = aeronefs.get(position);
    	Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
    	volActivity.putExtra(Aeronef.NAME, sel.getName());
    	volActivity.putExtra(Aeronef.TYPE, sel.getType());
    	startActivity(volActivity);
    	finish();
	}
    
    @Override
	public void onClickType(Aeronef item, int position) {
    	Aeronef sel = aeronefs.get(position);
    	Intent addAeronefActivity = new Intent(getApplicationContext(), AddAeronefActivity.class);
    	addAeronefActivity.putExtra(Aeronef.ID, sel.getId());
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
