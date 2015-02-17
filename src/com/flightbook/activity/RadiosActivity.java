package com.flightbook.activity;

import java.io.File;
import java.util.ArrayList;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.RadiosAdapter;
import com.flightbook.listener.RadiosAdapterListener;
import com.flightbook.model.Radio;
import com.flightbook.sqllite.DbJsonImport;
import com.flightbook.sqllite.DbRadio;
import com.flightbook.tools.SimpleFileDialog;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RadiosActivity extends ListActivity implements DialogReturn, RadiosAdapterListener  {

	private ImageButton add;
	private EditText newRadioName;
	private DbRadio dbRadio = new DbRadio(this);
	private ArrayList<Radio> radios;
	private RadiosAdapter adapter;
	private MyDialogInterface myInterface;
	private int selectItim = -1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radios);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dbRadio.open();
        	radios = dbRadio.getRadios();
        dbRadio.close();

        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        // Creation et initialisation de l'Adapter pour les aeronefs
        adapter = new RadiosAdapter(this, radios);
        adapter.addListener(this);

        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);
        
        newRadioName =  (EditText)  findViewById(R.id.editTextNewRadio);
        
        // Open view add 
        add = (ImageButton) findViewById(R.id.butNew);
        add.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String newName = newRadioName.getText().toString();
        		if (newName!=null && !"".equals(newName)) {
        			Radio r = new Radio();
        			r.setName(newName);
        			dbRadio.open();
        				dbRadio.addRadio(r);
        			dbRadio.close();
        			
        			/* On ne connait pas le nouveau id
        			radios.add(r);
        			adapter.notifyDataSetChanged();
        			*/
        			Intent radiosActivity = new Intent(getApplicationContext(), RadiosActivity.class);
                	startActivity(radiosActivity);
                	finish();
        		}
        	}
        });

        // Hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


	@Override
	public void onClickName(Radio item, int position) {
		Radio radio = radios.get(position);
    	Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
    	radioActivity.putExtra(Radio.RADIO_ID, radio.getId());
    	startActivity(radioActivity);
	}

	@Override
	public void onClickToDelete(Radio item, int position) {
		Radio sel = radios.get(position);
		selectItim = position;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(sel.getName() + "\n" );
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
			dbRadio.open();
				dbRadio.deleteRadio(radios.get(selectItim));
			dbRadio.close();
	        radios.remove(selectItim);
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
        MenuItem item = menu.findItem(R.id.send);
        item.setVisible(false);
        MenuItem item1 = menu.findItem(R.id.add);
        item1.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.close);
        item2.setVisible(false);
        return true;
    }


    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.importData:
                importRadios();
                return true;
        }
        return false;
    }


    /**
     * Import radios from file
     */
    private void importRadios() {
        SimpleFileDialog FileOpenDialog = new SimpleFileDialog(this, "FileOpen",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
                        File file = new File(chosenDir);
                        // Initiate the upload
                        DbJsonImport dbJsonImport = new DbJsonImport(RadiosActivity.this);
                        String result = dbJsonImport.importRadios(file);
                        if (result!=null) {
                            Toast.makeText(RadiosActivity.this, result, Toast.LENGTH_LONG).show();
                        } else {
                            // Update the list
                            reloadAllRadio();
                            Toast.makeText(RadiosActivity.this, getString(R.string.menu_import_ok), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        FileOpenDialog.Default_File_Name = "";
        FileOpenDialog.chooseFile_or_Dir();
    }

    private void reloadAllRadio() {
        if (radios!=null) {
            for (int i=radios.size()-1; i>=0; i--) {
                radios.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        if (radios!=null) {
            dbRadio.open();
            radios.addAll(dbRadio.getRadios());
            dbRadio.close();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(RadiosActivity.this, getString(R.string.import_reload_list), Toast.LENGTH_LONG).show();
        }


    }
}
