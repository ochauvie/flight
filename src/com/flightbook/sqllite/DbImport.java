package com.flightbook.sqllite;

import android.content.Context;

import com.flightbook.model.Accu;
import com.flightbook.model.Vol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DbImport {

    private DbAeronef dbAeronef;
    private DbVol dbVol;
    private DbRadio dbRadio;
    private DbChecklist dbCheckList;
    private DbSite dbSite;
    private DbAccu dbAccu;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    private static final String HEADER1_VOLS = "ENREGISTREMENTS";
    private static final String HEADER2_VOLS = "Type|Date|Nom|Min vol|Min moteur|Sec moteur|Note|Lieu|Accu";

    public DbImport(Context context) {
        dbAeronef = new DbAeronef(context);
        dbVol = new DbVol(context);
        dbRadio = new DbRadio(context);
        dbCheckList = new DbChecklist(context);
        dbSite = new DbSite(context);
        dbAccu = new DbAccu(context);
    }

    public String importVols(File file) {
        ArrayList<Vol> vols = new ArrayList<Vol>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int nbLine = 0;
            while ((line = br.readLine()) != null) {
                nbLine++;
                if (nbLine == 1) {
                    if (!HEADER1_VOLS.equals(line)) {
                        return "Le fichier n'est pas une liste d'enregistrements";
                    }
                } else if (nbLine == 2) {
                    if (!HEADER2_VOLS.equals(line)) {
                        return "Le fichier n'est pas une liste d'enregistrements";
                    }
                } else {
                    String[] elements = line.split("\\|");

                    try {
                        Vol vol = new Vol();
                        vol.setType(elements[0]);
                        vol.setDateVol(sdf.parse(elements[1]));
                        vol.setAeronef(elements[2]);
                        if (elements.length>=4) {
                            vol.setMinutesVol(Integer.valueOf(elements[3]));
                        } else {
                            vol.setMinutesVol(0);
                        }
                        if (elements.length>=5) {
                            vol.setMinutesMoteur(Integer.valueOf(elements[4]));
                        } else {
                            vol.setMinutesMoteur(0);
                        }
                        if (elements.length>=6) {
                            vol.setSecondsMoteur(Integer.valueOf(elements[5]));
                        } else {
                            vol.setSecondsMoteur(0);
                        }
                        if (elements.length>=7) {
                            vol.setNote(elements[6]);
                        }
                        if (elements.length>=8) {
                            vol.setLieu(elements[7]);
                        }
                        if (elements.length>=9) {
                            dbAccu.open();
                            Accu accu = dbAccu.getAccuByName(elements[8]);
                            dbAccu.close();
                            vol.setAccuPropulsion(accu);
                        }
                        vols.add(vol);
                    } catch (ParseException perE) {
                        return "Erreur dans le format de la date du vol pour la ligne " + nbLine;
                    }

                }
            }
            br.close();
        } catch (FileNotFoundException fnfE) {
            return "Emplacement du fichier incorrecte";
        } catch (IOException ioE) {
            return "Fichier incorrecte";
        }

        dbVol.open();
        for (Vol vol:vols) {
            dbVol.insertVol(vol);
        }
        dbVol.close();

        return null;
    }
}