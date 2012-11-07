package com.olivier.activity;




import java.util.ArrayList;

import com.olivier.R;
import com.olivier.adapter.RadiosAdapter;
import com.olivier.listener.RadiosAdapterListener;
import com.olivier.model.Aeronef;
import com.olivier.model.Radio;
import com.olivier.sqllite.DbAeronef;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ListView;

public class RadiosActivity extends ListActivity implements RadiosAdapterListener  {

	private ListView listView;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Radio> radios;
	RadiosAdapter adapter;	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radios);
    
        
        dbAeronef.open();
        radios = dbAeronef.getRadios();
        dbAeronef.close();
        
        listView = getListView();
        
        // Création et initialisation de l'Adapter pour les aeronefs
        adapter = new RadiosAdapter(this, radios);
            
        // Ecoute des évènements sur votre liste
        adapter.addListener(this);
        
        // Récupération du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les données
        setListAdapter(adapter);
        
    }


	@Override
	public void onClickName(Radio item, int position) {
		Radio radio = radios.get(position);
    	Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
    	radioActivity.putExtra("radioId", radio.getId());
    	startActivity(radioActivity);
	}


	@Override
	public void onClickToDelete(Radio item, int position) {
		// TODO Auto-generated method stub
		
	}



    
    
}
