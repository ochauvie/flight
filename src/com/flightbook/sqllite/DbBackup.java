package com.flightbook.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.flightbook.model.Aeronef;
import com.flightbook.model.Potar;
import com.flightbook.model.Radio;
import com.flightbook.model.Switch;
import com.flightbook.model.Vol;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DbBackup {

    private DbAeronef dbAeronef;
    private DbVol dbVol;
    private DbRadio dbRadio;
    private DbChecklist dbCheckList;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);


    public DbBackup(Context context) {
        dbAeronef = new DbAeronef(context);
        dbVol = new DbVol(context);
        dbRadio = new DbRadio(context);
        dbCheckList = new DbChecklist(context);
    }

    public void doBackup() throws Exception {
        ArrayList<Aeronef> aeronefs;
        ArrayList<Vol> vols;
        ArrayList<Radio> radios;
        List<Switch> switchs;
        List<Potar> potars;

        // Recuperation des areronefs
        dbAeronef.open();
        aeronefs = dbAeronef.getAeronefs();
        dbAeronef.close();

        // Recuperation des vols
        dbVol.open();
        vols = dbVol.getVols();
        dbVol.close();

        // Recuperation des radios
        dbRadio.open();
        radios = dbRadio.getRadios();
        dbRadio.close();

        // write on SD card file data in the text box
        File myFile = new File("/sdcard/CarnetVolBackup.txt");
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);


        if (aeronefs != null) {
            myOutWriter.append("MACHINES");
            myOutWriter.append("\n");
            myOutWriter.append("Type|Moteur|Note|Date premier vol|Masse|Envergure");
            myOutWriter.append("\n");
            for (int i = 0; i < aeronefs.size(); i++) {
                Aeronef aeronef = aeronefs.get(i);
                myOutWriter.append(aeronef.getType());
                myOutWriter.append('|');
                myOutWriter.append(aeronef.getName());
                myOutWriter.append('|');
                myOutWriter.append(aeronef.getEngine());
                myOutWriter.append('|');
                myOutWriter.append(aeronef.getComment());
                myOutWriter.append('|');
                myOutWriter.append(aeronef.getFirstFlight());
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(aeronef.getWeight()));
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(aeronef.getWingSpan()));
                myOutWriter.append("\n");
            }
        }
        myOutWriter.append("\n");

        if (vols != null) {
            myOutWriter.append("ENREGISTREMENTS");
            myOutWriter.append("\n");
            myOutWriter.append("Type|Date|Nom|Min vol|Min moteur|Sec moteur|Note|Lieu");
            myOutWriter.append("\n");
            for (int i = 0; i < vols.size(); i++) {
                Vol vol = vols.get(i);
                myOutWriter.append(vol.getType());
                myOutWriter.append('|');
                myOutWriter.append(sdf.format(vol.getDateVol()));
                myOutWriter.append('|');
                myOutWriter.append(vol.getAeronef());
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(vol.getMinutesVol()));
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(vol.getMinutesMoteur())).append(":").append(String.valueOf(vol.getSecondsMoteur()));
                myOutWriter.append('|');
                myOutWriter.append(vol.getNote());
                myOutWriter.append('|');
                myOutWriter.append(vol.getLieu());
                myOutWriter.append("\n");
            }
        }

        myOutWriter.append("\n");

        // Recuperation des radios
        if (radios != null) {
            myOutWriter.append("PROGRAMMES RADIO");
            myOutWriter.append("\n");
            myOutWriter.append("Nom");
            myOutWriter.append("\n");
            myOutWriter.append("Type|Nom|Action|Down|Center|Up");
            myOutWriter.append("\n");
            myOutWriter.append("\n");
            for (int i = 0; i < radios.size(); i++) {
                Radio radio = radios.get(i);
                myOutWriter.append(radio.getName());
                myOutWriter.append("\n");
                switchs = radio.getSwitchs();
                if (switchs != null) {
                    for (int j = 0; j < switchs.size(); j++) {
                        Switch sw = switchs.get(j);
                        myOutWriter.append("Switch");
                        myOutWriter.append('|');
                        myOutWriter.append(sw.getName());
                        myOutWriter.append('|');
                        myOutWriter.append(sw.getAction());
                        myOutWriter.append('|');
                        myOutWriter.append(sw.getDown());
                        myOutWriter.append('|');
                        myOutWriter.append(sw.getCenter());
                        myOutWriter.append('|');
                        myOutWriter.append(sw.getUp());
                        myOutWriter.append("\n");
                    }
                    potars = radio.getPotars();
                    if (potars != null) {
                        for (int j = 0; j < potars.size(); j++) {
                            Potar potar = potars.get(j);
                            myOutWriter.append("Potar");
                            myOutWriter.append('|');
                            myOutWriter.append(potar.getName());
                            myOutWriter.append('|');
                            myOutWriter.append(potar.getAction());
                            myOutWriter.append('|');
                            myOutWriter.append(potar.getDown());
                            myOutWriter.append('|');
                            myOutWriter.append(potar.getCenter());
                            myOutWriter.append('|');
                            myOutWriter.append(potar.getUp());
                            myOutWriter.append("\n");
                        }
                    }
                }
                myOutWriter.append("\n");
            }


            // TODO : dbCheckList


            myOutWriter.close();
            fOut.close();

        }

    }
}
