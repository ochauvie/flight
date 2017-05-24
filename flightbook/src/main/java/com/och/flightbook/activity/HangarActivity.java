package com.och.flightbook.activity;

import java.io.File;
import java.util.ArrayList;

import com.och.flightbook.R;
import com.och.flightbook.activity.MyDialogInterface.DialogReturn;
import com.och.flightbook.adapter.AeronefsAdapter;
import com.och.flightbook.listener.AeronefAdapterListener;
import com.och.flightbook.model.Accu;
import com.och.flightbook.model.Aeronef;
import com.och.flightbook.model.Site;
import com.och.flightbook.model.Vol;
import com.och.flightbook.speech.TtsProviderFactory;
import com.och.flightbook.sqllite.DbAeronef;
import com.och.flightbook.sqllite.DbBackup;
import com.och.flightbook.sqllite.DbJsonImport;
import com.och.flightbook.tools.SimpleFileDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class HangarActivity extends ListActivity  implements DialogReturn, AeronefAdapterListener, View.OnClickListener  {

	private ListView listView;
    private TextView headerTYpe;
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
        
        aeronefs = DbAeronef.getAeronefs();

        listView = getListView();

        View header = findViewById( R.id.header_layout );
        header.setOnClickListener(this);
        headerTYpe = (TextView) header.findViewById(R.id.headerType);
        
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
	public void onClickUpdate(Aeronef item, int position) {
    	Aeronef sel = aeronefs.get(position);
    	Intent addAeronefActivity = new Intent(getApplicationContext(), AddAeronefActivity.class);
    	addAeronefActivity.putExtra(Aeronef.class.getName(), sel);
    	startActivity(addAeronefActivity);
    	finish();
	}

    @Override
    public void onClickType(Aeronef item, int position) {
        if (aeronefs!=null) {
            for (int i=aeronefs.size()-1; i>=0; i--) {
                aeronefs.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        aeronefs.addAll(DbAeronef.getAeronefByType(item.getType()));
        headerTYpe.setTextColor(Color.rgb(219, 23, 2));
        adapter.notifyDataSetChanged();
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
			DbAeronef.deleteAeronef(aeronefs.get(selectItim));

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
                        DbJsonImport dbJsonImport = new DbJsonImport(HangarActivity.this);
                        String result = dbJsonImport.importAeronefs(file);
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
        if (aeronefs!=null) {
            aeronefs.addAll(DbAeronef.getAeronefs());
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(HangarActivity.this, getString(R.string.import_reload_list), Toast.LENGTH_LONG).show();
        }
        headerTYpe.setTextColor(Color.BLACK);

    }

    @Override
    // Action sur le click du header: suppression du filtre sur la liste
    public void onClick(View v) {
        refreshList();
    }



}
