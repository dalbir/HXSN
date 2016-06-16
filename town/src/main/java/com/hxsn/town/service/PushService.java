package com.hxsn.town.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.hxsn.town.R;
import com.hxsn.town.activity.PushDetailActivity;
import com.hxsn.town.data.Contents;
import com.hxsn.town.data.DataController;

import java.util.HashMap;


//推送后台服务
public class PushService extends Service{

	private TelephonyManager tm;
	private String imei;
	private NotificationManager nManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		imei = tm.getDeviceId();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void start() {
		new UpdateThread().start();
	}
	
	class UpdateThread extends Thread{
		@Override
		public void run(){
			try{
				HashMap<String,String> map = DataController.getPushData();
				Contents.getInstance().setUrl(map.get("url"));//保存url，用于推送详细页
				showNotify(map);
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void showNotify(HashMap<String,String> map){
		if(null == map || map.size()==0)
			return;
		PendingIntent m_PendingIntent = PendingIntent.getActivity(this, 0, new Intent(PushService.this, PushDetailActivity.class), 0);
		Notification n = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
				.setContentTitle(map.get("title"))
				.setContentText(map.get("about"))
				.setContentIntent(m_PendingIntent)
				.build();
		nManager.notify(0, n);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
