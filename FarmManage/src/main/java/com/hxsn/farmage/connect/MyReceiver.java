package com.hxsn.farmage.connect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.activity.DistinctListActivity;


public class MyReceiver extends BroadcastReceiver {  
	  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        String title = intent.getExtras().getString("title");
        int code = intent.getExtras().getInt("code");
        DistinctListActivity dkActivity = ((MyApplication) context.getApplicationContext()).dkActivity;
        if(title.length()>0&&dkActivity!=null){
        	dkActivity.getDqfbTV().setText(title);
        	if(dkActivity.flag)
        	{   
        		dkActivity.firstTitle=title;
        		dkActivity.flag=false;
        	}
        }
    }
  
}  