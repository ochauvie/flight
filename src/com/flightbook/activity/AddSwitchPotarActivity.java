package com.flightbook.activity;


import com.flightbook.R;
import com.flightbook.model.Potar;
import com.flightbook.model.Radio;
import com.flightbook.model.Switch;
import com.flightbook.sqllite.DbRadio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddSwitchPotarActivity extends Activity {

	private int radioId;

	private EditText name, action, up, center, down;
	private RadioButton optSwitch, optPotar;

	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_switch_potar);

            name = (EditText) findViewById(R.id.editName);
            action = (EditText) findViewById(R.id.editAction);
            up = (EditText) findViewById(R.id.editUp);
            center = (EditText) findViewById(R.id.editCenter);
            down = (EditText) findViewById(R.id.editDown);

            optSwitch = (RadioButton) findViewById(R.id.option_switch);
            optPotar = (RadioButton) findViewById(R.id.option_potar);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                radioId = bundle.getInt(Radio.RADIO_ID);
            }

            // Hide keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
	    

	    RadioButton.OnClickListener myOptionOnClickListener =
	    	new RadioButton.OnClickListener() {
	    		  @Override
	    		  public void onClick(View v) {
	    		  }
	    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.saveclose, menu);
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                onSave();
                return true;
            case R.id.close:
                Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
                radioActivity.putExtra(Radio.RADIO_ID, radioId);
                startActivity(radioActivity);
                finish();
                return true;
        }
        return false;
    }

    private void onSave() {
        Editable edName = name.getText();
        if (edName==null || "".equals(edName.toString())) {
            Toast.makeText(getBaseContext(), getString(R.string.name_mandatory), Toast.LENGTH_LONG).show();
        } else if (!optSwitch.isChecked() && !optPotar.isChecked() ) {
            Toast.makeText(getBaseContext(), getString(R.string.inter_potar_mandatory), Toast.LENGTH_LONG).show();
        } else {

            if (optSwitch.isChecked()) {
                Switch sw = new Switch();
                sw.setName(name.getText().toString());
                sw.setAction(action.getText().toString());
                sw.setUp(up.getText().toString());
                sw.setCenter(center.getText().toString());
                sw.setDown(down.getText().toString());
                DbRadio.addSwitchToRadio(radioId, sw);

            } else if (optPotar.isChecked()) {
                Potar potar = new Potar();
                potar.setName(name.getText().toString());
                potar.setAction(action.getText().toString());
                potar.setUp(up.getText().toString());
                potar.setCenter(center.getText().toString());
                potar.setDown(down.getText().toString());
                DbRadio.addPotarToRadio(radioId, potar);
            }

            Toast.makeText(getBaseContext(), getString(R.string.save_ok), Toast.LENGTH_LONG).show();

            Intent radioActivity = new Intent(getApplicationContext(), RadioActivity.class);
            radioActivity.putExtra(Radio.RADIO_ID, radioId);
            startActivity(radioActivity);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // Nothings
    }

}
