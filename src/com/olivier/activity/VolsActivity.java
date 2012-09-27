package com.olivier.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.olivier.R;
import com.olivier.activity.MyDialogInterface.DialogReturn;
import com.olivier.adapter.VolsAdapter;
import com.olivier.listener.VolsAdapterListener;
import com.olivier.model.Vol;
import com.olivier.sqllite.DbAeronef;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;

public class VolsActivity extends ListActivity implements DialogReturn, VolsAdapterListener  {

	MyDialogInterface myInterface;
	MyDialogInterface.DialogReturn dialogReturn;
	
	private DbAeronef dbAeronef = new DbAeronef(this);
	private ArrayList<Vol> vols;
	int selectItim = -1;
	VolsAdapter adapter;
	TextView totalVol;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vols);
        
        View header = getLayoutInflater().inflate(R.layout.activity_header_vols, null);
        View footer = getLayoutInflater().inflate(R.layout.activity_footer_vols, null);
        ListView listView = getListView();
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        totalVol = (TextView) footer.findViewById(R.id.totalVol);
        
        myInterface = new MyDialogInterface();
        myInterface.setListener(this);
        
        dbAeronef.open();
        vols = dbAeronef.getVols();
        dbAeronef.close();
        
        totalVol.setText(getTotalVol());
        	
    	// Création et initialisation de l'Adapter pour les personnes
        adapter = new VolsAdapter(this, vols);
            
        // Ecoute des évènements sur votre liste
        adapter.addListener(this);
        
        // Récupération du composant ListView
        //ListView list = (ListView)findViewById(R.id.ListViewHangar);
            
        //Initialisation de la liste avec les données
        setListAdapter(adapter);

        
        // Animation
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
        				this, R.anim.list_layout_controller);
        listView.setLayoutAnimation(controller);
    }

    private String getTotalVol() {
    	int total = 0;
    	if (vols!=null) {
	    	for (Vol vol:vols) {
	        	total = total + vol.getMinutesVol();
	        }
    	}
   		int heu = total/60;
   		int min = total % 60;
   		String sMin = String.valueOf(min);
   		if (min<10) {
   			sMin = "0" + sMin;
   		}
   		return String.valueOf(heu + "h" + sMin);
    }
    
	@Override
	public void onClickName(Vol vol, int position) {
		selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.note);
    	builder.setTitle(vol.getAeronef());
    	builder.setMessage("     Note: \n\n" + vol.getNote() + "\n\n" + "     Lieu: \n\n" + vol.getLieu());
    	builder.setInverseBackgroundForced(true);
    	builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	
	@Override
	public void onClickDelete(Vol vol, int position) {
		selectItim = position;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setCancelable(true);
    	builder.setIcon(R.drawable.delete);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.FRANCE);
		  String sDate = sdf.format(vol.getDateVol());
    	builder.setTitle(sDate + " "
    			+ vol.getAeronef() + "\n" 
    			+ vol.getMinutesVol() + " min"
    			+ " (" + vol.getMinutesMoteur() + ":" + vol.getSecondsMoteur() + ")");
    	builder.setInverseBackgroundForced(true);
    	builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(true);
    	    dialog.dismiss();
    	  }
    	});
    	builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
    	  @Override
    	  public void onClick(DialogInterface dialog, int which) {
    		myInterface.getListener().onDialogCompleted(false);
    		dialog.dismiss();
    	  }
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	@Override
	public void onDialogCompleted(boolean answer) {
		if (answer && selectItim!=-1) {
			Vol flight = vols.get(selectItim);
	    	dbAeronef.open();
	        dbAeronef.deleteVol(flight);
	        dbAeronef.close();
	        vols.remove(selectItim);
	        adapter.notifyDataSetChanged();
	        totalVol.setText(getTotalVol());
		}
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_olivier, menu);
        return true;
    }

	

}
