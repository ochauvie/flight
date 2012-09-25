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
	
	public static final String TABLE_AERONEFS = "table_aeronefs";
	//public static final String COL_ID = "ID";
	//public static final int NUM_COL_ID = 0;
	//public static final String COL_NAME = "NAME";
	//public static final int NUM_COL_NAME = 1;
	public static final String COL_TYPE = "TYPE";
	public static final int NUM_COL_TYPE = 2;
	
	
	private static final String CREATE_TABLE_VOLS = "CREATE TABLE " + TABLE_VOLS + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL, "
			+ COL_TYPE + " TEXT, "
			+ COL_MIN_VOL + " INTEGER, "
			+ COL_MIN_MOTEUR + " INTEGER, "
			+ COL_SEC_MOTEUR + " INTEGER, "
			+ COL_DATE + " TEXT NOT NULL, "
			+ COL_NOTE + " TEXT);";
	
	
	private static final String CREATE_TABLE_AERONEFS = "CREATE TABLE " + TABLE_AERONEFS + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL, "
			+ COL_TYPE + " TEXT);";
	
	public DbManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_VOLS);
		db.execSQL(CREATE_TABLE_AERONEFS);
		
		// Init values
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, "Alpina 4001");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Calmato");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values.put(DbManager.COL_NAME, "P40");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values.put(DbManager.COL_NAME, "Broussard");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values.put(DbManager.COL_NAME, "Raptor");
		values.put(DbManager.COL_TYPE, Aeronef.T_HELICO);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values.put(DbManager.COL_NAME, "Honcho");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values.put(DbManager.COL_NAME, "Savage X");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values.put(DbManager.COL_NAME, "Bullet");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		values.put(DbManager.COL_NAME, "Opale");
		values.put(DbManager.COL_TYPE, Aeronef.T_PARAMOTEUR);
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_VOLS + ";");
		db.execSQL("DROP TABLE " + TABLE_AERONEFS + ";");
		onCreate(db);
	}

}
