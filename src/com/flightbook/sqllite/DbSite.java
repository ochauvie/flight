package com.flightbook.sqllite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flightbook.model.Site;

import java.util.ArrayList;

public class DbSite {

    private static SQLiteDatabase bdd = DbApplicationContext.getInstance().getBdd();

	/**
	 * Insert a new {@link com.flightbook.model.Site}
	 * @param site the {@link com.flightbook.model.Site} to insert
	 * @return
	 */
	public static long insertSite(Site site) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, site.getName());
		values.put(DbManager.COL_COMMENT, site.getComment());
        values.put(DbManager.COL_DEFAULT, site.getIsDefault());
		return bdd.insert(DbManager.TABLE_SITES, null, values);
	}

	/**
	 * Update a new {@link com.flightbook.model.Site}
	 * @param site the {@link com.flightbook.model.Site} to insert
	 * @return
	 */
	public static long updateSite(Site site) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(site.getId())};
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, site.getName());
		values.put(DbManager.COL_COMMENT, site.getComment());
        values.put(DbManager.COL_DEFAULT, site.getIsDefault());
		return bdd.update(DbManager.TABLE_SITES, values, where, whereArgs);
	}

	/**
	 * Delete a {@link com.flightbook.model.Site}
	 * @param site the {@link com.flightbook.model.Site} to delete
	 * @return
	 */
	public static long deleteSite(Site site) {
		return bdd.delete(DbManager.TABLE_SITES, DbManager.COL_ID + "=" + site.getId(), null);
	}

	/**
	 * Get the list of {@link com.flightbook.model.Site}
	 * @return the list of {@link com.flightbook.model.Site}
	 */
	public static ArrayList<Site> getSites(){
        String orderBy = DbManager.COL_NAME;
		Cursor c = bdd.query(DbManager.TABLE_SITES, new String[] {DbManager.COL_ID,
																 DbManager.COL_NAME,
																 DbManager.COL_COMMENT,
                                                                 DbManager.COL_DEFAULT},
							null, null, null, null, orderBy);
		return cursorToSites(c);
	}


    public static Site getDefaultSite(){
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
	 * Get {@link com.flightbook.model.Site} by id
	 * @return the {@link com.flightbook.model.Site}
	 */
	public static Site getSiteById(int id) {
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
	 * Transform {@link android.database.Cursor} in list of {@link com.flightbook.model.Site}
	 * @param c{@link Cursor}
	 * @return the list of {@link com.flightbook.model.Site}
	 */
	private static ArrayList<Site> cursorToSites(Cursor c){
		ArrayList<Site> sites = new ArrayList<Site>();
		if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Site site = cursorToSite(c);
                if (site != null) {
                    sites.add(site);
                }
            }
        }
		c.close();
 		return sites;
	}

	/**
	 * Transform {@link android.database.Cursor} in {@link com.flightbook.model.Site}
	 * @param c{@link Cursor}
	 * @return the {@link com.flightbook.model.Site}
	 */
	private static Site cursorToSite(Cursor c){
        Site site = new Site();
		site.setId(c.getInt(DbManager.NUM_COL_ID));
		site.setName(c.getString(DbManager.NUM_COL_NAME));
		site.setComment(c.getString(DbManager.NUM_COL_SITE_COMMENT));
        site.setIsDefault(c.getInt(DbManager.NUM_COL_SITE_DEFAULT));
 		return site;
	}
	
}
