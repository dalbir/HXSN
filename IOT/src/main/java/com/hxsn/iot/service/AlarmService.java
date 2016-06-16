package com.hxsn.iot.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.util.DataConnection;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmService extends Service {

	public static final String ACTION = "com.snsoft.aiot.phone.service.ASR";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION);

			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
			String date = sDateFormat.format(new Date());
			getItemsbyImei(AbsApplication.getInstance().getImei(), date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private boolean getItemsbyImei(String imei, String begin)
			throws Exception {
		return DataConnection.conUpdateDbPhoneAlarmByIemi(this, imei, begin);
	}

}
