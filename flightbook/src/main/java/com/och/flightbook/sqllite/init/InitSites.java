package com.och.flightbook.sqllite.init;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.och.flightbook.sqllite.DbManager;

public final class InitSites {

	
	public static void initSites(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, "HOBBY CLUB");
		values.put(DbManager.COL_COMMENT, "Belfontaine");
        values.put(DbManager.COL_DEFAULT, 1);
		db.insert(DbManager.TABLE_SITES, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_NAME, "Fôret Puiseux");
        values.put(DbManager.COL_COMMENT, "Parcours de santé");
        db.insert(DbManager.TABLE_SITES, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_NAME, "Carrière Belfontaine");
        values.put(DbManager.COL_COMMENT, "");
        db.insert(DbManager.TABLE_SITES, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Fôret Pélissanne");
		values.put(DbManager.COL_COMMENT, "");
		db.insert(DbManager.TABLE_SITES, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_NAME, "Jardin Pélissanne");
        values.put(DbManager.COL_COMMENT, "");
        db.insert(DbManager.TABLE_SITES, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_NAME, "Saint Martin Vésubie");
        values.put(DbManager.COL_COMMENT, "Jardin");
        db.insert(DbManager.TABLE_SITES, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_NAME, "Montée Colmiane");
        values.put(DbManager.COL_COMMENT, "");
        db.insert(DbManager.TABLE_SITES, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_NAME, "Boréon");
        values.put(DbManager.COL_COMMENT, "");
        db.insert(DbManager.TABLE_SITES, null, values);

		

	}
			
}
