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
		
		initHangar(db);
		initRadio(db);
		initChecklist(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_VOLS + ";");
		db.execSQL("DROP TABLE " + TABLE_AERONEFS + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_RADIO_SWITCH + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_RADIO_POTAR + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_RADIO + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_POTAR + ";");
		db.execSQL("DROP TABLE " + CREATE_TABLE_SWITCH + ";");
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
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 2);
		values.put(DbManager.COL_WEIGHT, 1.5);
		values.put(DbManager.COL_ENGINE, "Protronik DM2615-1100");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Spatz 55");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 2);
		values.put(DbManager.COL_ENGINE, "Protronik DM2810-800");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Calmato");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_ENGINE, "OS 46 LA");
		values.put(DbManager.COL_FIRST_FLIGHT, "29/10/2011");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "P40");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_ENGINE, "OS 46 LA");
		values.put(DbManager.COL_FIRST_FLIGHT, "22/03/2003");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Broussard");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_WINGSPAN, 2.8);
		values.put(DbManager.COL_WEIGHT, 11);
		values.put(DbManager.COL_ENGINE, "Zenoah 45 PCI");
		values.put(DbManager.COL_FIRST_FLIGHT, "16/04/2011");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Raptor 30");
		values.put(DbManager.COL_TYPE, Aeronef.T_HELICO);
		values.put(DbManager.COL_ENGINE, "OS 32");
		values.put(DbManager.COL_FIRST_FLIGHT, "06/07/2002");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Honcho scx10");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_ENGINE, "Axial 55T");
		values.put(DbManager.COL_FIRST_FLIGHT, "20/08/2011");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Savage X");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_ENGINE, "HPI 4.1");
		values.put(DbManager.COL_FIRST_FLIGHT, "02/08/2007");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Bullet flux");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_FIRST_FLIGHT, "10/09/2010");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Spiral 1.2");
		values.put(DbManager.COL_TYPE, Aeronef.T_PARAMOTEUR);
		values.put(DbManager.COL_ENGINE, "Protronik DM2810-1200");
		values.put(DbManager.COL_FIRST_FLIGHT, "10/04/2010");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
	}
	
	private void initRadio(SQLiteDatabase db) {
		 // APLINA 4001
		
		// Switch
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_ID, 1);
		values.put(DbManager.COL_NAME, "A");
		values.put(DbManager.COL_UP, "Inactif + Stop chronom�tre");
		values.put(DbManager.COL_DOWN, "Actif + Start chronom�tre");
		values.put(DbManager.COL_ACTION, "Mixage butterfly + chronom�tre vol");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 2);
		values.put(DbManager.COL_NAME, "C");
		values.put(DbManager.COL_UP, "N�gatif");
		values.put(DbManager.COL_CENTER, "Neutre");
		values.put(DbManager.COL_DOWN, "Positif");
		values.put(DbManager.COL_ACTION, "Volet courbure");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 3);
		values.put(DbManager.COL_NAME, "D");
		values.put(DbManager.COL_UP, "Arr�t moteur");
		values.put(DbManager.COL_DOWN, "Moteur plein gaz");
		values.put(DbManager.COL_ACTION, "Moteur");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 4);
		values.put(DbManager.COL_NAME, "G");
		values.put(DbManager.COL_UP, "Actif");
		values.put(DbManager.COL_DOWN, "Inactif");
		values.put(DbManager.COL_ACTION, "Mixage ailerons -> volets");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		
		// Potar
		values = new ContentValues();
		values.put(DbManager.COL_ID, 1);
		values.put(DbManager.COL_NAME, "C�t� droit");
		values.put(DbManager.COL_UP, "Temp�rature");
		values.put(DbManager.COL_CENTER, "Variom�tre, Altim�tre");
		values.put(DbManager.COL_DOWN, "R�glage, Variom�tre, Altim�tre");
		values.put(DbManager.COL_ACTION, "Variom�tre");
		db.insert(DbManager.TABLE_POTAR, null, values);
		
		// Radio
		values = new ContentValues();
		values.put(DbManager.COL_ID, 1);
		values.put(DbManager.COL_NAME, "FF9 Alpina 4001");
		db.insert(DbManager.TABLE_RADIO, null, values);
		
		// Radio - switch
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 1);
		values.put(DbManager.COL_ID_SWITCH, 1);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 1);
		values.put(DbManager.COL_ID_SWITCH, 2);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 1);
		values.put(DbManager.COL_ID_SWITCH, 3);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 1);
		values.put(DbManager.COL_ID_SWITCH, 4);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		// Radio - potar
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 1);
		values.put(DbManager.COL_ID_POTAR, 1);
		db.insert(DbManager.TABLE_RADIO_POTAR, null, values);
	
		// Broussard
		
		// Switch
				values = new ContentValues();
				values.put(DbManager.COL_ID, 6);
				values.put(DbManager.COL_NAME, "C");
				values.put(DbManager.COL_UP, "Rentr�s");
				values.put(DbManager.COL_CENTER, "Sortis 50%");
				values.put(DbManager.COL_DOWN, "Sortis 100%");
				values.put(DbManager.COL_ACTION, "Volets");
				db.insert(DbManager.TABLE_SWITCH, null, values);
				
				values = new ContentValues();
				values.put(DbManager.COL_ID, 7);
				values.put(DbManager.COL_NAME, "D");
				values.put(DbManager.COL_UP, "Stop");
				values.put(DbManager.COL_DOWN, "Actif");
				values.put(DbManager.COL_ACTION, "Chronom�tre");
				db.insert(DbManager.TABLE_SWITCH, null, values);
				
				// Radio
				values = new ContentValues();
				values.put(DbManager.COL_ID, 2);
				values.put(DbManager.COL_NAME, "FF9 Broussard");
				db.insert(DbManager.TABLE_RADIO, null, values);
				
				// Radio - switch
				values = new ContentValues();
				values.put(DbManager.COL_ID_RADIO, 2);
				values.put(DbManager.COL_ID_SWITCH, 6);
				db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
				
				values = new ContentValues();
				values.put(DbManager.COL_ID_RADIO, 2);
				values.put(DbManager.COL_ID_SWITCH, 7);
				db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);

			// Spatz
			
				// Potar
				values = new ContentValues();
				values.put(DbManager.COL_ID, 2);
				values.put(DbManager.COL_NAME, "C�t� gauche");
				values.put(DbManager.COL_UP, "Sortis");
				values.put(DbManager.COL_DOWN, "Rentr�s");
				values.put(DbManager.COL_ACTION, "AF");
				db.insert(DbManager.TABLE_POTAR, null, values);
					
				// Radio
				values = new ContentValues();
				values.put(DbManager.COL_ID, 3);
				values.put(DbManager.COL_NAME, "FF9 Spatz 55");
				db.insert(DbManager.TABLE_RADIO, null, values);
				
				// Radio - potar
				values = new ContentValues();
				values.put(DbManager.COL_ID_RADIO, 3);
				values.put(DbManager.COL_ID_POTAR, 2);
				db.insert(DbManager.TABLE_RADIO_POTAR, null, values);
	
		
	}

	
	private void initChecklist(SQLiteDatabase db) {
		// Alpina 4001
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, "Alpina 4001");
		values.put(DbManager.COL_ACTION, "�metteur activ�");
		values.put(DbManager.COL_ORDER, 1);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Alpina 4001");
		values.put(DbManager.COL_ACTION, "inter moteur d�sactiv�");
		values.put(DbManager.COL_ORDER, 2);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Alpina 4001");
		values.put(DbManager.COL_ACTION, "r�cepteur activ�");
		values.put(DbManager.COL_ORDER, 3);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		// Montage Broussard
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Branchement servos aile droite");
		values.put(DbManager.COL_ORDER, 1);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Branchement servos aile gauche");
		values.put(DbManager.COL_ORDER, 2);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Tendeur maintien aile");
		values.put(DbManager.COL_ORDER, 3);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Hauban droit");
		values.put(DbManager.COL_ORDER, 4);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Hauban gauche");
		values.put(DbManager.COL_ORDER, 5);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Branchement prise empennage");
		values.put(DbManager.COL_ORDER, 6);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "S�curisation prise empennage");
		values.put(DbManager.COL_ORDER, 7);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Vis empennage");
		values.put(DbManager.COL_ORDER, 8);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "Capuchon bougie");
		values.put(DbManager.COL_ORDER, 9);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "Montage Broussard");
		values.put(DbManager.COL_ACTION, "H�lice serr�e");
		values.put(DbManager.COL_ORDER, 10);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		// V�rification Broussard
		values.put(DbManager.COL_NAME, "V�rification Broussard");
		values.put(DbManager.COL_ACTION, "Allumage d�sactiv�");
		values.put(DbManager.COL_ORDER, 1);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "V�rification Broussard");
		values.put(DbManager.COL_ACTION, "Batteries 1 et 2 activ�es");
		values.put(DbManager.COL_ORDER, 1);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "V�rification Broussard");
		values.put(DbManager.COL_ACTION, "Tension r�ception v�rifi�e");
		values.put(DbManager.COL_ORDER, 2);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "V�rification Broussard");
		values.put(DbManager.COL_ACTION, "Tension allumage v�rifi�e");
		values.put(DbManager.COL_ORDER, 3);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "V�rification Broussard");
		values.put(DbManager.COL_ACTION, "Fermeture portes");
		values.put(DbManager.COL_ORDER, 4);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "V�rification Broussard");
		values.put(DbManager.COL_ACTION, "Sens et D�battements gouvernes");
		values.put(DbManager.COL_ORDER, 5);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		// D�marrage Broussard
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Remplissage r�servoir");
		values.put(DbManager.COL_ORDER, 1);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Immobilisation de l'avion");
		values.put(DbManager.COL_ORDER, 2);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Batteries 1 et 2 activ�es");
		values.put(DbManager.COL_ORDER, 3);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Gaz au ralenti");
		values.put(DbManager.COL_ORDER, 4);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Fermeture starter");
		values.put(DbManager.COL_ORDER, 5);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Allumage activ� S�cu 1 minute");
		values.put(DbManager.COL_ORDER, 6);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Brassage h�lice");
		values.put(DbManager.COL_ORDER, 7);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Ouverture starter");
		values.put(DbManager.COL_ORDER, 8);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Gaz au ralenti");
		values.put(DbManager.COL_ORDER, 9);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values.put(DbManager.COL_NAME, "D�marrage Broussard");
		values.put(DbManager.COL_ACTION, "Lancer l'h�lice");
		values.put(DbManager.COL_ORDER, 10);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
	}
		 
		
}
