package com.och.flightbook.activity;

import com.och.flightbook.R;
import com.och.flightbook.activity.MyDialogInterface.DialogReturn;
import com.och.flightbook.adapter.RadioAdapter;
import com.och.flightbook.listener.SwitchPotarAdapterListener;
import com.och.flightbook.model.Potar;
import com.och.flightbook.model.Radio;
import com.och.flightbook.model.Switch;
import com.och.flightbook.speech.TtsProviderFactory;
import com.och.flightbook.sqllite.DbRadio;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RadioActivity extends ListActivity implements DialogReturn, SwitchPotarAdapterListener{

	private Radio radio;
	private MyDialogInterface myInterface;
	private int itemId;
	private String typeItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }


        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        TtsProviderFactory ttsProviderImpl = TtsProviderFactory.getInstance();

        // Get radio to edit
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	int radioId = bundle.getInt(Radio.RADIO_ID);
           	radio = DbRadio.getRadioById(radioId);
        }
        
        // View title
        setTitle(radio.getName());
        String say = getString(R.string.radio) + " " + getString(R.string.st_for) + " " + radio.getName();
        if (ttsProviderImpl != null) {
            ttsProviderImpl.say(say);
        }
        
        // Creation et initialisation de l'Adapter pour les aeronefs
        RadioAdapter adapter = new RadioAdapter(this, radio);
        // Ecoute des evenements sur votre liste
        adapter.addListener(this);
        
        //Initialisation de la liste avec les donnees
        setListAdapter(adapter);

    }


	@Override
	public void onClickLayout(Object item, int position) {
		itemId = -1;
		String name = null;
        String action = null;
		typeItem = null;
		if (item instanceof Switch) {
			itemId = ((Switch) item).getId();
			name = ((Switch) item).getName();
            action = ((Switch) item).getAction();
			typeItem = "Inter";
		} else if (item instanceof Potar) {
			itemId = ((Potar) item).getId();
			name = ((Potar) item).getName();
            action = ((Potar) item).getAction();
			typeItem = "Potar";
		}
		
		if (itemId!=-1) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setCancelable(true);
	    	builder.setIcon(R.drawable.delete);
	    	builder.setTitle(typeItem + " - " + name + (action!=null?"\n"+action:""));
	    	builder.setInverseBackgroundForced(true);
	    	builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
	    	  @Override
	    	  public void onClick(DialogInterface dialog, int which) {
	    		myInterface.getListener().onDialogCompleted(true, null);
	    	    dialog.dismiss();
	    	  }
	    	});
	    	builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
	    	  @Override
	    	  public void onClick(DialogInterface dialog, int which) {
	    		myInterface.getListener().onDialogCompleted(false, null);
	    		dialog.dismiss();
	    	  }
	    	});
	    	AlertDialog alert = builder.create();
	    	alert.show();
		}		
	}

	@Override
	public void onDialogCompleted(boolean answer, String type) {
		if (answer && itemId!=-1) {
            if ("Inter".equals(typeItem)) {
                DbRadio.deleteSwitch(itemId);
            } else if ("Potar".equals(typeItem)) {
                DbRadio.deletePotar(itemId);
            }

	        Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
	        radioActivity.putExtra(Radio.RADIO_ID, radio.getId());
        	startActivity(radioActivity);
        	finish();
		}
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addclose, menu);
        MenuItem item = menu.findItem(R.id.close);
        item.setVisible(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent addSwitchPotarActivity = new Intent(getApplicationContext(), AddSwitchPotarActivity.class);
                addSwitchPotarActivity.putExtra(Radio.RADIO_ID, radio.getId());
                startActivity(addSwitchPotarActivity);
                finish();
                return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // Nothings
    }

}
