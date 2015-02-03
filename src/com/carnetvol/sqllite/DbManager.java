package com.carnetvol.sqllite;


import com.carnetvol.sqllite.init.InitAccus;
import com.carnetvol.sqllite.init.InitChecklist;
import com.carnetvol.sqllite.init.InitHangar;
import com.carnetvol.sqllite.init.InitRadio;
import com.carnetvol.sqllite.init.InitSites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper {

	public static final int VERSION_BDD = 1;
	public static final String NOM_BDD = "vols.db";
	
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
    public static final String COL_ID_ACCU_PROPULSION = "ACCU_PROPULSION";
    public static final int NUM_COL_ID_ACCU_PROPULSION = 9;
	
	public static final String TABLE_AERONEFS = "table_aeronefs";
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
    public static final String COL_DEFAULT = "IS_DEFAULT";
    public static final int NUM_COL_SITE_DEFAULT = 3;

    public static final int NUM_COL_SITE_COMMENT = 2;

    public static final String TABLE_ACCUS = "table_accus";
    public static final String COL_ACCU_TYPE = "TYPE";
        public static final int NUM_COL_ACCU_TYPE = 1;
    public static final String COL_ACCU_ELEMENTS = "ELEMENTS";
        public static final int NUM_COL_ACCU_ELEMENTS = 2;
    public static final String COL_ACCU_CAPACITE = "CAPACITE";
        public static final int NUM_COL_ACCU_CAPACITE = 3;
    public static final String COL_ACCU_TAUX_DECHARGE = "TAUX_DECHARGE";
        public static final int NUM_COL_ACCU_TAUX_DECHARGE = 4;
    public static final String COL_ACCU_NUMERO = "NUMERO";
        public static final int NUM_COL_ACCU_NUMERO = 5;
    public static final String COL_ACCU_NOM = "NOM";
        public static final int NUM_COL_ACCU_NOM = 6;
    public static final String COL_ACCU_MARQUE = "MARQUE";
        public static final int NUM_COL_ACCU_MARQUE = 7;
    public static final String COL_ACCU_DATE_ACHAT = "DATE_ACHAT";
        public static final int NUM_COL_ACCU_DATE_ACHAT = 8;
    public static final String COL_ACCU_CYCLES = "CYCLES";
        public static final int NUM_COL_ACCU_CYCLES = 9;
    public static final String COL_ACCU_VOLTAGE = "VOLTAGE";
        public static final int NUM_COL_ACCU_VOLTAGE = 10;



    private static final String CREATE_TABLE_VOLS = "CREATE TABLE " + TABLE_VOLS + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL, "
			+ COL_TYPE + " TEXT, "
			+ COL_MIN_VOL + " INTEGER, "
			+ COL_MIN_MOTEUR + " INTEGER, "
			+ COL_SEC_MOTEUR + " INTEGER, "
			+ COL_DATE + " TEXT NOT NULL, "
			+ COL_NOTE + " TEXT, "
			+ COL_LIEU + " TEXT, "
            + COL_ID_ACCU_PROPULSION + " INTEGER);";

	
	private static final String CREATE_TABLE_AERONEFS = "CREATE TABLE " + TABLE_AERONEFS + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL, "
			+ COL_TYPE + " TEXT, "
			+ COL_WINGSPAN + " REAL, "
			+ COL_WEIGHT + " REAL, "
			+ COL_ENGINE + " TEXT, "
			+ COL_FIRST_FLIGHT + " TEXT, "
			+ COL_COMMENT + " TEXT);";
	
	public static final String TABLE_SWITCH = "table_switch";
	public static final String COL_UP = "UP";
	public static final String COL_CENTER = "CENTER";
	public static final String COL_DOWN = "DOWN";
	public static final String COL_ACTION = "ACTION";
	
	private static final String CREATE_TABLE_SWITCH = "CREATE TABLE " + TABLE_SWITCH + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_NAME + " TEXT NOT NULL," 
			+ COL_UP + " TEXT, "  
			+ COL_CENTER + " TEXT, " 
			+ COL_DOWN + " TEXT, " 
			+ COL_ACTION + " TEXT);";
	
	public static final String TABLE_POTAR = "table_potar";
	private static final String CREATE_TABLE_POTAR = "CREATE TABLE " + TABLE_POTAR + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_NAME + " TEXT NOT NULL," 
			+ COL_UP + " TEXT, "  
			+ COL_CENTER + " TEXT, "
			+ COL_DOWN + " TEXT, " 
			+ COL_ACTION + " TEXT);";
	
	public static final String TABLE_RADIO = "table_radio";
	private static final String CREATE_TABLE_RADIO = "CREATE TABLE " + TABLE_RADIO + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME + " TEXT NOT NULL);";
	
	public static final String TABLE_RADIO_SWITCH = "table_radio_switch";
	public static final String COL_ID_RADIO = "ID_RADIO";
	public static final String COL_ID_SWITCH = "ID_SWITCH";
	private static final String CREATE_TABLE_RADIO_SWITCH = "CREATE TABLE " + TABLE_RADIO_SWITCH + " ("
			+ COL_ID_RADIO + " INTEGER  NOT NULL, " 
			+ COL_ID_SWITCH + " INTEGER NOT NULL);";
	
	public static final String TABLE_RADIO_POTAR = "table_radio_potar";
	public static final String COL_ID_POTAR = "ID_POTAR";
	private static final String CREATE_TABLE_RADIO_POTAR = "CREATE TABLE " + TABLE_RADIO_POTAR + " ("
			+ COL_ID_RADIO + " INTEGER  NOT NULL, " 
			+ COL_ID_POTAR + " INTEGER NOT NULL);";
	
	
	public static final String TABLE_CHECKLIST = "table_checklist";
	public static final String COL_ORDER = "TRI";
	private static final String CREATE_TABLE_CHECKLIST = "CREATE TABLE " + TABLE_CHECKLIST + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_NAME + " TEXT  NOT NULL, "
			+ COL_ACTION + " TEXT , "
			+ COL_ORDER + " INTEGER NOT NULL);";

    public static final String TABLE_SITES = "table_sites";


    private static final String CREATE_TABLE_SITES = "CREATE TABLE " + TABLE_SITES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT NOT NULL, "
            + COL_COMMENT + " TEXT,"
            + COL_DEFAULT + " INTEGER);";

    private static final String CREATE_TABLE_ACCUS = "CREATE TABLE " + TABLE_ACCUS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ACCU_TYPE + " TEXT NOT NULL, "
            + COL_ACCU_ELEMENTS + " INTEGER,"
            + COL_ACCU_CAPACITE + " INTEGER,"
            + COL_ACCU_TAUX_DECHARGE + " INTEGER,"
            + COL_ACCU_NUMERO + " INTEGER,"
            + COL_ACCU_NOM + " TEXT,"
            + COL_ACCU_MARQUE + " TEXT,"
            + COL_ACCU_DATE_ACHAT + " TEXT,"
            + COL_ACCU_CYCLES + " INTEGER,"
            + COL_ACCU_VOLTAGE + " REAL);";


    public DbManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_VOLS);
		db.execSQL(CREATE_TABLE_AERONEFS);
		
		db.execSQL(CREATE_TABLE_SWITCH);
		db.execSQL(CREATE_TABLE_POTAR);
		db.execSQL(CREATE_TABLE_RADIO);
		db.execSQL(CREATE_TABLE_RADIO_SWITCH);
		db.execSQL(CREATE_TABLE_RADIO_POTAR);
		
		db.execSQL(CREATE_TABLE_CHECKLIST);

        db.execSQL(CREATE_TABLE_SITES);

        db.execSQL(CREATE_TABLE_ACCUS);
		
		InitHangar.initHangar(db);
		InitRadio.initRadio(db);
		InitChecklist.initChecklist(db);
        InitSites.initSites(db);
        InitAccus.initAccus(db);
	}
		
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        db.execSQL("DROP TABLE " + TABLE_VOLS + ";");
		db.execSQL("DROP TABLE " + TABLE_AERONEFS + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_RADIO_SWITCH + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_RADIO_POTAR + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_RADIO + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_POTAR + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_SWITCH + ";");
        db.execSQL("DROP TABLE " + CREATE_TABLE_SITES + ";");
        db.execSQL("DROP TABLE " + CREATE_TABLE_ACCUS + ";");
        */


       // db.execSQL("ALTER TABLE " + TABLE_SITES + " ADD COLUMN " + COL_DEFAULT + " INTEGER;");

        //db.execSQL(CREATE_TABLE_ACCUS);
        //db.execSQL("ALTER TABLE " + TABLE_VOLS + " ADD COLUMN " + COL_ID_ACCU_PROPULSION + " INTEGER;");



	}
	
			
}
