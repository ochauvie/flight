package com.olivier.sqllite;


import com.olivier.model.Aeronef;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper {

	
	public static final String TABLE_VOLS = "table_vols";
	public static final String COL_ID = "ID";
	public static final int NUM_COL_ID = 0;
	public static final String COL_NAME = "NAME";
	public static final int NUM_COL_NAME = 1;
	public static final String COL_MIN_VOL = "MIN_VOL";
	public static final int NUM_COL_MIN_VOL = 3;
	public static final String COL_MIN_MOTEUR = "MIN_MOT";
	public static final int NUM_COL_MIN_MOTEUR = 4;
	public static final String COL_SEC_MOTEUR = "SEC_MOT";
	public static final int NUM_COL_SEC_MOTEUR = 5;
	public static final String COL_DATE = "DATE_VOL";
	public static final int NUM_COL_DATE = 6;
	public static final String COL_NOTE = "NOTE";
	public static final int NUM_COL_NOTE = 7;
	public static final String COL_LIEU = "LIEU";
	public static final int NUM_COL_LIEU = 8;
	
	public static final String TABLE_AERONEFS = "table_aeronefs";
	//public static final String COL_ID = "ID";
	//public static final int NUM_COL_ID = 0;
	//public static final String COL_NAME = "NAME";
	//public static final int NUM_COL_NAME = 1;
	public static final String COL_TYPE = "TYPE";
	public static final int NUM_COL_TYPE = 2;
	
	public static final String COL_WINGSPAN = "WINGSPAN";
	public static final int NUM_COL_WINGSPAN = 3;
	public static final String COL_WEIGHT = "WEIGHT";
	public static final int NUM_COL_WEIGHT = 4;
	public static final String COL_ENGINE = "ENGINE";
	public static final int NUM_COL_ENGINE = 5;
	public static final String COL_FIRST_FLIGHT = "FIRST_FLIGHT";
	public static final int NUM_COL_FIRST_FLIGHT = 6;
	public static final String COL_COMMENT = "COMMENT";
	public static final int NUM_COL_COMMENT = 7;
	
	
	private static final String CREATE_TABLE_VOLS = "CREATE TABLE " + TABLE_VOLS + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL, "
			+ COL_TYPE + " TEXT, "
			+ COL_MIN_VOL + " INTEGER, "
			+ COL_MIN_MOTEUR + " INTEGER, "
			+ COL_SEC_MOTEUR + " INTEGER, "
			+ COL_DATE + " TEXT NOT NULL, "
			+ COL_NOTE + " TEXT, "
			+ COL_LIEU + " TEXT);";
	
	
	private static final String CREATE_TABLE_AERONEFS = "CREATE TABLE " + TABLE_AERONEFS + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL, "
			+ COL_TYPE + " TEXT, "
			+ COL_WINGSPAN + " REAL, "
			+ COL_WEIGHT + " REAL, "
			+ COL_ENGINE + " TEXT, "
			+ COL_FIRST_FLIGHT + " TEXT, "
			+ COL_COMMENT + " TEXT);";
	
	public DbManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_VOLS);
		db.execSQL(CREATE_TABLE_AERONEFS);
		initHangar(db);
	}
	
	private void initHangar(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, "Alpina 4001");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 4);
		values.put(DbManager.COL_WEIGHT, 4.8);
		values.put(DbManager.COL_ENGINE, "Protronik DM2830-660");
		values.put(DbManager.COL_FIRST_FLIGHT, "17/06/2012");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 2);
		values.put(DbManager.COL_WEIGHT, 1.5);
		values.put(DbManager.COL_ENGINE, "Protronik DM2615-1100");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Spatz 55");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 2);
		values.put(DbManager.COL_ENGINE, "Protronik DM2810-800");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Calmato");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_ENGINE, "OS 46 LA");
		values.put(DbManager.COL_FIRST_FLIGHT, "29/10/2011");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "P40");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_ENGINE, "OS 46 LA");
		values.put(DbManager.COL_FIRST_FLIGHT, "22/03/2003");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Broussard");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_WINGSPAN, 2.8);
		values.put(DbManager.COL_WEIGHT, 11);
		values.put(DbManager.COL_ENGINE, "Zenoah 45 PCI");
		values.put(DbManager.COL_FIRST_FLIGHT, "16/04/2011");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Raptor");
		values.put(DbManager.COL_TYPE, Aeronef.T_HELICO);
		values.put(DbManager.COL_ENGINE, "OS 32");
		values.put(DbManager.COL_FIRST_FLIGHT, "06/07/2002");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Honcho");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_ENGINE, "Axial 55T");
		values.put(DbManager.COL_FIRST_FLIGHT, "20/08/2011");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Savage X");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_ENGINE, "HPI 4.1");
		values.put(DbManager.COL_FIRST_FLIGHT, "02/08/2007");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Bullet");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_FIRST_FLIGHT, "10/09/2010");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values.put(DbManager.COL_NAME, "Opale");
		values.put(DbManager.COL_TYPE, Aeronef.T_PARAMOTEUR);
		values.put(DbManager.COL_ENGINE, "Protronik DM2810-1200");
		values.put(DbManager.COL_FIRST_FLIGHT, "10/04/2010");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_VOLS + ";");
		db.execSQL("DROP TABLE " + TABLE_AERONEFS + ";");
		onCreate(db);
	}

}
