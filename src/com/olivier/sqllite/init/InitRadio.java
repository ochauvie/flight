package com.olivier.sqllite.init;


import com.olivier.sqllite.DbManager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class InitRadio  {
	
	public static final void initRadio(SQLiteDatabase db) {
		initAlpina(db);
		initBroussard(db);
		initSpatz(db);
		initParamoteur(db);
	}
	
	
	private static final void initAlpina(SQLiteDatabase db) {

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
		values.put(DbManager.COL_UP, "Arrêt moteur");
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
	
	private static final void initBroussard(SQLiteDatabase db) {

		// Switch
		ContentValues values = new ContentValues();
		values = new ContentValues();
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
		values.put(DbManager.COL_UP, "Stop");
		values.put(DbManager.COL_DOWN, "Actif");
		values.put(DbManager.COL_ACTION, "Chronomètre");
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

	private static final void initSpatz(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		// Potar
		values = new ContentValues();
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
	
	private static final void initParamoteur(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		// Switch
		values = new ContentValues();
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
			
}
