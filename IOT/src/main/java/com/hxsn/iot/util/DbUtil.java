package com.hxsn.iot.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hxsn.iot.db.SQLiteHelper;

public class DbUtil {
	
	private static SQLiteDatabase dbRead =null;
	private static SQLiteDatabase dbWrite =null;
	
	//取可读的数据库
	public static SQLiteDatabase getReadDb(Context context){		
		dbRead= SQLiteHelper.getInstance(context).getReadableDatabase();
		return dbRead;		
	}
	//取可写的数据库
	public static SQLiteDatabase getWriteDb(Context context){		
		dbWrite=SQLiteHelper.getInstance(context).getWritableDatabase();
		return dbWrite;
		
	}
	

	public static void closeDb(SQLiteDatabase db){
		Log.i("sqlite", "set close");
		if(null!=db){
			if(db.isOpen()){
				db.close();
			}
		}
	}

}
