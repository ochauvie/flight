package com.carnetvol.sqllite.init;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.carnetvol.sqllite.DbManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class InitAccus {

	
	public static void initAccus(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
        values.put(DbManager.COL_ACCU_CYCLES, 0);
        values.put(DbManager.COL_ACCU_DATE_ACHAT, "2014/12/01");
        values.put(DbManager.COL_ACCU_NUMERO, 1);
        values.put(DbManager.COL_ACCU_MARQUE, "Pro Tronik");
        values.put(DbManager.COL_ACCU_NOM, "1-3S-2200");
        values.put(DbManager.COL_ACCU_TYPE, "LIPO");
        values.put(DbManager.COL_ACCU_ELEMENTS, 3);
        values.put(DbManager.COL_ACCU_CAPACITE, 2200);
        values.put(DbManager.COL_ACCU_VOLTAGE, 11.1);
        values.put(DbManager.COL_ACCU_TAUX_DECHARGE, 35);
        db.insert(DbManager.TABLE_ACCUS, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_ACCU_CYCLES, 0);
        values.put(DbManager.COL_ACCU_DATE_ACHAT, "2014/12/01");
        values.put(DbManager.COL_ACCU_NUMERO, 2);
        values.put(DbManager.COL_ACCU_MARQUE, "Pro Tronik");
        values.put(DbManager.COL_ACCU_NOM, "2-3S-2200");
        values.put(DbManager.COL_ACCU_TYPE, "LIPO");
        values.put(DbManager.COL_ACCU_ELEMENTS, 3);
        values.put(DbManager.COL_ACCU_CAPACITE, 2200);
        values.put(DbManager.COL_ACCU_VOLTAGE, 11.1);
        values.put(DbManager.COL_ACCU_TAUX_DECHARGE, 35);
        db.insert(DbManager.TABLE_ACCUS, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_ACCU_CYCLES, 0);
        values.put(DbManager.COL_ACCU_DATE_ACHAT, "2014/12/01");
        values.put(DbManager.COL_ACCU_NUMERO, 3);
        values.put(DbManager.COL_ACCU_MARQUE, "Pro Tronik");
        values.put(DbManager.COL_ACCU_NOM, "3-3S-2200");
        values.put(DbManager.COL_ACCU_TYPE, "LIPO");
        values.put(DbManager.COL_ACCU_ELEMENTS, 3);
        values.put(DbManager.COL_ACCU_CAPACITE, 2200);
        values.put(DbManager.COL_ACCU_VOLTAGE, 11.1);
        values.put(DbManager.COL_ACCU_TAUX_DECHARGE, 35);
        db.insert(DbManager.TABLE_ACCUS, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_ACCU_CYCLES, 0);
        values.put(DbManager.COL_ACCU_DATE_ACHAT, "2014/12/01");
        values.put(DbManager.COL_ACCU_NUMERO, 4);
        values.put(DbManager.COL_ACCU_MARQUE, "Pro Tronik");
        values.put(DbManager.COL_ACCU_NOM, "4-3S-2200");
        values.put(DbManager.COL_ACCU_TYPE, "LIPO");
        values.put(DbManager.COL_ACCU_ELEMENTS, 3);
        values.put(DbManager.COL_ACCU_CAPACITE, 2200);
        values.put(DbManager.COL_ACCU_VOLTAGE, 11.1);
        values.put(DbManager.COL_ACCU_TAUX_DECHARGE, 35);
        db.insert(DbManager.TABLE_ACCUS, null, values);
	}
			
}
