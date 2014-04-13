package de.fh_erfurt.cst.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper 
{
	public static final String TIME = "time";
	public static final String PROBLEM_TYPE = "problemType";
	public static final String RESULT = "result";
	public static final String LEVEL = "level";
	
	private static final String DATABASE_NAME = "db";
	
	// --------------------------------------------------------------------------
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, 1);
	}
	
	// --------------------------------------------------------------------------
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE statistic (_id integer primary key autoincrement, time real, problemType text, result integer, level integer);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS statistic;");
		onCreate(db);
	}
	

}
