package com.flightbook.activity;

import java.io.File;
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
import com.flightbook.sqllite.DbBackup;
import com.flightbook.sqllite.DbImport;
import com.flightbook.tools.SimpleFileDialog;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.Toast;

public class HangarActivity extends ListActivity  implements DialogReturn, AeronefAdapterListener {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addshareimportclose, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent myIntent = new Intent(getApplicationContext(), AddAeronefActivity.class);
                startActivityForResult(myIntent, 0);
                finish();
                return true;
            case R.id.close:
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
                return true;
            case R.id.send:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                DbBackup dbBackup = new DbBackup(this);
                sendIntent.putExtra(Intent.EXTRA_TEXT, dbBackup.getStbAeronefs().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.importData:
                importHangar();
                return true;

        }
        return false;
    }

    private void importHangar() {
        SimpleFileDialog FileOpenDialog = new SimpleFileDialog(this, "FileOpen",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
                        File file = new File(chosenDir);
                        // Initiate the upload
                        DbImport dbImport = new DbImport(HangarActivity.this);
                        String result = dbImport.importAeronefs(file);
                        if (result!=null) {
                            Toast.makeText(HangarActivity.this, result, Toast.LENGTH_LONG).show();
                        } else {
                            // Update the list
                            refreshList();
                            Toast.makeText(HangarActivity.this, getString(R.string.menu_import_ok), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        FileOpenDialog.Default_File_Name = "";
        FileOpenDialog.chooseFile_or_Dir();
    }


    private void refreshList() {
        if (aeronefs!=null) {
            for (int i=aeronefs.size()-1; i>=0; i--) {
                aeronefs.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        dbAeronef.open();
        aeronefs.addAll(dbAeronef.getAeronefs());
        dbAeronef.close();
        adapter.notifyDataSetChanged();
    }

}
