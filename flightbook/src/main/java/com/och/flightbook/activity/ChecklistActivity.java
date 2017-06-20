package com.och.flightbook.activity;

import java.util.ArrayList;

import com.och.flightbook.R;
import com.och.flightbook.adapter.ChecklistAdapter;
import com.och.flightbook.model.Checklist;
import com.och.flightbook.speech.TtsProviderFactory;
import com.och.flightbook.sqllite.DbChecklist;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Build;
import android.os.Bundle;

public class ChecklistActivity extends ListActivity {

	private Checklist checklist;
	private ChecklistAdapter adapter;
	private TtsProviderFactory ttsProviderImpl;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        if (ttsProviderImpl != null) {
            ttsProviderImpl = TtsProviderFactory.getInstance();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        // Get checklist to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	String checklistName = bundle.getString(Checklist.NAME);
        		ArrayList<Checklist> l = DbChecklist.getChecklists(checklistName);
        		if (l!=null) {
        			checklist = l.get(0);
        			String say = getString(R.string.checklist) + " " + getString(R.string.st_for) + " " + checklistName;
                    if (ttsProviderImpl != null) {
                        ttsProviderImpl.say(say);
                    }
        		}
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
