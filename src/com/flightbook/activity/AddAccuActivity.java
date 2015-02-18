package com.flightbook.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.flightbook.R;
import com.flightbook.model.Accu;
import com.flightbook.model.TypeAccu;
import com.flightbook.sqllite.DbAccu;
import com.flightbook.tools.SpinnerTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@TargetApi(14)
public class AddAccuActivity extends Activity implements DatePickerDialog.OnDateSetListener {

    private DbAccu dbAccu = new DbAccu(this);
    private Accu accu = null;

    private EditText nom, nbElement, capacite, tauxDecharge, marque, numero, dateAchat, nbCycle, voltage;
    private Spinner spinnerType;
    private ImageButton selectDate;
    private DatePickerDialog datePickerDialog = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accu);

        nom = (EditText) findViewById(R.id.editTextName);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        loadSpinnerType();
        nbElement = (EditText) findViewById(R.id.editTextNbElement);
        capacite = (EditText) findViewById(R.id.editTextCapacite);
        tauxDecharge = (EditText) findViewById(R.id.editTextTxDecharge);
        marque = (EditText) findViewById(R.id.editTextMarque);
        numero = (EditText) findViewById(R.id.editTextNumero);
        dateAchat = (EditText) findViewById(R.id.editTextDateAchat);
        dateAchat.setEnabled(false);
        nbCycle = (EditText) findViewById(R.id.editTextNbCycles);
        voltage = (EditText) findViewById(R.id.editTextVoltage);

        // Get accu in parameter
        initView();

        // Flight date
        selectDate = (ImageButton) findViewById(R.id.selectDate);
        selectDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sDate = dateAchat.getText().toString();
                if (!"".equals(sDate)) {
                    String[] ssDate = sDate.split("/");
                    datePickerDialog = new DatePickerDialog(v.getContext(),
                            AddAccuActivity.this,
                            Integer.parseInt(ssDate[2]),
                            Integer.parseInt(ssDate[1]) - 1,
                            Integer.parseInt(ssDate[0]));
                } else {
                    datePickerDialog = new DatePickerDialog(v.getContext(),
                            AddAccuActivity.this, 2015, 0, 1);
                }
                datePickerDialog.show();
            }
        });

        // Hide keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            accu = (Accu) bundle.getSerializable(Accu.class.getName());
            if (accu != null) {
                nom.setText(accu.getNom());
                SpinnerTool.SelectSpinnerItemByValue(spinnerType, accu.getType().name());
                nbElement.setText(String.valueOf(accu.getNbElements()));
                capacite.setText(String.valueOf(accu.getCapacite()));
                tauxDecharge.setText(String.valueOf(accu.getTauxDecharge()));
                marque.setText(accu.getMarque());
                numero.setText(String.valueOf(accu.getNumero()));
                nbCycle.setText(String.valueOf(accu.getNbCycles()));
                voltage.setText(String.valueOf(accu.getVoltage()));
                if (accu.getDateAchat() != null) {
                    String sDate = sdf.format(accu.getDateAchat());
                    dateAchat.setText(sDate);
                }
            }

        } else {
            nbCycle.setText("0");
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
        for (TypeAccu typeAccu : TypeAccu.values()) {
            list.add(typeAccu.name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapter);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String day = String.valueOf(dayOfMonth);
        if (day.length() < 2) {
            day = "0" + day;
        }
        String month = String.valueOf(monthOfYear + 1);
        if (month.length() < 2) {
            month = "0" + month;
        }
        String y = String.valueOf(year);
        if (y.length() < 2) {
            y = "0" + y;
        }
        dateAchat.setText(day + "/" + month + "/" + y);
        datePickerDialog.hide();
    }

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
                Intent accusActivity = new Intent(getApplicationContext(), AccusActivity.class);
                // TODO passer les extra
                startActivity(accusActivity);
                finish();
                return true;
        }
        return false;
    }

    private void onSave() {
        Editable edName = nom.getText();
        if (edName == null || "".equals(edName.toString())) {
            Toast.makeText(getBaseContext(), getString(R.string.name_mandatory), Toast.LENGTH_LONG).show();
        } else {
            try {
                if (accu == null) {
                    accu = new Accu();
                }
                accu.setNom(edName.toString());
                accu.setType(TypeAccu.valueOf((String) spinnerType.getSelectedItem()));
                accu.setNbElements(Integer.valueOf(nbElement.getText().toString()));
                accu.setCapacite(Integer.valueOf(capacite.getText().toString()));
                accu.setTauxDecharge(Integer.valueOf(tauxDecharge.getText().toString()));
                accu.setMarque((marque.getText() != null) ? marque.getText().toString() : null);
                accu.setNumero(Integer.valueOf(numero.getText().toString()));
                accu.setNbCycles(Integer.valueOf(nbCycle.getText().toString()));
                accu.setVoltage(Float.valueOf(voltage.getText().toString()));
                try {
                    accu.setDateAchat(sdf.parse(dateAchat.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dbAccu.open();
                if (accu.getId() != 0) {
                    dbAccu.updateAccu(accu);
                } else {
                    dbAccu.insertAccu(accu);
                }
                dbAccu.close();

                Toast.makeText(getBaseContext(), getString(R.string.accu_save), Toast.LENGTH_LONG).show();

                Intent accusActivity = new Intent(getApplicationContext(), AccusActivity.class);
                startActivity(accusActivity);
                // TODO  passer les extra
                finish();
            } catch (NumberFormatException nfe) {
                Toast.makeText(getBaseContext(), getString(R.string.number_format_ko), Toast.LENGTH_LONG).show();
            }
        }
    }
}