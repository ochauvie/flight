package com.olivier.activity;

import com.olivier.R;
import com.olivier.adapter.RadioAdapter;
import com.olivier.model.Radio;
import com.olivier.sqllite.DbAeronef;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

public class RadioActivity extends ListActivity {

	private ListView listView;
	private Radio radio;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private RadioAdapter adapter;	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        // Get radio to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	int radioId = bundle.getInt("radioId");
        	dbAeronef.open();
            radio = dbAeronef.getRadioById(radioId);
            dbAeronef.close();
        }
        
        // View title
        setTitle(radio.getName());
        
        listView = getListView();
        
        // Création et initialisation de l'Adapter pour les aeronefs
        adapter = new RadioAdapter(this, radio);
            
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
    }


    
    
}
