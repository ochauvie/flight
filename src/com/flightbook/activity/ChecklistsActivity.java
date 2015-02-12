package com.flightbook.activity;



import java.util.ArrayList;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.ChecklistsAdapter;
import com.flightbook.listener.ChecklistsAdapterListener;
import com.flightbook.model.Checklist;
import com.flightbook.model.ChecklistItem;
import com.flightbook.sqllite.DbChecklist;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class ChecklistsActivity extends ListActivity implements DialogReturn, ChecklistsAdapterListener  {

	private ImageButton add;
	private EditText newCkeckListName;
	
	private MyDialogInterface myInterface;
	
	private DbChecklist dbChecklist = new DbChecklist(this);
	private ArrayList<Checklist> checklists;
	private int selectItim = -1;
	private ChecklistsAdapter adapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklists);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ListView listView = getListView();
        newCkeckListName =  (EditText)  findViewById(R.id.editTextNewChecklist);
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbChecklist.open();
        	checklists = dbChecklist.getChecklists(null);
        dbChecklist.close();
        	
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
        			dbChecklist.open();
        				dbChecklist.addCheckList(cl);
        			dbChecklist.close();
        			
        			Intent checklistsActivity = new Intent(getApplicationContext(), ChecklistsActivity.class);
                	startActivity(checklistsActivity);
                	finish();
        		}
        	}
        });
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
			dbChecklist.open();
				dbChecklist.deleteChecklist(checklist);
			dbChecklist.close();
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


}
