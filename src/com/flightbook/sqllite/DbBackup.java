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
        File myFile = new File(fileName);
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

        myOutWriter.append(getStbAeronefs());
        myOutWriter.append(getStbSites());
        myOutWriter.append(getStbVols());
        myOutWriter.append(getStbRadios());
        myOutWriter.append(getStbCheckList());
        myOutWriter.append(getStbAccus());

        myOutWriter.close();
        fOut.close();
    }


    public StringBuffer getStbVols() {
        StringBuffer stbVols = new StringBuffer();

        // Recuperation des vols
        dbVol.open();
        ArrayList<Vol> vols = dbVol.getVols();
        dbVol.close();

        if (vols != null) {
            stbVols.append("ENREGISTREMENTS");
            stbVols.append("\n");
            stbVols.append("Type|Date|Nom|Min vol|Min moteur|Sec moteur|Note|Lieu|Accu");
            stbVols.append("\n");
            for (int i = 0; i < vols.size(); i++) {
                Vol vol = vols.get(i);
                stbVols.append(vol.getType());
                stbVols.append('|');
                stbVols.append(sdf.format(vol.getDateVol()));
                stbVols.append('|');
                stbVols.append(vol.getAeronef());
                stbVols.append('|');
                stbVols.append(String.valueOf(vol.getMinutesVol()));
                stbVols.append('|');
                stbVols.append(String.valueOf(vol.getMinutesMoteur())).append(":").append(String.valueOf(vol.getSecondsMoteur()));
                stbVols.append('|');
                stbVols.append(vol.getNote());
                stbVols.append('|');
                stbVols.append(vol.getLieu());
                stbVols.append('|');
                if (vol.getAccuPropulsion() != null) {
                    stbVols.append(vol.getAccuPropulsion().getNom());
                } else {
                    stbVols.append("");
                }
                stbVols.append("\n");
            }
        }
        stbVols.append("\n");
        return stbVols;
    }


    public StringBuffer getStbSites() {
        StringBuffer stbSites = new StringBuffer();

        // Recuperation des sites
        dbSite.open();
        ArrayList<Site> sites = dbSite.getSites();
        dbSite.close();

        if (sites != null) {
            stbSites.append("SITES");
            stbSites.append("\n");
            stbSites.append("Nom|Note");
            stbSites.append("\n");
            for (int i = 0; i < sites.size(); i++) {
                Site site = sites.get(i);
                stbSites.append(site.getName());
                stbSites.append('|');
                stbSites.append(site.getComment());
                stbSites.append("\n");
            }
        }
        stbSites.append("\n");
        return stbSites;
    }

    public StringBuffer getStbRadios() {
        StringBuffer stb = new StringBuffer();
        List<Switch> switchs;
        List<Potar> potars;

        // Recuperation des radios
        dbRadio.open();
        ArrayList<Radio> radios = dbRadio.getRadios();
        dbRadio.close();

        // Recuperation des radios
        if (radios != null) {
            stb.append("PROGRAMMES RADIO");
            stb.append("\n");
            stb.append("Nom");
            stb.append("\n");
            stb.append("Type|Nom|Action|Down|Center|Up");
            stb.append("\n");
            stb.append("\n");
            for (int i = 0; i < radios.size(); i++) {
                Radio radio = radios.get(i);
                stb.append(radio.getName());
                stb.append("\n");
                switchs = radio.getSwitchs();
                if (switchs != null) {
                    for (int j = 0; j < switchs.size(); j++) {
                        Switch sw = switchs.get(j);
                        stb.append("Switch");
                        stb.append('|');
                        stb.append(sw.getName());
                        stb.append('|');
                        stb.append(sw.getAction());
                        stb.append('|');
                        stb.append(sw.getDown());
                        stb.append('|');
                        stb.append(sw.getCenter());
                        stb.append('|');
                        stb.append(sw.getUp());
                        stb.append("\n");
                    }
                    potars = radio.getPotars();
                    if (potars != null) {
                        for (int j = 0; j < potars.size(); j++) {
                            Potar potar = potars.get(j);
                            stb.append("Potar");
                            stb.append('|');
                            stb.append(potar.getName());
                            stb.append('|');
                            stb.append(potar.getAction());
                            stb.append('|');
                            stb.append(potar.getDown());
                            stb.append('|');
                            stb.append(potar.getCenter());
                            stb.append('|');
                            stb.append(potar.getUp());
                            stb.append("\n");
                        }
                    }
                }
                stb.append("\n");
            }

        }
        return stb;
    }

    public StringBuffer getStbCheckList() {
        StringBuffer stb = new StringBuffer();
        // Recuperation des check list
        dbCheckList.open();
        ArrayList<Checklist> checklists = dbCheckList.getChecklists(null);
        dbCheckList.close();

        // Recuperation des checklist
        if (checklists != null) {
            stb.append("CHECKLIST");
            stb.append("\n");
            stb.append("Nom");
            stb.append("\n");
            stb.append("Ordre|Action");
            stb.append("\n");
            stb.append("\n");
            for (int i = 0; i < checklists.size(); i++) {
                Checklist checklist = checklists.get(i);
                stb.append(checklist.getName());
                stb.append("\n");
                ArrayList<ChecklistItem> items = checklist.getItems();
                if (items != null) {
                    for (int j = 0; j < items.size(); j++) {
                        ChecklistItem item = items.get(j);
                        stb.append(String.valueOf(item.getOrder()));
                        stb.append('|');
                        stb.append(item.getAction());
                        stb.append("\n");
                    }
                }
                stb.append("\n");
            }
        }

        return stb;
    }

    public StringBuffer getStbAccus() {
        StringBuffer stb = new StringBuffer();
        // Recuperation des accus
        dbAccu.open();
        ArrayList<Accu> accus = dbAccu.getAccus();
        dbAccu.close();

        // Récupération des accus
        if (accus != null) {
            stb.append("ACCUS");
            stb.append("\n");
            stb.append("Nom|Type|Nb éléments|Capacité|Taux décharge|Voltage|Marque|Numéro|Date achat|Nombre de cycles");
            stb.append("\n");
            for (int i = 0; i < accus.size(); i++) {
                Accu accu = accus.get(i);
                stb.append(accu.getNom());
                stb.append('|');
                stb.append(accu.getType().name());
                stb.append('|');
                stb.append(String.valueOf(accu.getNbElements()));
                stb.append('|');
                stb.append(String.valueOf(accu.getCapacite()));
                stb.append('|');
                stb.append(String.valueOf(accu.getTauxDecharge()));
                stb.append('|');
                stb.append(String.valueOf(accu.getVoltage()));
                stb.append('|');
                stb.append(accu.getMarque());
                stb.append('|');
                stb.append(String.valueOf(accu.getNumero()));
                stb.append('|');
                if (accu.getDateAchat() != null) {
                    stb.append(sdf.format(accu.getDateAchat()));
                } else {
                    stb.append("");
                }
                stb.append('|');
                stb.append(String.valueOf(accu.getNbCycles()));
                stb.append("\n");
            }
        }
        return stb;
    }

    public StringBuffer getStbAeronefs() {
        StringBuffer stb = new StringBuffer();

        // Recuperation des areronefs
        dbAeronef.open();
        ArrayList<Aeronef> aeronefs = dbAeronef.getAeronefs();
        dbAeronef.close();

        if (aeronefs != null) {
            stb.append("MACHINES");
            stb.append("\n");
            stb.append("Type|Moteur|Note|Date premier vol|Masse|Envergure");
            stb.append("\n");
            for (int i = 0; i < aeronefs.size(); i++) {
                Aeronef aeronef = aeronefs.get(i);
                stb.append(aeronef.getType());
                stb.append('|');
                stb.append(aeronef.getName());
                stb.append('|');
                stb.append(aeronef.getEngine());
                stb.append('|');
                stb.append(aeronef.getComment());
                stb.append('|');
                stb.append(aeronef.getFirstFlight());
                stb.append('|');
                stb.append(String.valueOf(aeronef.getWeight()));
                stb.append('|');
                stb.append(String.valueOf(aeronef.getWingSpan()));
                stb.append("\n");
            }
        }
        stb.append("\n");
        return stb;
    }
}