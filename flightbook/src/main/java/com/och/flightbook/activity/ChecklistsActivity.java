package com.och.flightbook.activity;



import java.io.File;
import java.util.ArrayList;

import com.och.flightbook.R;
import com.och.flightbook.activity.MyDialogInterface.DialogReturn;
import com.och.flightbook.adapter.ChecklistsAdapter;
import com.och.flightbook.listener.ChecklistsAdapterListener;
import com.och.flightbook.model.Checklist;
import com.och.flightbook.model.ChecklistItem;
import com.och.flightbook.sqllite.DbChecklist;
import com.och.flightbook.sqllite.DbJsonImport;
import com.och.flightbook.tools.SimpleFileDialog;

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
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ChecklistsActivity extends ListActivity implements DialogReturn, ChecklistsAdapterListener  {

	private ImageButton add;
	private EditText newCkeckListName;
	
	private MyDialogInterface myInterface;
	
	private ArrayList<Checklist> checklists;
	private int selectItim = -1;
	private ChecklistsAdapter adapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklists);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        ListView listView = getListView();
        newCkeckListName =  (EditText)  findViewById(R.id.editTextNewChecklist);
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
       	checklists = DbChecklist.getChecklists(null);

    	// Creation et initialisation de l'Adapter pour les personnes
        adapter = new ChecklistsAdapter(this, checklists);
            
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
        
        // Open view add 
        add = (ImageButton) findViewById(R.id.butNew);
        add.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String newName = newCkeckListName.getText().toString();
        		if (newName!=null && !"".equals(newName)) {
        			Checklist cl = new Checklist(newName);
        			ChecklistItem item = new ChecklistItem(getString(R.string.checklistDefaultItem), 1);
        			cl.addItem(item);
       				DbChecklist.addCheckList(cl);

        			Intent checklistsActivity = new Intent(getApplicationContext(), ChecklistsActivity.class);
                	startActivity(checklistsActivity);
                	finish();
        		}
        	}
        });

        // Hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    
	@Override
	public void onClickName(Checklist checklist, int position) {
		Intent checklistActivity = new Intent(getApplicationContext(), ChecklistActivity.class);
		checklistActivity.putExtra(Checklist.NAME, checklist.getName());
    	startActivity(checklistActivity);
	}
	
	
	@Override
	public void onClickToDelete(Checklist checklist, int position) {
		selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(checklist.getName());
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
			Checklist checklist = checklists.get(selectItim);
			DbChecklist.deleteChecklist(checklist);
			checklists.remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}


	@Override
	public void onClickToUpdate(Checklist item, int position) {
		Intent updateChecklistActivity = new Intent(getApplicationContext(), UpdateChecklistActivity.class);
		updateChecklistActivity.putExtra(Checklist.NAME, item.getName());
    	startActivity(updateChecklistActivity);
		
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
                importChecklists();
                return true;
        }
        return false;
    }


    /**
     * Import radios from file
     */
    private void importChecklists() {
        SimpleFileDialog FileOpenDialog = new SimpleFileDialog(this, "FileOpen",
                new SimpleFileDialog.SimpleFileDialogListener() {
                    @Override public void onChosenDir(String chosenDir) {
                        // The code in this function will be executed when the dialog OK button is pushed
                        File file = new File(chosenDir);
                        // Initiate the upload
                        DbJsonImport dbJsonImport = new DbJsonImport(ChecklistsActivity.this);
                        String result = dbJsonImport.importChecklists(file);
                        if (result!=null) {
                            Toast.makeText(ChecklistsActivity.this, result, Toast.LENGTH_LONG).show();
                        } else {
                            // Update the list
                            reloadAllChecklist();
                            Toast.makeText(ChecklistsActivity.this, getString(R.string.menu_import_ok), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        FileOpenDialog.Default_File_Name = "";
        FileOpenDialog.chooseFile_or_Dir();
    }

    private void reloadAllChecklist() {
        if (checklists!=null) {
            for (int i=checklists.size()-1; i>=0; i--) {
                checklists.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        if (checklists!=null) {
            checklists.addAll(DbChecklist.getChecklists(null));
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(ChecklistsActivity.this, getString(R.string.import_reload_list), Toast.LENGTH_LONG).show();
        }


    }

}
