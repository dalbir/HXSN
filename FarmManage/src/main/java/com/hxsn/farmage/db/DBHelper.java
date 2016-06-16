package com.hxsn.farmage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {	
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "QQForFarmManage.db";
	
	public static final String TABLE_NAME = "dbList";
	public static final String TABLE_DBNAME = "dbName";
	public static final String TABLE_USERPHONE = "userPhone";
	public static final String TABLE_USERNAME = "userName";
	public static final String TABLE_OFFLINES = "offLines";
	public static final String TABLE_USERPIC = "userPic";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public	void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists " + TABLE_NAME + "(id integer primary key autoincrement, " + TABLE_DBNAME + " String not null, " + TABLE_USERPHONE + " String not null, " + TABLE_USERNAME + " String not null, " + TABLE_OFFLINES + " Integer default 0, " + TABLE_USERPIC + " String default 'null')";
		db.execSQL(sql);
	}
	
	@Override
	public	void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {
		String sql = "drop table if exists " + TABLE_NAME;
		db.execSQL(sql);
	}
}
