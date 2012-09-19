package com.olivier.sqllite;

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
	public static final int NUM_COL_MIN_VOL = 2;
	public static final String COL_MIN_MOTEUR = "MIN_MOT";
	public static final int NUM_COL_MIN_MOTEUR = 3;
	public static final String COL_SEC_MOTEUR = "SEC_MOT";
	public static final int NUM_COL_SEC_MOTEUR = 4;
	public static final String COL_DATE = "DATE_VOL";
	public static final int NUM_COL_DATE = 5;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_VOLS + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL, "
			+ COL_MIN_VOL + " INTEGER, "
			+ COL_MIN_MOTEUR + " INTEGER, "
			+ COL_SEC_MOTEUR + " INTEGER, "
			+ COL_DATE + " TEXT NOT NULL);";
	
	public DbManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_VOLS + ";");
		onCreate(db);
	}

}
