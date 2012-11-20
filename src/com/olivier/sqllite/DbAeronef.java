package com.olivier.sqllite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.olivier.model.Aeronef;
import com.olivier.model.Checklist;
import com.olivier.model.ChecklistItem;
import com.olivier.model.Potar;
import com.olivier.model.Radio;
import com.olivier.model.Switch;
import com.olivier.model.Vol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbAeronef {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "vols.db";
	
	private SQLiteDatabase bdd;
	private DbManager dbManager;
 
	/**
	 * On cr�e la BDD et sa table
	 * @param context {@link Context}
	 */
	public DbAeronef(Context context){
		dbManager = new DbManager(context, NOM_BDD, null, VERSION_BDD);
	}

	/**
	 * On ouvre la BDD en �criture
	 */
	public void open(){
		bdd = dbManager.getWritableDatabase();
	}
 
	/**
	 * On ferme l'acc�s � la BDD
	 */
	public void close(){
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	/**
	 * Insert new {@link Vol}
	 * @param vol the {@link Vol} to insert
	 * @return
	 */
	public long insertVol(Vol vol){
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, vol.getAeronef());
		values.put(DbManager.COL_TYPE, vol.getType());
		values.put(DbManager.COL_MIN_VOL, vol.getMinutesVol());
		values.put(DbManager.COL_MIN_MOTEUR, vol.getMinutesMoteur());
		values.put(DbManager.COL_SEC_MOTEUR, vol.getSecondsMoteur());
		values.put(DbManager.COL_NOTE, vol.getNote());
		values.put(DbManager.COL_LIEU, vol.getLieu());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
		String sDate = sdf.format(vol.getDateVol());
		values.put(DbManager.COL_DATE, sDate);
		
		return bdd.insert(DbManager.TABLE_VOLS, null, values);
	}
		
	/**
	 * Get {@link Vol} list 
	 * @return the list
	 */
	public ArrayList<Vol> getVols(){
		String orderBy = null;
		Cursor c = bdd.query(DbManager.TABLE_VOLS, new String[] {DbManager.COL_ID, 
																 DbManager.COL_NAME, 
																 DbManager.COL_TYPE,
																 DbManager.COL_MIN_VOL, 
																 DbManager.COL_MIN_MOTEUR, 
																 DbManager.COL_SEC_MOTEUR, 
																 DbManager.COL_DATE,
																 DbManager.COL_NOTE,
																 DbManager.COL_LIEU}, 
							null, null, null, null, orderBy);
		return cursorToVols(c);
	}
	
	/**
	 * Delete all {@link Vol}
	 * @return
	 */
	public long deleteVols(){
		return bdd.delete(DbManager.TABLE_VOLS, null, null);
	}
	
	/**
	 * Delete a {@link Vol}
	 * @param vol the {@link Vol} to delete
	 * @return
	 */
	public long deleteVol(Vol vol){
		return bdd.delete(DbManager.TABLE_VOLS, DbManager.COL_ID + "=" + vol.getId(), null);
	}
	
	
	/**
	 * Transform {@link Cursor} in list of {@link Vol}
	 * @param c{@link Cursor}
	 * @return the list of {@link Vol}
	 */
	private ArrayList<Vol> cursorToVols(Cursor c){
		ArrayList<Vol> vols = new ArrayList<Vol>();
		if (c.getCount() == 0) {
			return null;
		}
		while (c.moveToNext()) {
			Vol vol = new Vol();
			vol.setId(c.getInt(DbManager.NUM_COL_ID));
			vol.setAeronef(c.getString(DbManager.NUM_COL_NAME));
			vol.setType(c.getString(DbManager.NUM_COL_TYPE));
			vol.setMinutesVol(c.getInt(DbManager.NUM_COL_MIN_VOL));
			vol.setMinutesMoteur(c.getInt(DbManager.NUM_COL_MIN_MOTEUR));
			vol.setSecondsMoteur(c.getInt(DbManager.NUM_COL_SEC_MOTEUR));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
			Date dVol= new Date();
			try {
				dVol = sdf.parse(c.getString(DbManager.NUM_COL_DATE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			vol.setNote(c.getString(DbManager.NUM_COL_NOTE));
			vol.setLieu(c.getString(DbManager.NUM_COL_LIEU));
			vol.setDateVol(dVol);
			vols.add(vol);
		}
		c.close();
 		return vols;
	}
	
	/**
	 * Insert a new {@link Aeronef}
	 * @param aeronef the {@link Aeronef} to insert
	 * @return
	 */
	public long insertAeronef(Aeronef aeronef) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, aeronef.getName());
		values.put(DbManager.COL_TYPE, aeronef.getType());
		values.put(DbManager.COL_WINGSPAN, aeronef.getWingSpan());
		values.put(DbManager.COL_WEIGHT, aeronef.getWeight());
		values.put(DbManager.COL_ENGINE, aeronef.getEngine());
		values.put(DbManager.COL_FIRST_FLIGHT, aeronef.getFirstFlight());
		values.put(DbManager.COL_COMMENT, aeronef.getComment());
		
		return bdd.insert(DbManager.TABLE_AERONEFS, null, values);
	}
	
	/**
	 * Update a new {@link Aeronef}
	 * @param aeronef the {@link Aeronef} to insert
	 * @return
	 */
	public long updateAeronef(Aeronef aeronef) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(aeronef.getId())};
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, aeronef.getName());
		values.put(DbManager.COL_TYPE, aeronef.getType());
		values.put(DbManager.COL_WINGSPAN, aeronef.getWingSpan());
		values.put(DbManager.COL_WEIGHT, aeronef.getWeight());
		values.put(DbManager.COL_ENGINE, aeronef.getEngine());
		values.put(DbManager.COL_FIRST_FLIGHT, aeronef.getFirstFlight());
		values.put(DbManager.COL_COMMENT, aeronef.getComment());
		return bdd.update(DbManager.TABLE_AERONEFS, values, where, whereArgs);	
	}
	
	/**
	 * Delete a {@link Aeronef}
	 * @param aeronef the {@link Aeronef} to delete
	 * @return
	 */
	public long deleteAeronef(Aeronef aeronef) {
		return bdd.delete(DbManager.TABLE_AERONEFS, DbManager.COL_ID + "=" + aeronef.getId(), null);
	}
	
	/**
	 * Get the list of {@link Aeronef}
	 * @return the list of {@link Aeronef}
	 */
	public ArrayList<Aeronef> getAeronefs(){
		String orderBy = DbManager.COL_TYPE + " DESC";
		Cursor c = bdd.query(DbManager.TABLE_AERONEFS, new String[] {DbManager.COL_ID, 
																 DbManager.COL_NAME, 
																 DbManager.COL_TYPE,
																 DbManager.COL_WINGSPAN,
																 DbManager.COL_WEIGHT,
																 DbManager.COL_ENGINE,
																 DbManager.COL_FIRST_FLIGHT,
																 DbManager.COL_COMMENT}, 
							null, null, null, null, orderBy);
		return cursorToAeronefs(c);
	}
	
	
	/**
	 * Get {@link Aeronef} by id
	 * @return the {@link Aeronef}
	 */
	public Aeronef getAeronefById(int id) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(id)};
		Cursor c = bdd.query(DbManager.TABLE_AERONEFS, new String[] {DbManager.COL_ID, 
																 DbManager.COL_NAME, 
																 DbManager.COL_TYPE,
																 DbManager.COL_WINGSPAN,
																 DbManager.COL_WEIGHT,
																 DbManager.COL_ENGINE,
																 DbManager.COL_FIRST_FLIGHT,
																 DbManager.COL_COMMENT}, 
								where, whereArgs, null, null, null);
		if (c.getCount() == 0) {
			return null;
		}
		c.moveToNext();
		Aeronef aeronef = cursorToAeronef(c);
		c.close();
		return aeronef;
	}

	
	/**
	 * Transform {@link Cursor} in list of {@link Aeronef}
	 * @param c{@link Cursor}
	 * @return the list of {@link Aeronef}
	 */
	private ArrayList<Aeronef> cursorToAeronefs(Cursor c){
		ArrayList<Aeronef> aeronefs = new ArrayList<Aeronef>();
		if (c.getCount() == 0) {
			return null;
		}
		while (c.moveToNext()) {
			Aeronef aeronef = cursorToAeronef(c);
			if (aeronef!=null) {
				aeronefs.add(aeronef);
			}
		}
		c.close();
 		return aeronefs;
	}
	
	/**
	 * Transform {@link Cursor} in {@link Aeronef}
	 * @param c{@link Cursor}
	 * @return the {@link Aeronef}
	 */
	private Aeronef cursorToAeronef(Cursor c){
		Aeronef aeronef = new Aeronef();
		aeronef.setId(c.getInt(DbManager.NUM_COL_ID));
		aeronef.setName(c.getString(DbManager.NUM_COL_NAME));
		aeronef.setType(c.getString(DbManager.NUM_COL_TYPE));
		aeronef.setWingSpan(c.getFloat(DbManager.NUM_COL_WINGSPAN));
		aeronef.setWeight(c.getFloat(DbManager.NUM_COL_WEIGHT));
		aeronef.setEngine(c.getString(DbManager.NUM_COL_ENGINE));
		aeronef.setFirstFlight(c.getString(DbManager.NUM_COL_FIRST_FLIGHT));
		aeronef.setComment(c.getString(DbManager.NUM_COL_COMMENT));
		
 		return aeronef;
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
	
	public ArrayList<Checklist> getChecklists(String checklistName) {
		ArrayList<Checklist> checklists = new ArrayList<Checklist>(); 
		
		String where = null;
		String[] whereArgs = null;
		if (checklistName!=null && !"".equals(checklistName)) {
			where = DbManager.COL_NAME + "=?";
			whereArgs = new String[] {checklistName};
		}
		
		String orderBy = DbManager.COL_NAME + "," + DbManager.COL_ORDER;
		Cursor c = bdd.query(DbManager.TABLE_CHECKLIST, new String[] {DbManager.COL_ID, 
																		 DbManager.COL_NAME, 
																		 DbManager.COL_ACTION,
																		 DbManager.COL_ORDER}, 
						where, whereArgs, null, null, orderBy);
		
		String c_Name = null;
		Checklist c_checklist = null;
		while (c.moveToNext()) {
			int id = c.getInt(0);
			String name = c.getString(1);
			String action = c.getString(2);
			int order = c.getInt(3);
			if (c_Name == null || !c_Name.equals(name)) {
				c_Name = name;
				c_checklist = new Checklist(id, name);
				checklists.add(c_checklist);
			} 
			ChecklistItem item = new ChecklistItem(id, action, order);
			c_checklist.addItem(item);
		}
		return checklists;
	}
	
	public void deleteChecklist(Checklist checklist) {
		bdd.delete(DbManager.TABLE_CHECKLIST, DbManager.COL_NAME + "='" + checklist.getName() + "'", null);	
	}
	
	public void deleteChecklistItem(int checklistItemId) {
		bdd.delete(DbManager.TABLE_CHECKLIST, DbManager.COL_ID + "=" + checklistItemId , null);	
	}
	
	public void addCheckList(Checklist checklist) {
		for (ChecklistItem item: checklist.getItems()) {
			ContentValues values = new ContentValues();
			values.put(DbManager.COL_NAME, checklist.getName());
			values.put(DbManager.COL_ACTION, item.getAction());
			values.put(DbManager.COL_ORDER, item.getOrder());
			bdd.insert(DbManager.TABLE_CHECKLIST, null, values);	
		}
	}
	
	public void updateChecklist(Checklist checklist) {
		String where = DbManager.COL_ID + "=?";
		for (ChecklistItem item: checklist.getItems()) {
			String[] whereArgs = new String[] {String.valueOf(item.getId())};
			ContentValues values = new ContentValues();
			values.put(DbManager.COL_ACTION, item.getAction());
			values.put(DbManager.COL_ORDER, item.getOrder());
			bdd.update(DbManager.TABLE_CHECKLIST, values, where, whereArgs);
		}
			
	}
	
}
