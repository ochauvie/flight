package com.och.flightbook.sqllite;

import android.content.Context;

import com.och.flightbook.model.Accu;
import com.och.flightbook.model.Aeronef;
import com.och.flightbook.model.Checklist;
import com.och.flightbook.model.Potar;
import com.och.flightbook.model.Radio;
import com.och.flightbook.model.Site;
import com.och.flightbook.model.Switch;
import com.och.flightbook.model.Vol;
import com.och.flightbook.tools.MyExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DbJsonImport {

    private Gson gson;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    public DbJsonImport(Context context) {
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
                    return sdf.parse((json).getAsString());
                }
                catch (ParseException e)
                {
                    throw new JsonParseException(e);
                }
            }
        }).create();

    }

    public String importAeronefs(File file) {
        try {
            String json = getJson(file);

            Aeronef[] aeronefs = gson.fromJson(json, Aeronef[].class);
            if (aeronefs!=null) {
                for (Aeronef aeronef:aeronefs) {
                    DbAeronef.insertAeronef(aeronef);
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    public String importVols(File file) {
        try {
            String json = getJson(file);
            Vol[] vols = gson.fromJson(json, Vol[].class);
            if (vols!=null) {
                for (Vol vol:vols) {
                    DbVol.insertVol(vol);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    public String importSites(File file) {
        try {
            String json = getJson(file);
            Site[] sites = gson.fromJson(json, Site[].class);
            if (sites!=null) {
                for (Site site:sites) {
                    DbSite.insertSite(site);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    public String importAccus(File file) {
        try {
            String json = getJson(file);
            Accu[] accus = gson.fromJson(json, Accu[].class);
            if (accus!=null) {
                for (Accu accu:accus) {
                    DbAccu.insertAccu(accu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }

    public String importRadios(File file) {
        try {
            String json = getJson(file);
            Radio[] radios = gson.fromJson(json, Radio[].class);
            if (radios!=null) {
                for (Radio radio:radios) {
                    long id = DbRadio.addRadio(radio);
                    if (radio.getPotars()!=null) {
                        for (Potar potar:radio.getPotars()) {
                            DbRadio.addPotarToRadio(id, potar);
                        }
                    }
                    if (radio.getSwitchs()!=null) {
                        for (Switch sw:radio.getSwitchs()) {
                            DbRadio.addSwitchToRadio(id, sw);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }


    public String importChecklists(File file) {
        try {
            String json = getJson(file);
            Checklist[] checklists = gson.fromJson(json, Checklist[].class);
            if (checklists!=null) {
                for (Checklist checklist:checklists) {
                    DbChecklist.addCheckList(checklist);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
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