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
	
	public long deleteVols(){
		return bdd.delete(DbManager.TABLE_VOLS, null, null);
	}
	
	public long deleteVol(Vol vol){
		return bdd.delete(DbManager.TABLE_VOLS, DbManager.COL_ID + "=" + vol.getId(), null);
	}
	
	
	//Cette méthode permet de convertir un cursor en un vol
	private ArrayList<Vol> cursorToVols(Cursor c){
		ArrayList<Vol> vols = new ArrayList<Vol>();
		if (c.getCount() == 0) {
			return null;
		}
		c.moveToFirst();
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
	
}
