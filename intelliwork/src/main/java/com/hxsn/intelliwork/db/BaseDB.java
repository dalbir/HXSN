package com.hxsn.intelliwork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseDB {

	private DBHelper mDBHelper;
	private Context context;
	protected SQLiteDatabase db = null;
	
	protected final static String FROMORTO = "fromOrTo";
	protected final static String MESSAGE = "message";
	protected final static String MESSAGE_TYPE = "msgtype";
	protected final static String TIME = "time";
	
	public BaseDB(Context ctx) {
		context = ctx;
		mDBHelper = new DBHelper(context);
		open();
		close();
	}

	private void open() {
		try {
			db = mDBHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void close() {
		mDBHelper.close();
		db = null;
	}

	protected void openDB() {
		if (db == null) {
			db = context.openOrCreateDatabase(DBHelper.DATABASE_NAME, DBHelper.DATABASE_VERSION, null);
		}
	}
	
	protected void closeDB() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
}
