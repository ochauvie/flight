package com.olivier.activity;



import com.olivier.R;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AeronefActivity extends ListActivity  {

	
	private String[] aeronefs = {
            "Alpina 4001", "Arcus", "Calmato", "Broussard", "Raptor",
            "Paramoteur"
};
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeronef);
        
      //Cr�ation d'un SimpleAdapter qui se chargera de mettre les items pr�sents dans notre list (listItem) dans la vue affichageitem    
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, aeronefs);
        
        
      //On attribue � notre listView l'adapter que l'on vient de cr�er
        setListAdapter(adapter);
        
    }

    
    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	String sel = aeronefs[position];
    	Intent volActivity = new Intent(getApplicationContext(),VolActivity.class);
    	volActivity.putExtra("aeronef", sel);
    	startActivity(volActivity);
    	finish();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }

    
    
    
    
}
