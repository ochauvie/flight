package com.flightbook.sqllite;

import android.content.Context;

import com.flightbook.model.Accu;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Checklist;
import com.flightbook.model.ChecklistItem;
import com.flightbook.model.Potar;
import com.flightbook.model.Radio;
import com.flightbook.model.Site;
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
    private DbSite dbSite;
    private DbAccu dbAccu;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);


    public DbBackup(Context context) {
        dbAeronef = new DbAeronef(context);
        dbVol = new DbVol(context);
        dbRadio = new DbRadio(context);
        dbCheckList = new DbChecklist(context);
        dbSite = new DbSite(context);
        dbAccu = new DbAccu(context);
    }

    public void doBackup(String fileName) throws Exception {

        ArrayList<Aeronef> aeronefs;
        ArrayList<Vol> vols;
        ArrayList<Radio> radios;
        List<Switch> switchs;
        List<Potar> potars;
        ArrayList<Checklist> checklists;
        ArrayList<Site> sites;
        ArrayList<Accu> accus;

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

        // Recuperation des check list
        dbCheckList.open();
        checklists = dbCheckList.getChecklists(null);
        dbCheckList.close();

        // Recuperation des sites
        dbSite.open();
        sites = dbSite.getSites();
        dbSite.close();

        // Recuperation des accus
        dbAccu.open();
        accus = dbAccu.getAccus();
        dbAccu.close();

        // write on SD card file data in the text box
        File myFile = new File(fileName);
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


        if (sites != null) {
            myOutWriter.append("SITES");
            myOutWriter.append("\n");
            myOutWriter.append("Nom|Note");
            myOutWriter.append("\n");
            for (int i = 0; i < sites.size(); i++) {
                Site site = sites.get(i);
                myOutWriter.append(site.getName());
                myOutWriter.append('|');
                myOutWriter.append(site.getComment());
                myOutWriter.append("\n");
            }
        }

        myOutWriter.append("\n");


        if (vols != null) {
            myOutWriter.append("ENREGISTREMENTS");
            myOutWriter.append("\n");
            myOutWriter.append("Type|Date|Nom|Min vol|Min moteur|Sec moteur|Note|Lieu|Accu");
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
                myOutWriter.append('|');
                if (vol.getAccuPropulsion()!=null) {
                    myOutWriter.append(vol.getAccuPropulsion().getNom());
                } else {
                    myOutWriter.append("");
                }
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

        }

        // Recuperation des checklist
        if (checklists != null) {
            myOutWriter.append("CHECKLIST");
            myOutWriter.append("\n");
            myOutWriter.append("Nom");
            myOutWriter.append("\n");
            myOutWriter.append("Ordre|Action");
            myOutWriter.append("\n");
            myOutWriter.append("\n");
            for (int i = 0; i < checklists.size(); i++) {
                Checklist checklist = checklists.get(i);
                myOutWriter.append(checklist.getName());
                myOutWriter.append("\n");
                ArrayList<ChecklistItem> items = checklist.getItems();
                if (items != null) {
                    for (int j = 0; j < items.size(); j++) {
                        ChecklistItem item = items.get(j);
                        myOutWriter.append(String.valueOf(item.getOrder()));
                        myOutWriter.append('|');
                        myOutWriter.append(item.getAction());
                        myOutWriter.append("\n");
                    }
                }
                myOutWriter.append("\n");
            }
        }

        // Récupération des accus
        if (accus != null) {
            myOutWriter.append("ACCUS");
            myOutWriter.append("\n");
            myOutWriter.append("Nom|Type|Nb éléments|Capacité|Taux décharge|Voltage|Marque|Numéro|Date achat|Nombre de cycles");
            myOutWriter.append("\n");
            for (int i = 0; i < accus.size(); i++) {
                Accu accu = accus.get(i);
                myOutWriter.append(accu.getNom());
                myOutWriter.append('|');
                myOutWriter.append(accu.getType().name());
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(accu.getNbElements()));
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(accu.getCapacite()));
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(accu.getTauxDecharge()));
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(accu.getVoltage()));
                myOutWriter.append('|');
                myOutWriter.append(accu.getMarque());
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(accu.getNumero()));
                myOutWriter.append('|');
                if (accu.getDateAchat()!=null) {
                    myOutWriter.append(sdf.format(accu.getDateAchat()));
                } else {
                    myOutWriter.append("");
                }
                myOutWriter.append('|');
                myOutWriter.append(String.valueOf(accu.getNbCycles()));
                myOutWriter.append("\n");
            }
        }

        myOutWriter.close();
        fOut.close();
    }
}
