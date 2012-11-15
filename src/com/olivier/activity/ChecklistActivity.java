package com.olivier.activity;

import java.util.ArrayList;

import com.olivier.R;
import com.olivier.adapter.ChecklistAdapter;
import com.olivier.model.Checklist;
import com.olivier.speech.TtsProviderFactory;
import com.olivier.sqllite.DbAeronef;

import android.app.ListActivity;
import android.os.Bundle;

public class ChecklistActivity extends ListActivity {

	private Checklist checklist;
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ChecklistAdapter adapter;
	private TtsProviderFactory ttsProviderImpl;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);
        
        ttsProviderImpl = TtsProviderFactory.getInstance();
        
        // Get checklist to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String checklistName = bundle.getString(Checklist.NAME);
        	dbAeronef.open();
        		ArrayList<Checklist> l = dbAeronef.getChecklists(checklistName);
        		if (l!=null) {
        			checklist = l.get(0);
        			ttsProviderImpl.say("Checklist pour " + checklistName);
        		}
            dbAeronef.close();
        }
        
        // View title
        setTitle(checklist.getName());
        
        // Création et initialisation de l'Adapter 
        adapter = new ChecklistAdapter(this, checklist.getItems());
        
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
    }


    
}
