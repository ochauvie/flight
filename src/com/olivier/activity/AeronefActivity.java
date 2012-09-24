package com.olivier.activity;




import com.olivier.R;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@Deprecated
public class AeronefActivity extends ListActivity  {

	
	private String[] aeronefsList = {
            "Alpina 4001", "Arcus", "Calmato", "Broussard", "Raptor",
            "Paramoteur"
		};
	
	//private DbAeronef dbAeronef = new DbAeronef(this);
	//private ArrayList<Aeronef> aeronefs;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeronef);
        
        /*
        dbAeronef.open();
        aeronefs = dbAeronef.getAeronefs();
        dbAeronef.close();
        */
        
      //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem    
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, aeronefsList);
        
        
      //On attribue à notre listView l'adapter que l'on vient de créer
        setListAdapter(adapter);
        
    }

    
    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	String sel = aeronefsList[position];
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
