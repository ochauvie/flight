package com.flightbook.sqllite;

import java.util.ArrayList;
import com.flightbook.model.Checklist;
import com.flightbook.model.ChecklistItem;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbChecklist {

    private static SQLiteDatabase bdd = DbApplicationContext.getInstance().getBdd();
	
	
	public static ArrayList<Checklist> getChecklists(String checklistName) {
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
	
	public static void deleteChecklist(Checklist checklist) {
		bdd.delete(DbManager.TABLE_CHECKLIST, DbManager.COL_NAME + "='" + checklist.getName() + "'", null);	
	}
	
	public static void deleteChecklistItem(int checklistItemId) {
		bdd.delete(DbManager.TABLE_CHECKLIST, DbManager.COL_ID + "=" + checklistItemId , null);	
	}
	
	public static void addCheckList(Checklist checklist) {
		for (ChecklistItem item: checklist.getItems()) {
			ContentValues values = new ContentValues();
			values.put(DbManager.COL_NAME, checklist.getName());
			values.put(DbManager.COL_ACTION, item.getAction());
			values.put(DbManager.COL_ORDER, item.getOrder());
			bdd.insert(DbManager.TABLE_CHECKLIST, null, values);
		}
	}
	
	public static void updateChecklist(Checklist checklist) {
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
