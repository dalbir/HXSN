package com.hxsn.farmage.connect;



import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hxsn.farmage.utils.WiFiStateUtils;


/**

*/
public class NetJudgeService extends Service {


	public static boolean networkState=false;
	public boolean run = true;
	public static boolean isRun = false;
	@Override 
	public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
	}

	@Override 
	public void onStart(Intent intent,int startId){
	//super.onStart(intent, startId);
		isRun = true;
	new Thread(new Runnable(){
		@SuppressWarnings("static-access")
		public void run() {
		
			while(run)
			{
				try {
//					Log.i("zxw", "service");
					Thread.currentThread().sleep(1000);
					networkState = WiFiStateUtils.detect(getApplicationContext());
					if (!networkState) {  
						networkState=false;
						Intent intent = new Intent();
		                intent.setAction(Intent.ACTION_EDIT);
		                intent.putExtra("isHave", false);
		                sendBroadcast(intent);
					}  
					else {
						networkState=true;
						Intent intent = new Intent();
		                intent.setAction(Intent.ACTION_EDIT);
		                intent.putExtra("isHave", true);
		                sendBroadcast(intent);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}).start();

	
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		run = false;
		isRun = false;
	}
}
