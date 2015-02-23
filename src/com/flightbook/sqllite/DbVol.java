package com.flightbook.sqllite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.flightbook.activity.FilterActivity;
import com.flightbook.model.Accu;
import com.flightbook.model.TypeAeronef;
import com.flightbook.model.Vol;
import com.flightbook.model.VolsFilter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbVol {

    private static SQLiteDatabase bdd = DbApplicationContext.getInstance().getBdd();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);

    private static final String SUM_MIN_VOL = "SUM_MIN_VOL";
    private static final String SUM_NB_VOL = "SUM_NB_VOL";


	/**
	 * Insert new {@link Vol}
	 * @param vol the {@link Vol} to insert
	 * @return
	 */
	public static long insertVol(Vol vol){
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, vol.getAeronef());
		values.put(DbManager.COL_TYPE, vol.getType());
		values.put(DbManager.COL_MIN_VOL, vol.getMinutesVol());
		values.put(DbManager.COL_MIN_MOTEUR, vol.getMinutesMoteur());
		values.put(DbManager.COL_SEC_MOTEUR, vol.getSecondsMoteur());
		values.put(DbManager.COL_NOTE, vol.getNote());
		values.put(DbManager.COL_LIEU, vol.getLieu());
        if (vol.getAccuPropulsion()!=null) {
            values.put(DbManager.COL_ID_ACCU_PROPULSION, vol.getAccuPropulsion().getId());
        }

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
		String sDate = sdf.format(vol.getDateVol());
		values.put(DbManager.COL_DATE, sDate);

		return bdd.insert(DbManager.TABLE_VOLS, null, values);
	}
		
	/**
	 * Get {@link Vol} list 
	 * @return the list
	 */
	public static ArrayList<Vol> getVols(){
		String orderBy = DbManager.COL_DATE + " DESC";
		Cursor c = bdd.query(DbManager.TABLE_VOLS, new String[] {DbManager.COL_ID, 
																 DbManager.COL_NAME, 
																 DbManager.COL_TYPE,
																 DbManager.COL_MIN_VOL, 
																 DbManager.COL_MIN_MOTEUR, 
																 DbManager.COL_SEC_MOTEUR, 
																 DbManager.COL_DATE,
																 DbManager.COL_NOTE,
																 DbManager.COL_LIEU,
                                                                 DbManager.COL_ID_ACCU_PROPULSION},
							null, null, null, null, orderBy);
		return cursorToVols(c, null);
	}

    /**
     * Get {@link Vol} list
     * @return the list for one machine
     */
    public static ArrayList<Vol> getVolsByMachine(String machineName){
        String orderBy = DbManager.COL_DATE + " DESC";

        String where = null;
        String[] whereArgs = null;
        if (machineName!=null && !"".equals(machineName)) {
            where = DbManager.COL_NAME + "=?";
            whereArgs = new String[] {machineName};
        }

        Cursor c = bdd.query(DbManager.TABLE_VOLS, new String[] {DbManager.COL_ID,
                        DbManager.COL_NAME,
                        DbManager.COL_TYPE,
                        DbManager.COL_MIN_VOL,
                        DbManager.COL_MIN_MOTEUR,
                        DbManager.COL_SEC_MOTEUR,
                        DbManager.COL_DATE,
                        DbManager.COL_NOTE,
                        DbManager.COL_LIEU,
                        DbManager.COL_ID_ACCU_PROPULSION},
                where, whereArgs, null, null, orderBy);
        return cursorToVols(c, null);
    }

    /**
     * Get {@link Vol} list
     * @return the list for one flight date
     */
    public static ArrayList<Vol> getVolsByDate(String date){
        String orderBy = DbManager.COL_NAME;

        String where = null;
        String[] whereArgs = null;
        if (date!=null && !"".equals(date)) {
            where = DbManager.COL_DATE + "=?";
            whereArgs = new String[] {date};
        }

        Cursor c = bdd.query(DbManager.TABLE_VOLS, new String[] {DbManager.COL_ID,
                        DbManager.COL_NAME,
                        DbManager.COL_TYPE,
                        DbManager.COL_MIN_VOL,
                        DbManager.COL_MIN_MOTEUR,
                        DbManager.COL_SEC_MOTEUR,
                        DbManager.COL_DATE,
                        DbManager.COL_NOTE,
                        DbManager.COL_LIEU,
                        DbManager.COL_ID_ACCU_PROPULSION},
                where, whereArgs, null, null, orderBy);
        return cursorToVols(c, null);
    }

    public static ArrayList<Vol> getVolsByFilter(VolsFilter volFilter) {
        String orderBy = DbManager.COL_DATE + " DESC";
        String where = null;
        String whereAeronef = null;
        String whereSite = null;
        String whereTypeAeronef = null;
        String[] whereArgs = null;
        ArrayList<String> whereArgsList = new ArrayList<String>();
        if (volFilter!=null) {
            if (volFilter.getAeronef()!=null && !FilterActivity.EMPTY_CHOISE.equals(volFilter.getAeronef().getName())) {
                whereAeronef = DbManager.COL_NAME + "=?";
                whereArgsList.add(volFilter.getAeronef().getName());
            }

            if (volFilter.getSite()!=null && !FilterActivity.EMPTY_CHOISE.equals(volFilter.getSite().getName())) {
                whereSite = DbManager.COL_LIEU + "=?";
                whereArgsList.add(volFilter.getSite().getName());
            }

            if (volFilter.getTypeAeronef()!=null && !TypeAeronef.ALL.name().equals(volFilter.getTypeAeronef().name())) {
                whereTypeAeronef = DbManager.COL_TYPE + "=?";
                whereArgsList.add(volFilter.getTypeAeronef().name());
            }
        }

        if (whereArgsList.size()>0) {
            whereArgs = whereArgsList.toArray(new String[whereArgsList.size()]);
            where = "1=1 and " + (whereAeronef!=null?whereAeronef:"1=1")
                    + " and " +  (whereSite!=null?whereSite:"1=1")
                    + " and " +  (whereTypeAeronef!=null?whereTypeAeronef:"1=1");
        }

        Cursor c = bdd.query(DbManager.TABLE_VOLS, new String[] {DbManager.COL_ID,
                        DbManager.COL_NAME,
                        DbManager.COL_TYPE,
                        DbManager.COL_MIN_VOL,
                        DbManager.COL_MIN_MOTEUR,
                        DbManager.COL_SEC_MOTEUR,
                        DbManager.COL_DATE,
                        DbManager.COL_NOTE,
                        DbManager.COL_LIEU,
                        DbManager.COL_ID_ACCU_PROPULSION},
                where, whereArgs, null, null, orderBy);
        return cursorToVols(c, volFilter);
    }
	
	/**
	 * Delete all {@link Vol}
	 * @return
	 */
	public static long deleteVols(){
		return bdd.delete(DbManager.TABLE_VOLS, null, null);
	}
	
	/**
	 * Delete a {@link Vol}
	 * @param vol the {@link Vol} to delete
	 * @return
	 */
	public static long deleteVol(Vol vol){
		return bdd.delete(DbManager.TABLE_VOLS, DbManager.COL_ID + "=" + vol.getId(), null);
	}


    public static ArrayList<Vol> getVolsByFilterGyDateMAchine(VolsFilter volFilter) {
        String orderBy = DbManager.COL_DATE + " DESC";
        String where = null;
        String whereAeronef = null;
        String whereSite = null;
        String whereTypeAeronef = null;
        String[] whereArgs = null;
        String groupBy = DbManager.COL_DATE + "," + DbManager.COL_NAME;
        ArrayList<String> whereArgsList = new ArrayList<String>();
        if (volFilter!=null) {
            if (volFilter.getAeronef()!=null && !FilterActivity.EMPTY_CHOISE.equals(volFilter.getAeronef().getName())) {
                whereAeronef = DbManager.COL_NAME + "=?";
                whereArgsList.add(volFilter.getAeronef().getName());
            }

            if (volFilter.getSite()!=null && !FilterActivity.EMPTY_CHOISE.equals(volFilter.getSite().getName())) {
                whereSite = DbManager.COL_LIEU + "=?";
                whereArgsList.add(volFilter.getSite().getName());
            }

            if (volFilter.getTypeAeronef()!=null && !TypeAeronef.ALL.name().equals(volFilter.getTypeAeronef().name())) {
                whereTypeAeronef = DbManager.COL_TYPE + "=?";
                whereArgsList.add(volFilter.getTypeAeronef().name());
            }
        }

        if (whereArgsList.size()>0) {
            whereArgs = whereArgsList.toArray(new String[whereArgsList.size()]);
            where = "1=1 and " + (whereAeronef!=null?whereAeronef:"1=1")
                    + " and " +  (whereSite!=null?whereSite:"1=1")
                    + " and " +  (whereTypeAeronef!=null?whereTypeAeronef:"1=1");
        }

        Cursor c = bdd.query(DbManager.TABLE_VOLS, new String[] {
                        DbManager.COL_NAME,
                        "sum(" + DbManager.COL_MIN_VOL + ") AS " + SUM_MIN_VOL,
                        "sum(" + DbManager.COL_ID + ") AS " + SUM_NB_VOL,
                        DbManager.COL_DATE},
                where, whereArgs, groupBy, null, orderBy);
        return cursorToVolResults(c, volFilter);
    }
	
	/**
	 * Transform {@link Cursor} in list of {@link Vol}
	 * @param c{@link Cursor}
	 * @return the list of {@link Vol}
	 */
	private static ArrayList<Vol> cursorToVols(Cursor c, VolsFilter volFilter){
		ArrayList<Vol> vols = new ArrayList<Vol>();
		if (c.getCount() > 0) {
            while (c.moveToNext()) {
                boolean isOk = true;
                Date dVol = new Date();
                try {
                    dVol = sdf.parse(c.getString(DbManager.NUM_COL_DATE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (volFilter!=null) {
                    if (volFilter.getDateFin()!=null && dVol.after(volFilter.getDateFin())) {
                        isOk = false;
                    }
                    if (volFilter.getDateDebut()!=null && dVol.before(volFilter.getDateDebut())) {
                        isOk = false;
                    }
                }
                if (isOk) {
                    Vol vol = new Vol();
                    vol.setId(c.getInt(DbManager.NUM_COL_ID));
                    vol.setAeronef(c.getString(DbManager.NUM_COL_NAME));
                    vol.setType(c.getString(DbManager.NUM_COL_TYPE));
                    vol.setMinutesVol(c.getInt(DbManager.NUM_COL_MIN_VOL));
                    vol.setMinutesMoteur(c.getInt(DbManager.NUM_COL_MIN_MOTEUR));
                    vol.setSecondsMoteur(c.getInt(DbManager.NUM_COL_SEC_MOTEUR));
                    vol.setNote(c.getString(DbManager.NUM_COL_NOTE));
                    vol.setLieu(c.getString(DbManager.NUM_COL_LIEU));
                    vol.setDateVol(dVol);

                    int idAccuPropultion = c.getInt(DbManager.NUM_COL_ID_ACCU_PROPULSION);
                    if (idAccuPropultion >= 0) {
                        Accu accu = DbAccu.getAccuById(idAccuPropultion);
                        if (accu != null) {
                            vol.setAccuPropulsion(accu);
                        }
                    }

                    vols.add(vol);
                }
            }
        }
		c.close();
 		return vols;
	}


    private static ArrayList<Vol> cursorToVolResults(Cursor c, VolsFilter volFilter){
        ArrayList<Vol> vols = new ArrayList<Vol>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                boolean isOk = true;
                Date dVol = new Date();
                try {
                    dVol = sdf.parse(c.getString(DbManager.NUM_COL_DATE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (volFilter!=null) {
                    if (volFilter.getDateFin()!=null && dVol.after(volFilter.getDateFin())) {
                        isOk = false;
                    }
                    if (volFilter.getDateDebut()!=null && dVol.before(volFilter.getDateDebut())) {
                        isOk = false;
                    }
                }
                if (isOk) {
                    Vol vol = new Vol();
                    vol.setAeronef(c.getString(0));
                    vol.setMinutesVol(c.getInt(1));
                    //vol.setSumNbsVol(c.getInt(2));
                    vol.setDateVol(dVol);
                    vols.add(vol);
                }
            }
        }
        c.close();
        return vols;
    }
	
	
}
