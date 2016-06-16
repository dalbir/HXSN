package com.hxsn.iot.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.util.DataConnection;
import com.hxsn.iot.util.DbUtil;


public class AlarmService_1 extends Service{


	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		
		insertBjxx(AbsApplication.getInstance().getImei(),
				getAlarmTimeMark(this), this);

		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public static String getAlarmTimeMark(Context context) {

		SQLiteDatabase dbRead;
		dbRead = DbUtil.getReadDb(context);
		Cursor cur = dbRead
				.rawQuery(
						"select alarm_timemark from alarm_phone order by alarm_timemark desc limit 0,1",
						null);
		if (cur != null && cur.moveToFirst()) {
			if (cur.getCount() > 0) {
				String time = cur.getString(cur
						.getColumnIndex("alarm_timemark"));
				cur.close();
				return time;
			}
		}
		cur.close();
		return "";
	}

	public static void insertBjxx(String imei, String timemark, Context context) {

		try {
			DataConnection.conUpdateDbPhoneAlarmByBaiDuClound(context, imei, timemark);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
