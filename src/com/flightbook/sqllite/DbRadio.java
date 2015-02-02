package com.flightbook.sqllite;

import java.util.ArrayList;

import com.flightbook.model.Potar;
import com.flightbook.model.Radio;
import com.flightbook.model.Switch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbRadio {
	
	private SQLiteDatabase bdd;
	private DbManager dbManager;
 
	/**
	 * On cree la BDD et sa table
	 * @param context {@link Context}
	 */
	public DbRadio(Context context){
		dbManager = new DbManager(context, DbManager.NOM_BDD, null, DbManager.VERSION_BDD);
	}

	/**
	 * On ouvre la BDD en ecriture
	 */
	public void open(){
		bdd = dbManager.getWritableDatabase();
	}
 
	/**
	 * On ferme l'acces a la BDD
	 */
	public void close(){
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}

	
	public Radio getRadioById(int id) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(id)};
		Cursor c = bdd.query(DbManager.TABLE_RADIO, new String[] {DbManager.COL_ID, DbManager.COL_NAME}, 
				where, whereArgs, null, null, null);
		if (c.getCount() == 0) {
			return null;
		}
		c.moveToNext();
		Radio radio = cursorToRadio(c);
		c.close();
		return radio;
		
	}
	
	public long addRadio(Radio radio) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, radio.getName());
		return bdd.insert(DbManager.TABLE_RADIO, null, values);
	}
	
	public long deleteRadio(Radio radio) {
		for (Switch sw:radio.getSwitchs()) {
			bdd.delete(DbManager.TABLE_SWITCH, DbManager.COL_ID + "=" + sw.getId(), null);	
		}
		for (Potar potar:radio.getPotars()) {
			bdd.delete(DbManager.TABLE_POTAR, DbManager.COL_ID + "=" + potar.getId(), null);
		}
		bdd.delete(DbManager.TABLE_RADIO, DbManager.COL_ID + "=" + radio.getId(), null);
		bdd.delete(DbManager.TABLE_RADIO_SWITCH, DbManager.COL_ID_RADIO + "=" + radio.getId(), null);
		bdd.delete(DbManager.TABLE_RADIO_POTAR, DbManager.COL_ID_RADIO + "=" + radio.getId(), null);
		return 0;
	}
	
	public ArrayList<Radio> getRadios(){
		Cursor c = bdd.query(DbManager.TABLE_RADIO, new String[] {DbManager.COL_ID,
																 DbManager.COL_NAME}, 
							null, null, null, null, null);
		return cursorToRadios(c);
	}
	
	
	public long addSwitchToRadio(int radioId, Switch sw) {
		// Add switch
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, sw.getName());
		values.put(DbManager.COL_ACTION, sw.getAction());
		values.put(DbManager.COL_UP, sw.getUp());
		values.put(DbManager.COL_CENTER, sw.getCenter());
		values.put(DbManager.COL_DOWN, sw.getDown());
		long rowid = bdd.insert(DbManager.TABLE_SWITCH, null, values);
		
		String where = "rowid=?";
		String[] whereArgs = new String[] {String.valueOf(rowid)};
		Cursor c = bdd.query(DbManager.TABLE_SWITCH, new String[] {DbManager.COL_ID}, where, whereArgs, null, null, null);
		if (c.getCount() == 0) {
			return -1;
		}
		c.moveToNext();
		int switchId = c.getInt(0);
		c.close();
		
		// Link to radio
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, radioId);
		values.put(DbManager.COL_ID_SWITCH, switchId);
		return bdd.insert(DbManager.TABLE_RADIO_SWITCH, null, values);
	}
	
	public void deleteSwitch(int switchId) {
		bdd.delete(DbManager.TABLE_SWITCH, DbManager.COL_ID + "=" + switchId, null);	
		bdd.delete(DbManager.TABLE_RADIO_SWITCH, DbManager.COL_ID_SWITCH + "=" + switchId, null);
	}
	
	public long addPotarToRadio(int radioId, Potar potar) {
		// Add potar
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, potar.getName());
		values.put(DbManager.COL_ACTION, potar.getAction());
		values.put(DbManager.COL_UP, potar.getUp());
		values.put(DbManager.COL_CENTER, potar.getCenter());
		values.put(DbManager.COL_DOWN, potar.getDown());
		long rowid = bdd.insert(DbManager.TABLE_POTAR, null, values);
		
		String where = "rowid=?";
		String[] whereArgs = new String[] {String.valueOf(rowid)};
		Cursor c = bdd.query(DbManager.TABLE_POTAR, new String[] {DbManager.COL_ID}, where, whereArgs, null, null, null);
		if (c.getCount() == 0) {
			return -1;
		}
		c.moveToNext();
		int potarId = c.getInt(0);
		c.close();
		
		// Link to radio
		values = new ContentValues();
		values.put(DbManager.COL_ID_RADIO, radioId);
		values.put(DbManager.COL_ID_POTAR, potarId);
		return bdd.insert(DbManager.TABLE_RADIO_POTAR, null, values);
	}
	
	public void deletePotar(int potarId) {
		bdd.delete(DbManager.TABLE_POTAR, DbManager.COL_ID + "=" + potarId, null);	
		bdd.delete(DbManager.TABLE_RADIO_POTAR, DbManager.COL_ID_POTAR + "=" + potarId, null);
	}
	
	private ArrayList<Radio> cursorToRadios(Cursor c){
		ArrayList<Radio> radios = new ArrayList<Radio>();
		if (c.getCount() == 0) {
			return null;
		}
		while (c.moveToNext()) {
			Radio radio = cursorToRadio(c);
			if (radio!=null) {
				radios.add(radio);
			}
		}
		c.close();
 		return radios;
	}
	
	private Radio cursorToRadio(Cursor c){
		Radio radio = new Radio();
		radio.setId(c.getInt(DbManager.NUM_COL_ID));
		radio.setName(c.getString(DbManager.NUM_COL_NAME));
		
		// Switch
		String switchQuery = "SELECT " + DbManager.COL_ID + ","
				 						+ DbManager.COL_NAME + "," 
				 						+ DbManager.COL_UP + ","
				 						+ DbManager.COL_CENTER + ","
				 						+ DbManager.COL_DOWN + ","
				 						+ DbManager.COL_ACTION 
				 			 + " FROM " + DbManager.TABLE_RADIO_SWITCH + " t1, " + DbManager.TABLE_SWITCH + " t2 "
		 					 + " WHERE t1." + DbManager.COL_ID_SWITCH + "=t2." + DbManager.COL_ID 
		 					 + " AND t1." + DbManager.COL_ID_RADIO + "=" + radio.getId();
	     Cursor cursorSwitch = bdd.rawQuery(switchQuery, null);
	     while (cursorSwitch.moveToNext()) {
	    	 Switch sw = new Switch();
	    	 sw.setId(cursorSwitch.getInt(0));
	    	 sw.setName(cursorSwitch.getString(1));
	    	 sw.setUp(cursorSwitch.getString(2));
	    	 sw.setCenter(cursorSwitch.getString(3));
	    	 sw.setDown(cursorSwitch.getString(4));
	    	 sw.setAction(cursorSwitch.getString(5));
	    	 radio.addSwitch(sw);
	     }
	     cursorSwitch.close();
		
	    // Potar
	     String potarQuery = "SELECT " + DbManager.COL_ID + ","
					+ DbManager.COL_NAME + "," 
					+ DbManager.COL_UP + ","
					+ DbManager.COL_CENTER + ","
					+ DbManager.COL_DOWN + ","
					+ DbManager.COL_ACTION 
		 + " FROM " + DbManager.TABLE_RADIO_POTAR + " t1, " + DbManager.TABLE_POTAR + " t2 "
		 + " WHERE t1." + DbManager.COL_ID_POTAR + "=t2." + DbManager.COL_ID 
		 + " AND t1." + DbManager.COL_ID_RADIO + "=" + radio.getId();
		Cursor cursorPotar = bdd.rawQuery(potarQuery, null);
		while (cursorPotar.moveToNext()) {
			Potar potar = new Potar();
			potar.setId(cursorPotar.getInt(0));
			potar.setName(cursorPotar.getString(1));
			potar.setUp(cursorPotar.getString(2));
			potar.setCenter(cursorPotar.getString(3));
			potar.setDown(cursorPotar.getString(4));
			potar.setAction(cursorPotar.getString(5));
			radio.addPotar(potar);
		}
		cursorPotar.close();

		return radio;
	}
	
}
