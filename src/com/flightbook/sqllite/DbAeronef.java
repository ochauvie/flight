package com.flightbook.sqllite;
import java.util.ArrayList;
import com.flightbook.model.Aeronef;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbAeronef {

    private static SQLiteDatabase bdd = DbApplicationContext.getInstance().getBdd();
 
	/**
	 * Insert a new {@link Aeronef}
	 * @param aeronef the {@link Aeronef} to insert
	 * @return
	 */
	public static long insertAeronef(Aeronef aeronef) {
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
	public static long updateAeronef(Aeronef aeronef) {
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
	public static long deleteAeronef(Aeronef aeronef) {
		return bdd.delete(DbManager.TABLE_AERONEFS, DbManager.COL_ID + "=" + aeronef.getId(), null);
	}
	
	/**
	 * Get the list of {@link Aeronef}
	 * @return the list of {@link Aeronef}
	 */
	public static ArrayList<Aeronef> getAeronefs(){
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
	public static Aeronef getAeronefById(int id) {
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

    public static Aeronef getAeronefByNameAndType(String name, String type) {
        String where = DbManager.COL_NAME + "=? and " + DbManager.COL_TYPE + "=?";
        String[] whereArgs = new String[] {name, type};
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

    public static ArrayList<Aeronef>  getAeronefByType(String type) {
        String where =  DbManager.COL_TYPE + "=?";
        String[] whereArgs = new String[] {type};
        Cursor c = bdd.query(DbManager.TABLE_AERONEFS, new String[] {DbManager.COL_ID,
                        DbManager.COL_NAME,
                        DbManager.COL_TYPE,
                        DbManager.COL_WINGSPAN,
                        DbManager.COL_WEIGHT,
                        DbManager.COL_ENGINE,
                        DbManager.COL_FIRST_FLIGHT,
                        DbManager.COL_COMMENT},
                where, whereArgs, null, null, null);
        return cursorToAeronefs(c);
    }

	
	/**
	 * Transform {@link Cursor} in list of {@link Aeronef}
	 * @param c{@link Cursor}
	 * @return the list of {@link Aeronef}
	 */
	private static ArrayList<Aeronef> cursorToAeronefs(Cursor c){
		ArrayList<Aeronef> aeronefs = new ArrayList<Aeronef>();
		if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Aeronef aeronef = cursorToAeronef(c);
                if (aeronef != null) {
                    aeronefs.add(aeronef);
                }
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
	private static Aeronef cursorToAeronef(Cursor c){
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
	
}
