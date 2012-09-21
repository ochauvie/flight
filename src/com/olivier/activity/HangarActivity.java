package com.olivier.activity;




import java.util.ArrayList;

import com.olivier.R;
import com.olivier.adapter.AeronefsAdapter;
import com.olivier.listener.AeronefAdapterListener;
import com.olivier.model.Aeronef;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;

public class HangarActivity extends ListActivity  implements AeronefAdapterListener {

	
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Aeronef> aeronefs;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangar);
        
        dbAeronef.open();
        aeronefs = dbAeronef.getAeronefs();
        dbAeronef.close();
        
        // Cr�ation et initialisation de l'Adapter pour les personnes
        AeronefsAdapter adapter = new AeronefsAdapter(this, aeronefs);
            
        // Ecoute des �v�nements sur votre liste
        adapter.addListener(this);
        
        // R�cup�ration du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les donn�es
        setListAdapter(adapter);
        
    }
    
    
    @Override
	public void onClickName(Aeronef item, int position) {
    	Aeronef sel = aeronefs.get(position);
    	Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
    	volActivity.putExtra("aeronef", sel.getName());
    	volActivity.putExtra("type", sel.getType());
    	startActivity(volActivity);
    	finish();
	}
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }









	

    
    
    
    
}
