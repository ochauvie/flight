package com.flightbook.sqllite;

import android.content.Context;

import com.flightbook.model.Accu;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Checklist;
import com.flightbook.model.Radio;
import com.flightbook.model.Site;
import com.flightbook.model.Vol;
import com.flightbook.tools.MyExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class DbJsonBackup {

    private boolean addAeronefs, addVols, addRadios, addChecklists, addSites, addAccus;
    private Gson gson;

    public DbJsonBackup(Context context, boolean addAeronefs, boolean addVols, boolean addRadios, boolean addChecklists, boolean addSites, boolean addAccus) {
        this.addAeronefs = addAeronefs;
        this.addVols = addVols;
        this.addRadios = addRadios;
        this.addChecklists = addChecklists;
        this.addSites = addSites;
        this.addAccus = addAccus;

        gson = new GsonBuilder().serializeNulls()
                .setExclusionStrategies(new MyExclusionStrategy(null))
                .setDateFormat("dd/MM/yyyy")
                .create();
    }


    public void doBackup(String filePath) throws Exception {
        if (addAeronefs) {
            doBackupAreonefs(filePath);
        }
        if (addVols) {
            doBackupVols(filePath);
        }
        if (addSites) {
            doBackupSites(filePath);
        }
        if (addAccus) {
            doBackupAccus(filePath);
        }
        if (addRadios) {
            doBackupRadios(filePath);
        }
        if (addChecklists) {
            doBackupChecklists(filePath);
        }

    }

    private void doBackupAreonefs(String filePath) throws Exception  {
        File myFile = new File(filePath + "Flight_Book_Machines.json");
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

        // Recuperation des areronefs
        ArrayList<Aeronef> aeronefs = DbAeronef.getAeronefs();

        String json = gson.toJson(aeronefs);

        myOutWriter.append(json);

        myOutWriter.close();
        fOut.close();
    }


    private void doBackupVols(String filePath) throws Exception  {
        File myFile = new File(filePath + "Flight_Book_Flights.json");
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

        ArrayList<Vol> vols = DbVol.getVols();

        myOutWriter.append(gson.toJson(vols));

        myOutWriter.close();
        fOut.close();
    }

    private void doBackupSites(String filePath) throws Exception  {
        File myFile = new File(filePath + "Flight_Book_Sites.json");
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

        ArrayList<Site> sites = DbSite.getSites();

        myOutWriter.append(gson.toJson(sites));

        myOutWriter.close();
        fOut.close();
    }

    private void doBackupAccus(String filePath) throws Exception  {
        File myFile = new File(filePath + "Flight_Book_Accus.json");
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

        ArrayList<Accu> accus = DbAccu.getAccus();

        myOutWriter.append(gson.toJson(accus));

        myOutWriter.close();
        fOut.close();
    }

    private void doBackupRadios(String filePath) throws Exception  {
        File myFile = new File(filePath + "Flight_Book_Radios.json");
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

        ArrayList<Radio> radios = DbRadio.getRadios();

        myOutWriter.append(gson.toJson(radios));

        myOutWriter.close();
        fOut.close();
    }

    private void doBackupChecklists(String filePath) throws Exception  {
        File myFile = new File(filePath + "Flight_Book_Checklists.json");
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

        ArrayList<Checklist> checklists = DbChecklist.getChecklists(null);

        myOutWriter.append(gson.toJson(checklists));

        myOutWriter.close();
        fOut.close();
    }

}