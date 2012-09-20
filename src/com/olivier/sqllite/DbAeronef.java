package com.olivier.sqllite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.olivier.model.Aeronef;
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
	 * On crée la BDD et sa table
	 * @param context {@link Context}
	 */
	public DbAeronef(Context context){
		dbManager = new DbManager(context, NOM_BDD, null, VERSION_BDD);
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
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(DbManager.COL_NAME, vol.getAeronef());
		values.put(DbManager.COL_MIN_VOL, vol.getMinutesVol());
		values.put(DbManager.COL_MIN_MOTEUR, vol.getMinutesMoteur());
		values.put(DbManager.COL_SEC_MOTEUR, vol.getSecondsMoteur());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
		String sDate = sdf.format(vol.getDateVol());
		values.put(DbManager.COL_DATE, sDate);
		
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(DbManager.TABLE_VOLS, null, values);
	}
		
	/**
	 * Get {@link Vol} list 
	 * @return the list
	 */
	public ArrayList<Vol> getVols(){
		//Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		//String orderBy = DbManager.COL_DATE + "," + DbManager.COL_NAME;
		String orderBy = null;
		Cursor c = bdd.query(DbManager.TABLE_VOLS, new String[] {DbManager.COL_ID, 
																 DbManager.COL_NAME, 
																 DbManager.COL_MIN_VOL, 
																 DbManager.COL_MIN_MOTEUR, 
																 DbManager.COL_SEC_MOTEUR, 
																 DbManager.COL_DATE}, 
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
		return bdd.insert(DbManager.TABLE_AERONEFS, null, values);
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
																 DbManager.COL_TYPE}, 
							null, null, null, null, orderBy);
		return cursorToAeronefs(c);
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
			Aeronef aeronef = new Aeronef();
			aeronef.setId(c.getInt(DbManager.NUM_COL_ID));
			aeronef.setName(c.getString(DbManager.NUM_COL_NAME));
			aeronef.setType(c.getString(DbManager.NUM_COL_TYPE));
			aeronefs.add(aeronef);
		}
		c.close();
 		return aeronefs;
	}
	
}
