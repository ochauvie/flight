package com.carnetvol.sqllite.init;


import com.carnetvol.model.Aeronef;
import com.carnetvol.sqllite.DbManager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class InitHangar  {

	
	public static void initHangar(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.put(DbManager.COL_NAME, "Alpina 4001");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 4);
		values.put(DbManager.COL_WEIGHT, 4.8);
		values.put(DbManager.COL_ENGINE, "Protronik DM2830-660");
		values.put(DbManager.COL_FIRST_FLIGHT, "17/06/2012");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arcus");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 2);
		values.put(DbManager.COL_WEIGHT, 1.5);
		values.put(DbManager.COL_ENGINE, "Protronik DM2615-1100");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Spatz 55");
		values.put(DbManager.COL_TYPE, Aeronef.T_PLANEUR);
		values.put(DbManager.COL_WINGSPAN, 2);
		values.put(DbManager.COL_ENGINE, "Protronik DM2810-800");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Calmato");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_ENGINE, "OS 46 LA");
		values.put(DbManager.COL_FIRST_FLIGHT, "29/10/2011");
		values.put(DbManager.COL_WINGSPAN, "1.6");
		values.put(DbManager.COL_WEIGHT, "2.5");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "P40");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_ENGINE, "OS 46 LA");
		values.put(DbManager.COL_FIRST_FLIGHT, "22/03/2003");
		values.put(DbManager.COL_WINGSPAN, "1.57");
		values.put(DbManager.COL_WEIGHT, "4");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Broussard");
		values.put(DbManager.COL_TYPE, Aeronef.T_AVION);
		values.put(DbManager.COL_WINGSPAN, 2.8);
		values.put(DbManager.COL_WEIGHT, 11);
		values.put(DbManager.COL_ENGINE, "Zenoah 45 PCI");
		values.put(DbManager.COL_FIRST_FLIGHT, "16/04/2011");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Raptor 30");
		values.put(DbManager.COL_TYPE, Aeronef.T_HELICO);
		values.put(DbManager.COL_ENGINE, "OS 32");
		values.put(DbManager.COL_FIRST_FLIGHT, "06/07/2002");
		db.insert(DbManager.TABLE_AERONEFS, null, values);

        values = new ContentValues();
        values.put(DbManager.COL_NAME, "Trex 450");
        values.put(DbManager.COL_TYPE, Aeronef.T_HELICO);
        values.put(DbManager.COL_ENGINE, "Brushless 450MX");
        values.put(DbManager.COL_WINGSPAN, 0.715);
        values.put(DbManager.COL_WEIGHT, 0.600);
        values.put(DbManager.COL_FIRST_FLIGHT, "11/01/2015");
        values.put(DbManager.COL_COMMENT, "Version sport");
        db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Honcho SCX10");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_ENGINE, "Axial 55T");
		values.put(DbManager.COL_FIRST_FLIGHT, "20/08/2011");
		values.put(DbManager.COL_COMMENT, "Remorque");
		values.put(DbManager.COL_WINGSPAN, "0.5");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Savage X");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_ENGINE, "HPI 4.1");
		values.put(DbManager.COL_FIRST_FLIGHT, "02/08/2007");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Bullet flux");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_FIRST_FLIGHT, "10/09/2010");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Spiral 1.2");
		values.put(DbManager.COL_TYPE, Aeronef.T_PARAMOTEUR);
		values.put(DbManager.COL_ENGINE, "Protronik DM2810-1200");
		values.put(DbManager.COL_FIRST_FLIGHT, "10/04/2010");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
				
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Arducopter");
		values.put(DbManager.COL_TYPE, Aeronef.T_HELICO);
		values.put(DbManager.COL_ENGINE, "");
		values.put(DbManager.COL_WINGSPAN, 0.5);
		values.put(DbManager.COL_WEIGHT, 1.5);
		values.put(DbManager.COL_FIRST_FLIGHT, "29/12/2012");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
				
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Tigre 1");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		values.put(DbManager.COL_FIRST_FLIGHT, "08/08/2013");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		values = new ContentValues();
		values.put(DbManager.COL_NAME, "Wraight");
		values.put(DbManager.COL_TYPE, Aeronef.T_AUTO);
		//values.put(DbManager.COL_FIRST_FLIGHT, "08/08/2013");
		values.put(DbManager.COL_ENGINE, "55T");
		values.put(DbManager.COL_COMMENT, "Remorque");
		db.insert(DbManager.TABLE_AERONEFS, null, values);
		
		
	}
			
}
