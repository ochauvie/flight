package com.flightbook.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.flightbook.R;
import com.flightbook.activity.listener.DateSetListener;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Site;
import com.flightbook.model.TypeAeronef;
import com.flightbook.model.VolsFilter;
import com.flightbook.speech.TtsProviderFactory;
import com.flightbook.sqllite.DbAeronef;
import com.flightbook.sqllite.DbSite;
import com.flightbook.tools.SpinnerTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FilterActivity extends Activity implements AdapterView.OnItemSelectedListener{

    public static final String EMPTY_CHOISE = " ";
    public static final int FILTER_REQUEST_CODE = 555;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    private TtsProviderFactory ttsProviderImpl;
    private Spinner spinnerType, spinnerAeronef, spinnerSite;
    private EditText editTextStartDate, editTextEndDate;
    private ImageButton btDateStart, btDateEnd, btClearStartDate, btClearEndDate;
    private DatePickerDialog startDatePickerDialog, endDatePickerDialog;

    private VolsFilter currentVolsFilter = new VolsFilter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ttsProviderImpl = TtsProviderFactory.getInstance();
        ttsProviderImpl.say(getString(R.string.menu_filter));

        spinnerType = (Spinner) findViewById(R.id.spType);
        loadSpinnerType();
        spinnerType.setOnItemSelectedListener(this);

        spinnerAeronef = (Spinner) findViewById(R.id.spAeronef);
        loadSpinnerAeronef();

        spinnerSite = (Spinner) findViewById(R.id.spSite);
        loadSpinnerSite();

        editTextStartDate = (EditText)  findViewById(R.id.editTextStartDate);
        btDateStart = (ImageButton) findViewById(R.id.imageButtonStartDate);
        btDateStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sDate = editTextStartDate.getText().toString();
                if (sDate==null || "".equals(sDate)) {
                    sDate = sdf.format(new Date());
                }
                String[] ssDate = sDate.split("/");
                startDatePickerDialog = new DatePickerDialog(v.getContext(),
                        new DateSetListener(editTextStartDate),
                        Integer.parseInt(ssDate[2]),
                        Integer.parseInt(ssDate[1]) - 1,
                        Integer.parseInt(ssDate[0]));
                startDatePickerDialog.show();
            }
        });

        editTextEndDate = (EditText)  findViewById(R.id.editTextEndDate);
        btDateEnd = (ImageButton) findViewById(R.id.imageButtonEndDate);
        btDateEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String sDate = editTextEndDate.getText().toString();
                if (sDate==null || "".equals(sDate)) {
                    sDate = sdf.format(new Date());
                }
                String[] ssDate = sDate.split("/");
                endDatePickerDialog = new DatePickerDialog(v.getContext(),
                        new DateSetListener(editTextEndDate),
                        Integer.parseInt(ssDate[2]),
                        Integer.parseInt(ssDate[1]) - 1,
                        Integer.parseInt(ssDate[0]));
                endDatePickerDialog.show();
            }
        });


        btClearStartDate = (ImageButton) findViewById(R.id.imageButtonClearStartDate);
        btClearStartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editTextStartDate.setText("");
            }
        });

        btClearEndDate = (ImageButton) findViewById(R.id.imageButtonClearEndDate);
        btClearEndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editTextEndDate.setText("");
            }
        });

        initPage();

    }


    private void loadSpinnerType() {
        List<TypeAeronef> list = new ArrayList<TypeAeronef>();
        for (TypeAeronef typeAeronef : TypeAeronef.values()) {
            list.add(typeAeronef);
        }
        ArrayAdapter<TypeAeronef> dataAdapter = new ArrayAdapter<TypeAeronef>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapter);
    }

    private void loadSpinnerAeronef() {
        String param = null;
        if (!((TypeAeronef)spinnerType.getSelectedItem()).name().equals(TypeAeronef.ALL.name())) {
            param = ((TypeAeronef) spinnerType.getSelectedItem()).name();
        }
        ArrayList<Aeronef> list = new ArrayList<Aeronef>();
        Aeronef emptyAeronef = new Aeronef();
        emptyAeronef.setName(EMPTY_CHOISE);
        list.add(emptyAeronef);
        for (Aeronef aeronef:DbAeronef.getAeronefByType(param)) {
            list.add(aeronef);
        }
        ArrayAdapter<Aeronef> dataAdapter = new ArrayAdapter<Aeronef>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAeronef.setAdapter(dataAdapter);
        SpinnerTool.SelectSpinnerItemByValue(spinnerAeronef, currentVolsFilter.getAeronef());
    }

    private void loadSpinnerSite() {
        ArrayList<Site> list = new ArrayList<Site>();
        Site emptySite = new Site();
        emptySite.setName(EMPTY_CHOISE);
        list.add(emptySite);
        for (Site site:DbSite.getSites()) {
            list.add(site);
        }
        ArrayAdapter<Site> dataAdapter = new ArrayAdapter<Site>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSite.setAdapter(dataAdapter);
    }


    @Override
    // Spinner TypeAeronef
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loadSpinnerAeronef();
    }

    @Override
    // Spinner TypeAeronef
    public void onNothingSelected(AdapterView<?> parent) {
        loadSpinnerAeronef();
    }

    private void initPage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentVolsFilter = (VolsFilter) bundle.getSerializable(VolsFilter.class.getName());
            if (currentVolsFilter != null) {
                SpinnerTool.SelectSpinnerItemByValue(spinnerSite, currentVolsFilter.getSite());
                SpinnerTool.SelectSpinnerItemByValue(spinnerType, currentVolsFilter.getTypeAeronef());
                SpinnerTool.SelectSpinnerItemByValue(spinnerAeronef, currentVolsFilter.getAeronef());
                try {
                    editTextStartDate.setText(sdf.format(currentVolsFilter.getDateDebut()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    editTextEndDate.setText(sdf.format(currentVolsFilter.getDateFin()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.saveclose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                onSave();
                return true;
            case R.id.close:
                finish();
                return true;
        }
        return false;
    }


    private void onSave() {
        currentVolsFilter.setTypeAeronef((TypeAeronef)spinnerType.getSelectedItem());
        currentVolsFilter.setAeronef((Aeronef)spinnerAeronef.getSelectedItem());
        currentVolsFilter.setSite((Site)spinnerSite.getSelectedItem());
        try {
            currentVolsFilter.setDateDebut(sdf.parse(editTextStartDate.getText().toString()));
        } catch (ParseException pe) {
            currentVolsFilter.setDateDebut(null);
        }
        try {
            currentVolsFilter.setDateFin(sdf.parse(editTextEndDate.getText().toString()));
        } catch (ParseException pe) {
            currentVolsFilter.setDateFin(null);
        }
        Intent intent = new Intent();
        intent.putExtra(VolsFilter.class.getName(), currentVolsFilter);
        setResult(FILTER_REQUEST_CODE,intent);
        finish();
    }
}