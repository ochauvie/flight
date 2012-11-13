package com.olivier.activity;



import java.util.ArrayList;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.ChecklistsAdapter;
import com.olivier.listener.ChecklistsAdapterListener;
import com.olivier.model.Checklist;
import com.olivier.model.ChecklistItem;
import com.olivier.model.Radio;
import com.olivier.sqllite.DbAeronef;

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
	
	MyDialogInterface myInterface;
	MyDialogInterface.DialogReturn dialogReturn;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Checklist> checklists;
	int selectItim = -1;
	ChecklistsAdapter adapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklists);
        
        ListView listView = getListView();
        newCkeckListName =  (EditText)  findViewById(R.id.editTextNewChecklist);
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbAeronef.open();
        	checklists = dbAeronef.getChecklists(null);
        dbAeronef.close();
        	
    	// Création et initialisation de l'Adapter pour les personnes
        adapter = new ChecklistsAdapter(this, checklists);
            
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
        
        // Open view add 
        add = (ImageButton) findViewById(R.id.butNew);
        add.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		String newName = newCkeckListName.getText().toString();
        		if (newName!=null && !"".equals(newName)) {
        			Checklist cl = new Checklist(newName);
        			ChecklistItem item = new ChecklistItem("Emetteur ON", 1);
        			cl.addItem(item);
        			dbAeronef.open();
        				dbAeronef.addCheckList(cl);
        			dbAeronef.close();
        			
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
			Checklist checklist = checklists.get(selectItim);
	    	dbAeronef.open();
	        	dbAeronef.deleteChecklist(checklist);
	        dbAeronef.close();
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


}
