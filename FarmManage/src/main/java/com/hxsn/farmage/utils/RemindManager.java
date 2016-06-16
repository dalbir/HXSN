package com.hxsn.farmage.utils;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class RemindManager {

	public static void doRemind(boolean isShake, Context context) {
		startAlarm(context);
		if (isShake) {
			shake(context);
		}
	}
	
	private static void shake(Context con){
		Vibrator vibrator=(Vibrator)con.getSystemService(Context.VIBRATOR_SERVICE);
		
		long pattern[]={0, 300, 325, 250};
		vibrator.vibrate(pattern, -1);
	}
	//获取系统默认铃声的Uri  
    private static Uri getSystemDefultRingtoneUri(Context con) {  
        return RingtoneManager.getActualDefaultRingtoneUri(con,  
                RingtoneManager.TYPE_NOTIFICATION);  
    }
    
    private static void startAlarm(Context con) {  
    	try{
    	MediaPlayer media= MediaPlayer.create(con,getSystemDefultRingtoneUri(con));  
    	media.setLooping(false);  
        try {  
        	media.prepare();  
        } catch (IllegalStateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        media.start();
    	}
    	catch(Exception e){}
    } 
}
