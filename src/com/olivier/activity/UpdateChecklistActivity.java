package com.olivier.activity;

import java.util.ArrayList;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.UpdateChecklistAdapter;
import com.olivier.listener.UpdateChecklistAdapterListener;
import com.olivier.model.Checklist;
import com.olivier.model.ChecklistItem;
import com.olivier.sqllite.DbAeronef;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;

public class UpdateChecklistActivity extends ListActivity implements DialogReturn, UpdateChecklistAdapterListener{

	private Checklist checklist;
	private DbAeronef dbAeronef = new DbAeronef(this);
	private UpdateChecklistAdapter adapter;	
	private int selectItim = -1;
	private MyDialogInterface myInterface;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
    
        // Get checklist to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String checklistName = bundle.getString(Checklist.NAME);
        	dbAeronef.open();
        		ArrayList<Checklist> l = dbAeronef.getChecklists(checklistName);
        		if (l!=null) {
        			checklist = l.get(0);
        		}
            dbAeronef.close();
        }
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        // View title
        setTitle(checklist.getName());
        
        // Création et initialisation de l'Adapter 
        adapter = new UpdateChecklistAdapter(this, checklist.getItems());
        
        // Ecoute des évènements sur votre liste
        adapter.addListener(this);
        
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
    }

	@Override
	public void onClickToDelete(ChecklistItem item, int position) {
		selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(item.getAction());
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
			ChecklistItem item = checklist.getItems().get(selectItim);
	    	dbAeronef.open();
	        	dbAeronef.deleteChecklistItem(item.getId());
	        dbAeronef.close();
	        checklist.getItems().remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}

	


    
}
