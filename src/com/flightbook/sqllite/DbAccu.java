package com.flightbook.sqllite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flightbook.model.Accu;
import com.flightbook.model.TypeAccu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DbAccu {

    private static SQLiteDatabase bdd = DbApplicationContext.getInstance().getBdd();
	/**
	 * Insert a new {@link com.flightbook.model.Accu}
	 * @param accu the {@link com.flightbook.model.Accu} to insert
	 * @return
	 */
	public static long insertAccu(Accu accu) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_ACCU_CAPACITE, accu.getCapacite());
        values.put(DbManager.COL_ACCU_CYCLES, accu.getNbCycles());
        if (accu.getDateAchat()!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
            String sDate = sdf.format(accu.getDateAchat());
            values.put(DbManager.COL_ACCU_DATE_ACHAT, sDate);
        }
        values.put(DbManager.COL_ACCU_ELEMENTS, accu.getNbElements());
        values.put(DbManager.COL_ACCU_MARQUE, accu.getMarque());
        values.put(DbManager.COL_ACCU_NOM, accu.getNom());
        values.put(DbManager.COL_ACCU_NUMERO, accu.getNumero());
        values.put(DbManager.COL_ACCU_TYPE, accu.getType().name());
        values.put(DbManager.COL_ACCU_VOLTAGE, accu.getVoltage());
        values.put(DbManager.COL_ACCU_TAUX_DECHARGE, accu.getTauxDecharge());
        return bdd.insert(DbManager.TABLE_ACCUS, null, values);
	}

	/**
	 * Update a new {@link com.flightbook.model.Accu}
	 * @param accu the {@link com.flightbook.model.Accu} to insert
	 * @return
	 */
	public static long updateAccu(Accu accu) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(accu.getId())};
		ContentValues values = new ContentValues();
        values.put(DbManager.COL_ACCU_CAPACITE, accu.getCapacite());
        values.put(DbManager.COL_ACCU_CYCLES, accu.getNbCycles());
        String sDate = null;
        if (accu.getDateAchat()!=null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
            sDate = sdf.format(accu.getDateAchat());
        }
        values.put(DbManager.COL_ACCU_DATE_ACHAT, sDate);

        values.put(DbManager.COL_ACCU_ELEMENTS, accu.getNbElements());
        values.put(DbManager.COL_ACCU_MARQUE, accu.getMarque());
        values.put(DbManager.COL_ACCU_NOM, accu.getNom());
        values.put(DbManager.COL_ACCU_NUMERO, accu.getNumero());
        values.put(DbManager.COL_ACCU_TYPE, accu.getType().name());
        values.put(DbManager.COL_ACCU_VOLTAGE, accu.getVoltage());
        values.put(DbManager.COL_ACCU_TAUX_DECHARGE, accu.getTauxDecharge());
		return bdd.update(DbManager.TABLE_ACCUS, values, where, whereArgs);
	}

	/**
	 * Delete a {@link com.flightbook.model.Accu}
	 * @param accu the {@link com.flightbook.model.Accu} to delete
	 * @return
	 */
	public static long deleteAccu(Accu accu) {
		return bdd.delete(DbManager.TABLE_ACCUS, DbManager.COL_ID + "=" + accu.getId(), null);
	}

	/**
	 * Get the list of {@link com.flightbook.model.Accu}
	 * @return the list of {@link com.flightbook.model.Accu}
	 */
	public static ArrayList<Accu> getAccus(){
        String orderBy = DbManager.COL_ACCU_NOM;
		Cursor c = bdd.query(DbManager.TABLE_ACCUS, new String[] {DbManager.COL_ID,
                                                            DbManager.COL_ACCU_TYPE,
                                                            DbManager.COL_ACCU_ELEMENTS,
                                                            DbManager.COL_ACCU_CAPACITE,
                                                            DbManager.COL_ACCU_TAUX_DECHARGE,
                                                            DbManager.COL_ACCU_NUMERO,
                                                            DbManager.COL_ACCU_NOM,
                                                            DbManager.COL_ACCU_MARQUE,
                                                            DbManager.COL_ACCU_DATE_ACHAT,
                                                            DbManager.COL_ACCU_CYCLES,
                                                            DbManager.COL_ACCU_VOLTAGE
                                                            },
							null, null, null, null, orderBy);
		return cursorToAccus(c);
	}

    /**
	 * Get {@link com.flightbook.model.Site} by id
	 * @return the {@link com.flightbook.model.Accu}
	 */
	public static Accu getAccuById(int id) {
		String where = DbManager.COL_ID + "=?";
		String[] whereArgs = new String[] {String.valueOf(id)};
		Cursor c = bdd.query(DbManager.TABLE_ACCUS, new String[] {DbManager.COL_ID,
                        DbManager.COL_ACCU_TYPE,
                        DbManager.COL_ACCU_ELEMENTS,
                        DbManager.COL_ACCU_CAPACITE,
                        DbManager.COL_ACCU_TAUX_DECHARGE,
                        DbManager.COL_ACCU_NUMERO,
                        DbManager.COL_ACCU_NOM,
                        DbManager.COL_ACCU_MARQUE,
                        DbManager.COL_ACCU_DATE_ACHAT,
                        DbManager.COL_ACCU_CYCLES,
                        DbManager.COL_ACCU_VOLTAGE},
								where, whereArgs, null, null, null);
		if (c.getCount() == 0) {
			return null;
		}
		c.moveToNext();
        Accu accu = cursorToAccu(c);
		c.close();
		return accu;
	}

    public static Accu getAccuByName(String name) {
        String where = DbManager.COL_ACCU_NOM + "=?";
        String[] whereArgs = new String[] {name};
        Cursor c = bdd.query(DbManager.TABLE_ACCUS, new String[] {DbManager.COL_ID,
                        DbManager.COL_ACCU_TYPE,
                        DbManager.COL_ACCU_ELEMENTS,
                        DbManager.COL_ACCU_CAPACITE,
                        DbManager.COL_ACCU_TAUX_DECHARGE,
                        DbManager.COL_ACCU_NUMERO,
                        DbManager.COL_ACCU_NOM,
                        DbManager.COL_ACCU_MARQUE,
                        DbManager.COL_ACCU_DATE_ACHAT,
                        DbManager.COL_ACCU_CYCLES,
                        DbManager.COL_ACCU_VOLTAGE},
                where, whereArgs, null, null, null);
        if (c.getCount() == 0) {
            return null;
        }
        c.moveToNext();
        Accu accu = cursorToAccu(c);
        c.close();
        return accu;
    }

    public static ArrayList<Accu> getAccuByType(TypeAccu type) {
        String where = null;
        String[] whereArgs = null;
        if (type!=null) {
            where = DbManager.COL_ACCU_TYPE + "=?";
            whereArgs = new String[] {type.name()};
        }
        String orderBy = DbManager.COL_ACCU_NOM;
        Cursor c = bdd.query(DbManager.TABLE_ACCUS, new String[] {DbManager.COL_ID,
                        DbManager.COL_ACCU_TYPE,
                        DbManager.COL_ACCU_ELEMENTS,
                        DbManager.COL_ACCU_CAPACITE,
                        DbManager.COL_ACCU_TAUX_DECHARGE,
                        DbManager.COL_ACCU_NUMERO,
                        DbManager.COL_ACCU_NOM,
                        DbManager.COL_ACCU_MARQUE,
                        DbManager.COL_ACCU_DATE_ACHAT,
                        DbManager.COL_ACCU_CYCLES,
                        DbManager.COL_ACCU_VOLTAGE
                },
                where, whereArgs, null, null, orderBy);
        return cursorToAccus(c);
    }

    public static ArrayList<Accu> getAccuByNbElements(int nbElements) {
        String where = DbManager.COL_ACCU_ELEMENTS + "=?";
        String[] whereArgs = new String[] {String.valueOf(nbElements)};
        String orderBy = DbManager.COL_ACCU_NOM;
        Cursor c = bdd.query(DbManager.TABLE_ACCUS, new String[] {DbManager.COL_ID,
                        DbManager.COL_ACCU_TYPE,
                        DbManager.COL_ACCU_ELEMENTS,
                        DbManager.COL_ACCU_CAPACITE,
                        DbManager.COL_ACCU_TAUX_DECHARGE,
                        DbManager.COL_ACCU_NUMERO,
                        DbManager.COL_ACCU_NOM,
                        DbManager.COL_ACCU_MARQUE,
                        DbManager.COL_ACCU_DATE_ACHAT,
                        DbManager.COL_ACCU_CYCLES,
                        DbManager.COL_ACCU_VOLTAGE
                },
                where, whereArgs, null, null, orderBy);
        return cursorToAccus(c);
    }


    /**
	 * Transform {@link android.database.Cursor} in list of {@link com.flightbook.model.Accu}
	 * @param c{@link Cursor}
	 * @return the list of {@link com.flightbook.model.Accu}
	 */
	private static ArrayList<Accu> cursorToAccus(Cursor c){
		ArrayList<Accu> accus = new ArrayList<Accu>();
		if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Accu accu = cursorToAccu(c);
                if (accu != null) {
                    accus.add(accu);
                }
            }
        }
		c.close();
 		return accus;
	}

	/**
	 * Transform {@link android.database.Cursor} in {@link com.flightbook.model.Accu}
	 * @param c{@link Cursor}
	 * @return the {@link com.flightbook.model.Accu}
	 */
	private static Accu cursorToAccu(Cursor c){
        Accu accu = new Accu();
        accu.setId(c.getInt(DbManager.NUM_COL_ID));
        accu.setCapacite(c.getInt(DbManager.NUM_COL_ACCU_CAPACITE));
        accu.setNbCycles(c.getInt(DbManager.NUM_COL_ACCU_CYCLES));
        String sDate = c.getString(DbManager.NUM_COL_ACCU_DATE_ACHAT);
        if (sDate!=null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.FRANCE);
                Date dAccu = sdf.parse(sDate);
                accu.setDateAchat(dAccu);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        accu.setNbElements(c.getInt(DbManager.NUM_COL_ACCU_ELEMENTS));
        accu.setMarque(c.getString(DbManager.NUM_COL_ACCU_MARQUE));
        accu.setNom(c.getString(DbManager.NUM_COL_ACCU_NOM));
        accu.setNumero(c.getInt(DbManager.NUM_COL_ACCU_NUMERO));
        accu.setType(TypeAccu.valueOf(c.getString(DbManager.NUM_COL_ACCU_TYPE)));
        accu.setVoltage(c.getFloat(DbManager.NUM_COL_ACCU_VOLTAGE));
        accu.setTauxDecharge(c.getInt(DbManager.NUM_COL_ACCU_TAUX_DECHARGE));
        return accu;
	}
	
}
