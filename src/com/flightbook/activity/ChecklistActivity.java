package com.flightbook.activity;

import java.util.ArrayList;

import com.flightbook.R;
import com.flightbook.adapter.ChecklistAdapter;
import com.flightbook.model.Checklist;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbChecklist;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;

public class ChecklistActivity extends ListActivity {

	private Checklist checklist;
	private DbChecklist dbChecklist = new DbChecklist(this);
	private ChecklistAdapter adapter;
	private TtsProviderFactory ttsProviderImpl;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        
        ttsProviderImpl = TtsProviderFactory.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        // Get checklist to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String checklistName = bundle.getString(Checklist.NAME);
        	dbChecklist.open();
        		ArrayList<Checklist> l = dbChecklist.getChecklists(checklistName);
        		if (l!=null) {
        			checklist = l.get(0);
        			String say = getString(R.string.checklist) + " " + getString(R.string.st_for) + " " + checklistName;  
        			ttsProviderImpl.say(say);
        		}
        		dbChecklist.close();
        }
        
        // View title
        setTitle(checklist.getName());
        
        // Creation et initialisation de l'Adapter
        adapter = new ChecklistAdapter(this, checklist.getItems());
        
        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);
        
    }

    @Override
    public void onBackPressed() {
        // Nothings
    }
    
}
