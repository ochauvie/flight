package com.och.flightbook.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.och.flightbook.R;
import com.och.flightbook.activity.MyDialogInterface.DialogReturn;
import com.och.flightbook.speech.TtsProviderFactory;
import com.och.flightbook.sqllite.DbJsonBackup;

public class ExportActivity extends Activity implements DialogReturn {

    private MyDialogInterface myInterface;
    private TtsProviderFactory ttsProviderImpl;
    private ImageButton btExport;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        ttsProviderImpl = TtsProviderFactory.getInstance();

        myInterface = new MyDialogInterface();
        myInterface.setListener(this);

        ttsProviderImpl.say(getString(R.string.menu_export_db));

        // Find aeronef
        btExport = (ImageButton) findViewById(R.id.btExport);
        btExport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backupDb();
            }
        });

    }


    private void backupDb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.backup);
        builder.setTitle(R.string.menu_export_db);
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

    @Override
    public void onDialogCompleted(boolean answer, String type) {
        if (answer) {
            CheckBox expVol = (CheckBox) findViewById(R.id.expVol);
            CheckBox expAeronef = (CheckBox) findViewById(R.id.expAeronef);
            CheckBox expSite = (CheckBox) findViewById(R.id.expSite);
            CheckBox expAccu = (CheckBox) findViewById(R.id.expAccu);
            CheckBox expChecklist = (CheckBox) findViewById(R.id.expChecklist);
            CheckBox expRadio = (CheckBox) findViewById(R.id.expRadio);

            if (expAeronef.isChecked() || expVol.isChecked() || expRadio.isChecked() || expChecklist.isChecked() || expSite.isChecked() || expAccu.isChecked()) {
                DbJsonBackup dbJsonBackup = new DbJsonBackup(this,
                        expAeronef.isChecked(),
                        expVol.isChecked(),
                        expRadio.isChecked(),
                        expChecklist.isChecked(),
                        expSite.isChecked(),
                        expAccu.isChecked());

                try {
                    String filePath = Environment.getExternalStorageDirectory().getPath() + "/";
                    dbJsonBackup.doBackup(filePath);
                    Toast.makeText(getBaseContext(),
                            "Done writing SD " + filePath,
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addclose, menu);
        MenuItem item = menu.findItem(R.id.add);
        item.setVisible(false);
        return true;
    }

    /**
     * Call when menu item is selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:
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