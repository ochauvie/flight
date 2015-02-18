package com.flightbook.sqllite.init;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.flightbook.sqllite.DbManager;

public class DbApplicationContext {

    private Context appContext;
    private DbManager dbManager;
    private SQLiteDatabase bdd;

    private DbApplicationContext(){}

    public void init(Context context){
        if(appContext == null){
            appContext = context;
            dbManager = new DbManager(context, DbManager.NOM_BDD, null, DbManager.VERSION_BDD);
            bdd = dbManager.getWritableDatabase();

        }
    }

    private Context getContext(){
        return appContext;
    }

    public static Context get(){
        return getInstance().getContext();
    }

    private static DbApplicationContext instance;

    public static DbApplicationContext getInstance(){
        return instance == null ?
                (instance = new DbApplicationContext()):
                instance;
    }

    public SQLiteDatabase getBdd() {
        return bdd;
    }

}
