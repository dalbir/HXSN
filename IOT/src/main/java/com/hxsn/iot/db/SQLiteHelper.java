package com.hxsn.iot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteHelper extends SQLiteOpenHelper {
	private static SQLiteHelper mInstance;

	private static final int VERSION = 1;
	private static final String DATABASENAME = "aiot_phone.db";

	private SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	private SQLiteHelper(Context context, String name) {
		this(context, name, null, VERSION);
	}

	private SQLiteHelper(Context context) {
		this(context, DATABASENAME, null, VERSION);
	}

	public static synchronized SQLiteHelper getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new SQLiteHelper(context);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase sqlitedatabase) { 
		try {

			//sqlitedatabase = createDataBase(sqlitedatabase);
			// TODO create tables
			String alarm_phone_table = "create table alarm_phone (id TEXT primary key ,jd_name TEXT,dp_name TEXT ,dpid TEXT , alarm_time TEXT , alarm_msgid TEXT , alarm_level TEXT , alarm_title TEXT , alarm_text TEXT , alarm_timemark integer,sessionid TEXT,alarm_status TEXT)";
			sqlitedatabase.execSQL(alarm_phone_table);//autoincrement
			String alarm_content_table = "create table alarm_content (alarm_msgid TEXT primary key , alarm_time TEXT , alarm_level TEXT , alarm_phone TEXT ,alarm_name TEXT ,alarm_title TEXT ,alarm_content TEXT ,dp_name TEXT , jd_name TEXT)";
			sqlitedatabase.execSQL(alarm_content_table);
			String user_table = "create table user_table(id TEXT primary key, username TEXT)";
			sqlitedatabase.execSQL(user_table);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} 

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j) {
	}

	public SQLiteDatabase createDataBase(SQLiteDatabase sqlitedatabase) {
		try {
			if (sqlitedatabase != null) {
				return sqlitedatabase;
			}
			String path = "/data/data/com.snsoft.aiot.phone.v2/databases/"
					+ DATABASENAME;
			// (数据库所在的路径，游标工厂（null表示使用默认的游标工厂）)
			sqlitedatabase = SQLiteDatabase.openOrCreateDatabase(path, null);
			return sqlitedatabase;
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return null;
	}
}
