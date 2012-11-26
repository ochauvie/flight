package com.olivier.sqllite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.olivier.model.Vol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbVol {
	
	private SQLiteDatabase bdd;
	private DbManager dbManager;
 
	/**
	 * On crée la BDD et sa table
	 * @param context {@link Context}
	 */
	public DbVol(Context context){
		dbManager = new DbManager(context, DbManager.NOM_BDD, null, DbManager.VERSION_BDD);
	}

	/**
	 * On ouvre la BDD en écriture
	 */
	public void open(){
		bdd = dbManager.getWritableDatabase();
	}
 
	/**
	 * On ferme l'accès à la BDD
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
	
	
}
