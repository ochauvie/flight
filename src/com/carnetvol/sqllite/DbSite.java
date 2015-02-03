package com.carnetvol.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.carnetvol.model.Site;

import java.util.ArrayList;

public class DbSite {

	private SQLiteDatabase bdd;
	private DbManager dbManager;

	/**
	 * On cree la BDD et sa table
	 * @param context {@link android.content.Context}
	 */
	public DbSite(Context context){
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


	/**
	 * Insert a new {@link com.carnetvol.model.Site}
	 * @param site the {@link com.carnetvol.model.Site} to insert
	 * @return
	 */
	public long insertSite(Site site) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, site.getName());
		values.put(DbManager.COL_COMMENT, site.getComment());
        values.put(DbManager.COL_DEFAULT, site.getIsDefault());
		return bdd.insert(DbManager.TABLE_SITES, null, values);
	}

	/**
	 * Update a new {@link com.carnetvol.model.Site}
	 * @param site the {@link com.carnetvol.model.Site} to insert
	 * @return
	 */
	public long updateSite(Site site) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(site.getId())};
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, site.getName());
		values.put(DbManager.COL_COMMENT, site.getComment());
        values.put(DbManager.COL_DEFAULT, site.getIsDefault());
		return bdd.update(DbManager.TABLE_SITES, values, where, whereArgs);
	}

	/**
	 * Delete a {@link com.carnetvol.model.Site}
	 * @param site the {@link com.carnetvol.model.Site} to delete
	 * @return
	 */
	public long deleteSite(Site site) {
		return bdd.delete(DbManager.TABLE_SITES, DbManager.COL_ID + "=" + site.getId(), null);
	}

	/**
	 * Get the list of {@link com.carnetvol.model.Site}
	 * @return the list of {@link com.carnetvol.model.Site}
	 */
	public ArrayList<Site> getSites(){
		Cursor c = bdd.query(DbManager.TABLE_SITES, new String[] {DbManager.COL_ID,
																 DbManager.COL_NAME,
																 DbManager.COL_COMMENT,
                                                                 DbManager.COL_DEFAULT},
							null, null, null, null, null);
		return cursorToSites(c);
	}


    public Site getDefaultSite(){
        String where = DbManager.COL_DEFAULT + "=?";
        String[] whereArgs = new String[] {String.valueOf(1)};
        Cursor c = bdd.query(DbManager.TABLE_SITES, new String[] {DbManager.COL_ID,
                        DbManager.COL_NAME,
                        DbManager.COL_COMMENT,
                        DbManager.COL_DEFAULT},
                where, whereArgs, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToNext();
        Site site = cursorToSite(c);
        c.close();
        return site;
    }


    /**
	 * Get {@link com.carnetvol.model.Site} by id
	 * @return the {@link com.carnetvol.model.Site}
	 */
	public Site getSiteById(int id) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(id)};
		Cursor c = bdd.query(DbManager.TABLE_SITES, new String[] {DbManager.COL_ID,
																 DbManager.COL_NAME,
																 DbManager.COL_COMMENT,
                                                                 DbManager.COL_DEFAULT},
								where, whereArgs, null, null, null);
		if (c.getCount() == 0) {
			return null;
		}
		c.moveToNext();
        Site site = cursorToSite(c);
		c.close();
		return site;
	}


	/**
	 * Transform {@link android.database.Cursor} in list of {@link com.carnetvol.model.Site}
	 * @param c{@link Cursor}
	 * @return the list of {@link com.carnetvol.model.Site}
	 */
	private ArrayList<Site> cursorToSites(Cursor c){
		ArrayList<Site> sites = new ArrayList<Site>();
		if (c.getCount() == 0) {
			return null;
		}
		while (c.moveToNext()) {
            Site site = cursorToSite(c);
			if (site!=null) {
				sites.add(site);
			}
		}
		c.close();
 		return sites;
	}

	/**
	 * Transform {@link android.database.Cursor} in {@link com.carnetvol.model.Site}
	 * @param c{@link Cursor}
	 * @return the {@link com.carnetvol.model.Site}
	 */
	private Site cursorToSite(Cursor c){
        Site site = new Site();
		site.setId(c.getInt(DbManager.NUM_COL_ID));
		site.setName(c.getString(DbManager.NUM_COL_NAME));
		site.setComment(c.getString(DbManager.NUM_COL_SITE_COMMENT));
        site.setIsDefault(c.getInt(DbManager.NUM_COL_SITE_DEFAULT));
 		return site;
	}
	
}
