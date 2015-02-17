package com.flightbook.sqllite;

import android.content.Context;

import com.flightbook.model.Accu;
import com.flightbook.model.Aeronef;
import com.flightbook.model.Checklist;
import com.flightbook.model.Potar;
import com.flightbook.model.Radio;
import com.flightbook.model.Site;
import com.flightbook.model.Switch;
import com.flightbook.model.Vol;
import com.flightbook.tools.MyExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DbJsonImport {

    private DbAeronef dbAeronef;
    private DbVol dbVol;
    private DbRadio dbRadio;
    private DbChecklist dbCheckList;
    private DbSite dbSite;
    private DbAccu dbAccu;
    private Gson gson;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public DbJsonImport(Context context) {
        dbAeronef = new DbAeronef(context);
        dbVol = new DbVol(context);
        dbRadio = new DbRadio(context);
        dbCheckList = new DbChecklist(context);
        dbSite = new DbSite(context);
        dbAccu = new DbAccu(context);
        gson = new GsonBuilder().serializeNulls()
                .setExclusionStrategies(new MyExclusionStrategy(null))
                .setDateFormat("dd/MM/yyyy")
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
        {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException
            {
                try
                {
                    System.out.println(json.getAsString());
                    return sdf.parse(((JsonPrimitive) json).getAsString());
                }
                catch (ParseException e)
                {
                    throw new JsonParseException(e);
                }
            }
        }).create();

    }

    public String importAeronefs(File file) {
        dbAeronef.open();
        try {
            String json = getJson(file);

            Aeronef[] aeronefs = gson.fromJson(json, Aeronef[].class);
            if (aeronefs!=null) {
                for (Aeronef aeronef:aeronefs) {
                    dbAeronef.insertAeronef(aeronef);
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        } finally {
            dbAeronef.close();
        }
        return null;
    }

    public String importVols(File file) {
        dbVol.open();
        try {
            String json = getJson(file);
            Vol[] vols = gson.fromJson(json, Vol[].class);
            if (vols!=null) {
                for (Vol vol:vols) {
                    dbVol.insertVol(vol);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            dbVol.close();
        }
        return null;
    }

    public String importSites(File file) {
        dbSite.open();
        try {
            String json = getJson(file);
            Site[] sites = gson.fromJson(json, Site[].class);
            if (sites!=null) {
                for (Site site:sites) {
                    dbSite.insertSite(site);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            dbSite.close();
        }
        return null;
    }

    public String importAccus(File file) {
        dbAccu.open();
        try {
            String json = getJson(file);
            Accu[] accus = gson.fromJson(json, Accu[].class);
            if (accus!=null) {
                for (Accu accu:accus) {
                    dbAccu.insertAccu(accu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            dbAccu.close();
        }
        return null;
    }

    public String importRadios(File file) {
        dbRadio.open();
        try {
            String json = getJson(file);
            Radio[] radios = gson.fromJson(json, Radio[].class);
            if (radios!=null) {
                for (Radio radio:radios) {
                    long id = dbRadio.addRadio(radio);
                    if (radio.getPotars()!=null) {
                        for (Potar potar:radio.getPotars()) {
                            dbRadio.addPotarToRadio(id, potar);
                        }
                    }
                    if (radio.getSwitchs()!=null) {
                        for (Switch sw:radio.getSwitchs()) {
                            dbRadio.addSwitchToRadio(id, sw);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            dbRadio.close();
        }
        return null;
    }


    public String importChecklists(File file) {
        dbCheckList.open();
        try {
            String json = getJson(file);
            Checklist[] checklists = gson.fromJson(json, Checklist[].class);
            if (checklists!=null) {
                for (Checklist checklist:checklists) {
                    dbCheckList.addCheckList(checklist);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            dbCheckList.close();
        }
        return null;
    }

    /**
     * Return json data from the file
     * @param file the file
     * @return the json string
     * @throws Exception Error
     */
    private String getJson(File file) throws Exception {
        String json = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            json = json + line;
        }
        br.close();
        return json;
    }
}