package com.olivier.activity;

import java.util.ArrayList;

import com.olivier.R;
import com.olivier.adapter.ChecklistAdapter;
import com.olivier.model.Checklist;
import com.olivier.speech.TtsProviderFactory;
import com.olivier.sqllite.DbChecklist;

import android.app.ListActivity;
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
        
        // Création et initialisation de l'Adapter 
        adapter = new ChecklistAdapter(this, checklist.getItems());
        
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
    }


    
}
