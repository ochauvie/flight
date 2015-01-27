package com.flightbook.sqllite.init;


import com.flightbook.sqllite.DbManager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class InitRadio  {
	
	public static void initRadio(SQLiteDatabase db) {
		initAlpina(db);
		initBroussard(db);
		initSpatz(db);
		initParamoteur(db);
		initArducopter(db);
		initRaptor(db);
        initTrex450(db);
	}
	
	
	private static void initAlpina(SQLiteDatabase db) {

		// Switch
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_ID, 1);
		values.put(DbManager.COL_NAME, "A");
		values.put(DbManager.COL_UP, "Inactif + Stop chronomètre");
		values.put(DbManager.COL_DOWN, "Actif + Start chronomètre");
		values.put(DbManager.COL_ACTION, "Mixage butterfly + chronomètre vol");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 2);
		values.put(DbManager.COL_NAME, "C");
		values.put(DbManager.COL_UP, "Négatif");
		values.put(DbManager.COL_CENTER, "Neutre");
		values.put(DbManager.COL_DOWN, "Positif");
		values.put(DbManager.COL_ACTION, "Volet courbure");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 3);
		values.put(DbManager.COL_NAME, "D");
		values.put(DbManager.COL_UP, "Arrét moteur");
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
		values.put(DbManager.COL_NAME, "Côté droit");
		values.put(DbManager.COL_UP, "Température");
		values.put(DbManager.COL_CENTER, "Variomètre, Altimètre");
		values.put(DbManager.COL_DOWN, "Réglage, Variomètre, Altimètre");
		values.put(DbManager.COL_ACTION, "Variomètre");
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
	}
	
	private static void initBroussard(SQLiteDatabase db) {

		// Switch
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_ID, 6);
		values.put(DbManager.COL_NAME, "C");
		values.put(DbManager.COL_UP, "Rentrés");
		values.put(DbManager.COL_CENTER, "Sortis 50%");
		values.put(DbManager.COL_DOWN, "Sortis 100%");
		values.put(DbManager.COL_ACTION, "Volets");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 7);
		values.put(DbManager.COL_NAME, "D");
		values.put(DbManager.COL_UP, "Allumage ON");
		values.put(DbManager.COL_DOWN, "Allumage OFF");
		values.put(DbManager.COL_ACTION, "Kill switch");
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
	}

	private static void initSpatz(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		// Potar
		values.put(DbManager.COL_ID, 2);
		values.put(DbManager.COL_NAME, "Côté gauche");
		values.put(DbManager.COL_UP, "Sortis");
		values.put(DbManager.COL_DOWN, "Rentrés");
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
	
	private static void initParamoteur(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		// Switch
		values.put(DbManager.COL_ID, 8);
		values.put(DbManager.COL_NAME, "D");
		values.put(DbManager.COL_UP, "ON");
		values.put(DbManager.COL_DOWN, "OFF");
		values.put(DbManager.COL_ACTION, "Chronomètre");
		db.insert(DbManager.TABLE_SWITCH, null, values);
			
		// Radio
		values = new ContentValues();
		values.put(DbManager.COL_ID, 4);
		values.put(DbManager.COL_NAME, "FF9 Paramoteur");
		db.insert(DbManager.TABLE_RADIO, null, values);
		
		// Radio - switch
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 4);
		values.put(DbManager.COL_ID_SWITCH, 8);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
	}

	private static void initArducopter(SQLiteDatabase db) {
		// Switch
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_ID, 9);
		values.put(DbManager.COL_NAME, "E");
		values.put(DbManager.COL_UP, "Loiter (G down) - Auto (G up)");
		values.put(DbManager.COL_CENTER, "Halt hold (G down) - RTL (G up)");
		values.put(DbManager.COL_DOWN, "Stabilize");
		values.put(DbManager.COL_ACTION, "Mode");
		db.insert(DbManager.TABLE_SWITCH, null, values);

		values = new ContentValues();
		values.put(DbManager.COL_ID, 10);
		values.put(DbManager.COL_NAME, "G");
		values.put(DbManager.COL_UP, "Action UP sur switch E");
		values.put(DbManager.COL_DOWN, "Action DOWN sur switch E");
		values.put(DbManager.COL_ACTION, "Switch mode sur E");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 11);
		values.put(DbManager.COL_NAME, "A");
		values.put(DbManager.COL_UP, "Stop");
		values.put(DbManager.COL_DOWN, "Start");
		values.put(DbManager.COL_ACTION, "Chronomètre");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 12);
		values.put(DbManager.COL_NAME, "H");
		values.put(DbManager.COL_UP, "-");
		values.put(DbManager.COL_DOWN, "Save");
		values.put(DbManager.COL_ACTION, "Save Trim");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 13);
		values.put(DbManager.COL_NAME, "F");
		values.put(DbManager.COL_UP, "Normal");
		values.put(DbManager.COL_DOWN, "Super simple (gps locked)");
		values.put(DbManager.COL_ACTION, "Assisatnt mode de vol");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		// Radio
		values = new ContentValues();
		values.put(DbManager.COL_ID, 5);
		values.put(DbManager.COL_NAME, "FF9 Arducopter");
		db.insert(DbManager.TABLE_RADIO, null, values);
		
		// Radio - switch
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 5);
		values.put(DbManager.COL_ID_SWITCH, 9);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 5);
		values.put(DbManager.COL_ID_SWITCH, 10);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 5);
		values.put(DbManager.COL_ID_SWITCH, 11);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 5);
		values.put(DbManager.COL_ID_SWITCH, 12);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 5);
		values.put(DbManager.COL_ID_SWITCH, 13);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);

	}

	private static void initRaptor(SQLiteDatabase db) {
		// Switch
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_ID, 14);
		values.put(DbManager.COL_NAME, "F");
		values.put(DbManager.COL_UP, "Normal");
		values.put(DbManager.COL_DOWN, "Conservateur de cap");
		values.put(DbManager.COL_ACTION, "Mode gyroscope");
		db.insert(DbManager.TABLE_SWITCH, null, values);

		values = new ContentValues();
		values.put(DbManager.COL_ID, 15);
		values.put(DbManager.COL_NAME, "E");
		values.put(DbManager.COL_UP, "Linéaire");
		values.put(DbManager.COL_CENTER, "Idle up 1");
		values.put(DbManager.COL_DOWN, "Idle up 2");
		values.put(DbManager.COL_ACTION, "Idle UP");
		db.insert(DbManager.TABLE_SWITCH, null, values);

		values = new ContentValues();
		values.put(DbManager.COL_ID, 16);
		values.put(DbManager.COL_NAME, "G");
		values.put(DbManager.COL_UP, "Normal");
		values.put(DbManager.COL_DOWN, "Ralenti moteur");
		values.put(DbManager.COL_ACTION, "Autorotation");
		db.insert(DbManager.TABLE_SWITCH, null, values);
		
		// Potar
		values = new ContentValues();
		values.put(DbManager.COL_ID, 3);
		values.put(DbManager.COL_NAME, "A");
		values.put(DbManager.COL_UP, "+");
		values.put(DbManager.COL_DOWN, "-");
		values.put(DbManager.COL_ACTION, "Pas stationnaire");
		db.insert(DbManager.TABLE_POTAR, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID, 4);
		values.put(DbManager.COL_NAME, "C");
		values.put(DbManager.COL_UP, "+");
		values.put(DbManager.COL_DOWN, "-");
		values.put(DbManager.COL_ACTION, "Gaz stationnaire");
		db.insert(DbManager.TABLE_POTAR, null, values);
		
		
		// Radio
		values = new ContentValues();
		values.put(DbManager.COL_ID, 6);
		values.put(DbManager.COL_NAME, "FF9 Raptor 30");
		db.insert(DbManager.TABLE_RADIO, null, values);
		
		// Radio - switch
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 6);
		values.put(DbManager.COL_ID_SWITCH, 14);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 6);
		values.put(DbManager.COL_ID_SWITCH, 15);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 6);
		values.put(DbManager.COL_ID_SWITCH, 16);
		db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
		
		// Radio - potar
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 6);
		values.put(DbManager.COL_ID_POTAR, 3);
		db.insert(DbManager.TABLE_RADIO_POTAR, null, values);
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, 6);
		values.put(DbManager.COL_ID_POTAR, 4);
		db.insert(DbManager.TABLE_RADIO_POTAR, null, values);
		
	}

    private static void initTrex450(SQLiteDatabase db) {
        // Switch
        ContentValues values = new ContentValues();
        values.put(DbManager.COL_ID, 17);
        values.put(DbManager.COL_NAME, "F");
        values.put(DbManager.COL_UP, "Normal");
        values.put(DbManager.COL_DOWN, "Conservateur de cap");
        values.put(DbManager.COL_ACTION, "Mode gyroscope");
        db.insert(DbManager.TABLE_SWITCH, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_ID, 18);
        values.put(DbManager.COL_NAME, "E");
        values.put(DbManager.COL_UP, "Linéaire");
        values.put(DbManager.COL_CENTER, "Idle up 1");
        values.put(DbManager.COL_DOWN, "Idle up 2");
        values.put(DbManager.COL_ACTION, "Idle UP");
        db.insert(DbManager.TABLE_SWITCH, null, values);


        // Potar
        values = new ContentValues();
        values.put(DbManager.COL_ID, 5);
        values.put(DbManager.COL_NAME, "A");
        values.put(DbManager.COL_UP, "+");
        values.put(DbManager.COL_DOWN, "-");
        values.put(DbManager.COL_ACTION, "Pas stationnaire");
        db.insert(DbManager.TABLE_POTAR, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_ID, 6);
        values.put(DbManager.COL_NAME, "C");
        values.put(DbManager.COL_UP, "+");
        values.put(DbManager.COL_DOWN, "-");
        values.put(DbManager.COL_ACTION, "Gaz stationnaire");
        db.insert(DbManager.TABLE_POTAR, null, values);


        // Radio
        values = new ContentValues();
        values.put(DbManager.COL_ID, 7);
        values.put(DbManager.COL_NAME, "FF9 Trex 450");
        db.insert(DbManager.TABLE_RADIO, null, values);

        // Radio - switch
        values = new ContentValues();
        values.put(DbManager.COL_ID_RADIO, 7);
        values.put(DbManager.COL_ID_SWITCH, 17);
        db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_ID_RADIO, 7);
        values.put(DbManager.COL_ID_SWITCH, 18);
        db.insert(DbManager.TABLE_RADIO_SWITCH, null, values);


        // Radio - potar
        values = new ContentValues();
        values.put(DbManager.COL_ID_RADIO, 7);
        values.put(DbManager.COL_ID_POTAR, 5);
        db.insert(DbManager.TABLE_RADIO_POTAR, null, values);
        values = new ContentValues();
        values.put(DbManager.COL_ID_RADIO, 7);
        values.put(DbManager.COL_ID_POTAR, 6);
        db.insert(DbManager.TABLE_RADIO_POTAR, null, values);

    }

}
