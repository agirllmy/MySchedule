package com.example.mySchedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {

	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBOpenHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		android.os.Debug.waitForDebugger();
		db.execSQL("create table " + ContentData.USERS_TABLE_NAME + "("
				+ ContentData.UserTableData._ID
				+ " INTEGER PRIMARY KEY autoincrement,"
				+ ContentData.UserTableData.TITTLE + " varchar(20),"
				+ ContentData.UserTableData.IMPORTANCE + " int,"
				+ ContentData.UserTableData.STARTDATE + " varchar(20),"
				+ ContentData.UserTableData.ENDDATE + " varchar(20),"
				+ ContentData.UserTableData.ENDTIME + " varchar(20),"
				+ ContentData.UserTableData.STARTTIME + " varchar(20),"
				+ ContentData.UserTableData.REMINDER + " int,"
				+ ContentData.UserTableData.REPEAT + " int,"
				+ ContentData.UserTableData.PROJECT + " varchar(20),"
				+ ContentData.UserTableData.LABEL + " varchar(20),"
				+ ContentData.UserTableData.LOACTION + " int,"
				+ ContentData.UserTableData.SUBTASK + " varchar(20),"
				+ ContentData.UserTableData.NOTE + " varchar(20) )");
		Log.d("lmy", "haha");
		db.execSQL("create table " + ContentData.LABEL_TABLE_NAME + "("
				+ ContentData.LabelTableData._ID
				+ " INTEGER PRIMARY KEY autoincrement,"
				+ ContentData.LabelTableData.NAME + " varchar(20))");
		db.execSQL("create table " + ContentData.PROGRAM_TABLE_NAME + "("
				+ ContentData.ProgramTableData._ID
				+ " INTEGER PRIMARY KEY autoincrement,"
				+ ContentData.ProgramTableData.NAME + " varchar(20))");
		Log.d("lmy", "haha");
		db.execSQL("create table " + ContentData.TIMER_TABLE_NAME + "("
				+ ContentData.TimerTableData._ID
				+ " INTEGER PRIMARY KEY autoincrement,"
				+ ContentData.TimerTableData.TASK + " varchar(20),"
				+ ContentData.TimerTableData.TIME + " varchar(20))");
		db.execSQL("create table " + ContentData.LOCATION_TABLE_NAME + "("
				+ ContentData.LocationTableData._ID
				+ " INTEGER PRIMARY KEY autoincrement,"
				+ ContentData.LocationTableData.NAME + " varchar(20))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
