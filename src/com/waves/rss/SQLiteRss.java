package com.waves.rss;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLiteRss {
	
	private static final String Database_Name = "RssDb" ;
	private static final String Database_Table = "RssTable" ;
	private static final int Database_Version = 1 ; 
	
	private static final String KEY_ROWID = "ROWID" ;
	//private static final String KEY_UPDATETIME = "UPDATETIME" ;
	private static final String KEY_HEADLINE = "HEADLINE" ;
	private static final String KEY_GUID = "GUID" ;
	
	
	private final Context ourContext ;
	private DbHelper ourHelper;
	private SQLiteDatabase ourDatabase;
	
			private static class DbHelper extends SQLiteOpenHelper {

				public DbHelper(Context context) {
					super(context, Database_Name, null , Database_Version);
					// TODO Auto-generated constructor stub
				}

				@Override
				public void onCreate(SQLiteDatabase db) {
					// TODO Auto-generated method stub
					db.execSQL("Create Table " + Database_Table + "(" + 
								KEY_ROWID + " INTEGER  PRIMARY KEY AUTOINCREMENT, " + 
								KEY_GUID + " INTEGER NOT NULL , " +
								KEY_HEADLINE + " TEXT NOT NULL);"
							
							);
					
				}

				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion,
						int newVersion) {
					// TODO Auto-generated method stub
					db.execSQL("DROP TABLE IF EXISTS " + Database_Table + ";");
					onCreate(db);
				}
				
			}
	
	public SQLiteRss(Context c){
		ourContext = c;
	}
	
	public SQLiteRss open() throws SQLiteException{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close() throws SQLiteException{
		ourHelper.close();
	}

	public long createEntry(String s,int i) throws SQLiteException{
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_GUID, i);
		cv.put(KEY_HEADLINE, s);
		return ourDatabase.insert(Database_Table,null,cv);
		
	}

	public List<RSSDescription> getData() throws SQLiteException{
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID,KEY_HEADLINE};
		Cursor c = ourDatabase.query(Database_Table, columns, null, null, null, null, KEY_GUID+" DESC");
		List<RSSDescription> result = new ArrayList<RSSDescription>();
//		String result="";
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iName = c.getColumnIndex(KEY_HEADLINE);
		
		try {
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
			{
				result.add(get(c.getString(iName)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return result;
	}
	private RSSDescription get(String s) {
		return new RSSDescription(s);
	}

} 
