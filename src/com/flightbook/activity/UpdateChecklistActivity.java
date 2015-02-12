package com.flightbook.activity;

import java.util.ArrayList;

import com.flightbook.R;
import com.flightbook.activity.MyDialogInterface.DialogReturn;
import com.flightbook.adapter.UpdateChecklistAdapter;
import com.flightbook.listener.UpdateChecklistAdapterListener;
import com.flightbook.model.Checklist;
import com.flightbook.model.ChecklistItem;
import com.flightbook.sqllite.DbChecklist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class UpdateChecklistActivity extends ListActivity implements DialogReturn, UpdateChecklistAdapterListener{

	private Checklist checklist;
	private DbChecklist dbChecklist = new DbChecklist(this);
	private UpdateChecklistAdapter adapter;	
	private int selectItim = -1;
	private MyDialogInterface myInterface;
	private EditText itemNewOrder, itemNewAction;
	private ImageButton butNew, butSave;
	private Context ctx;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist_update);
    
        ctx = this;
        
        // Get checklist to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String checklistName = bundle.getString(Checklist.NAME);
        	dbChecklist.open();
        		ArrayList<Checklist> l = dbChecklist.getChecklists(checklistName);
        		if (l!=null) {
        			checklist = l.get(0);
        		}
        		dbChecklist.close();
        }
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        // View title
        setTitle(checklist.getName());
        
        // Creation et initialisation de l'Adapter
        adapter = new UpdateChecklistAdapter(this, checklist.getItems());
        
        // Ecoute des evenements sur votre liste
        adapter.addListener(this);
        
        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);
        
        itemNewOrder = (EditText)  findViewById(R.id.itemNewOrder);
        itemNewAction = (EditText)  findViewById(R.id.itemNewAction);
        initPage();
        
        butNew = (ImageButton)  findViewById(R.id.butNew);
        butNew.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Editable edItemNewOrder = itemNewOrder.getText();
        		Editable edItemNewAction = itemNewAction.getText();
        		if (edItemNewOrder==null || "".equals(edItemNewOrder.toString())) {
        			Toast.makeText(ctx, R.string.checklist_order_mandatory, Toast.LENGTH_LONG ).show();
        		} else if (edItemNewAction==null || "".equals(edItemNewAction.toString())) {
        			Toast.makeText(ctx, R.string.checklist_action_mandatory, Toast.LENGTH_LONG ).show();
        		} else {
        			Checklist cp = new Checklist(checklist.getName());
        			ChecklistItem item = new ChecklistItem(edItemNewAction.toString(), Integer.valueOf(edItemNewOrder.toString()));
        			cp.addItem(item);
        			dbChecklist.open();
        				dbChecklist.addCheckList(cp);
    	        	dbChecklist.close();
    	        	Intent updateChecklistActivity = new Intent(getApplicationContext(), UpdateChecklistActivity.class);
    	        	updateChecklistActivity.putExtra(Checklist.NAME, checklist.getName());
	            	startActivity(updateChecklistActivity);
	            	finish();
        		}
        	}
        });
        
        butSave = (ImageButton)  findViewById(R.id.butSave);
        butSave.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		dbChecklist.open();
        			dbChecklist.updateChecklist(checklist);
        		dbChecklist.close();
        		Toast.makeText(ctx, R.string.checklist_update_ok, Toast.LENGTH_LONG ).show();
        	}
        });
        
    }
    
    private void initPage() {
    	itemNewOrder.setText("");
        itemNewAction.setText("");	
    }
    

	@Override
	public void onClickToDelete(ChecklistItem item, int position) {
		selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	builder.setTitle(item.getOrder() + " - " +  item.getAction());
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
			ChecklistItem item = checklist.getItems().get(selectItim);
			dbChecklist.open();
				dbChecklist.deleteChecklistItem(item.getId());
			dbChecklist.close();
	        checklist.getItems().remove(selectItim);
	        adapter.notifyDataSetChanged();
		}
	}
    
}
