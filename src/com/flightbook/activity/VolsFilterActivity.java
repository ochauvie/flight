package com.flightbook.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.flightbook.R;
import com.flightbook.model.Accu;
import com.flightbook.model.TypeAccu;
import com.flightbook.sqllite.DbAccu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@TargetApi(14)
public class VolsFilterActivity extends Activity implements DatePickerDialog.OnDateSetListener {

	private DbAccu dbAccu = new DbAccu(this);
	private Accu accu = null;

	private EditText dateStart, dateEnd;
    private Spinner spinnerType;
    private ImageButton selectDateStart, selectDayeEbnd;
	private DatePickerDialog datePickerDialog = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_add_accu);

	        
            spinnerType = (Spinner) findViewById(R.id.spinnerType);
            loadSpinnerType();
            dateStart = (EditText) findViewById(R.id.editTextDateStart);
            dateStart.setEnabled(false);
            dateEnd = (EditText) findViewById(R.id.editTextDateEnd);
            dateEnd.setEnabled(false);


	        // Get filter in parameter
	        initView();
	        

            // Flight date
            selectDateStart = (ImageButton) findViewById(R.id.selectDateStart);
            selectDateStart.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String sDate = dateStart.getText().toString();
                    if (!"".equals(sDate)) {
                        String[] ssDate = sDate.split("/");
                        datePickerDialog = new DatePickerDialog(v.getContext(),
                                VolsFilterActivity.this,
                                Integer.parseInt(ssDate[2]),
                                Integer.parseInt(ssDate[1]) - 1,
                                Integer.parseInt(ssDate[0]));
                    } else {
                        datePickerDialog = new DatePickerDialog(v.getContext(),
                                VolsFilterActivity.this, 2015, 0, 1);
                    }
                    datePickerDialog.setTitle(getString(R.string.filter_start_date));
                    datePickerDialog.show();
                }
            });


        }
	    
	    private void initView() {
	    	Bundle bundle = getIntent().getExtras();
	        if (bundle!=null) {
	            /*
                accu = (Accu)bundle.getSerializable(Accu.class.getName());
                if (accu!=null) {
                    nom.setText(accu.getNom());
                    SpinnerTool.SelectSpinnerItemByValue(spinnerType, accu.getType().name());
                    nbElement.setText(String.valueOf(accu.getNbElements()));
                    capacite.setText(String.valueOf(accu.getCapacite()));
                    tauxDecharge.setText(String.valueOf(accu.getTauxDecharge()));
                    marque.setText(accu.getMarque());
                    numero.setText(String.valueOf(accu.getNumero()));
                    nbCycle.setText(String.valueOf(accu.getNbCycles()));
                    voltage.setText(String.valueOf(accu.getVoltage()));
                    if (accu.getDateAchat()!=null) {
                        String sDate = sdf.format(accu.getDateAchat());
                        dateAchat.setText(sDate);
                    }
                }
                */
	        }
	    }

	    RadioButton.OnClickListener myOptionOnClickListener =
	    	new RadioButton.OnClickListener() {
	    		  @Override
	    		  public void onClick(View v) {
	    		  }
	    };
	    

		@Override
	    public void onBackPressed() {
			// Nothings
		}

    private void loadSpinnerType() {
        List<String> list = new ArrayList<String>();
        for (TypeAccu typeAccu :TypeAccu.values()) {
            list.add(typeAccu.name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String day = String.valueOf(dayOfMonth);
        if (day.length()<2) {day = "0" + day;}
        String month = String.valueOf(monthOfYear+1);
        if (month.length()<2) {month = "0" + month;}
        String y = String.valueOf(year);
        if (y.length()<2) {y = "0" + y;}
        dateStart.setText(day + "/" + month + "/" + y);
        datePickerDialog.hide();
    }

}
