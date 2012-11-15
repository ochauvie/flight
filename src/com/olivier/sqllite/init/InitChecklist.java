package com.olivier.sqllite.init;


import com.olivier.sqllite.DbManager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class InitChecklist  {
	
	public static final void initChecklist(SQLiteDatabase db) {
		
		initBroussard(db);
		initAlpina(db);
		initArcus(db);
	}
	
	private static final void initArcus(SQLiteDatabase db) {
		// Arcus
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_ACTION, "�metteur activ�");
		values.put(DbManager.COL_ORDER, 1);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_ACTION, "Gaz au ralenti");
		values.put(DbManager.COL_ORDER, 2);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_ACTION, "Potar vario centr�");
		values.put(DbManager.COL_ORDER, 3);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_ACTION, "r�cepteur activ�");
		values.put(DbManager.COL_ORDER, 4);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
	}
	
	private static final void initAlpina(SQLiteDatabase db) {
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
		values.put(DbManager.COL_ACTION, "Potar vario centr�");
		values.put(DbManager.COL_ORDER, 3);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Alpina 4001");
		values.put(DbManager.COL_ACTION, "r�cepteur activ�");
		values.put(DbManager.COL_ORDER, 4);
		db.insert(DbManager.TABLE_CHECKLIST, null, values);
	}
	
	private static final void initBroussard(SQLiteDatabase db) {
		// Montage Broussard
		ContentValues values = new ContentValues();
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
