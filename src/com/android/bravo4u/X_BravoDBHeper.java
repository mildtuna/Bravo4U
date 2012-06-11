package com.android.bravo4u;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class X_BravoDBHeper  extends SQLiteOpenHelper
{
	public X_BravoDBHeper(Context context) 
	{
		super(context, "bravo.db", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{ 
	    String table = 
	        "CREATE TABLE "+ "bravo_table"+ "(" +
	        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
	        "name TEXT NOT NULL," +
	        "phone_num Text NOT NULL);";
	    
	    db.execSQL(table);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{        
	    db.execSQL("DROP TABLE IF EXISTS bravo_table");
	    onCreate(db);
	}
}
