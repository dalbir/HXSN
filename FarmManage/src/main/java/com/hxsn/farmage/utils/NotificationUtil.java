package com.hxsn.farmage.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hxsn.farmage.R;
import com.hxsn.farmage.activity.OnLineActivity;
import com.hxsn.farmage.activity.NewRealPlayActivity;

public class NotificationUtil {
	
	private static Notification notification;
	private static NotificationManager notificationManager = null;

	public static void sendNot(Context context, String otherID, String name, String phone, String time, int num) {
		if (Shared.getMessageAlert().equals("1")) {
			if (notificationManager == null) {
				notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			}
			notification=new Notification();
	    	//2 可选 如果使用系统默认的图标和文本框 需要使用setLatestEventInfo方法
	    	notification.icon=R.drawable.icon;
	    	notification.tickerText= context.getString(R.string.app_name) + "：您有新的信息，点击查看";
	    	//通知方法 响铃 震动等
	    	notification.defaults=Notification.DEFAULT_ALL;
	    	//标签是否可以清除等
	    	notification.flags=Notification.FLAG_AUTO_CANCEL;
	    	//点击后是否事件
	    	Intent intent=new Intent(context, OnLineActivity.class);
	    	intent.putExtra("otherID", otherID);
	    	intent.putExtra("otherName", name);
	    	intent.putExtra("titleName", name);
	    	intent.putExtra("headPic", "");
	    	intent.putExtra("phone",  phone);
	    	intent.putExtra("num",  num);
	    	LogUtil.showLog("Not", "num: " + num);
	    	//自定义的界面
	    	RemoteViews v=new RemoteViews(context.getPackageName(), R.layout.notifcation_item);
	    	notification.contentView =v;
	    	v.setTextViewText(R.id.not_msg, "您收到来自“" + name + "”的消息，点击查看");
	    	v.setTextViewText(R.id.not_time, time);
	    	//跳转的界面
	    	PendingIntent pi=PendingIntent.getActivity(context,0, intent, 0);
	    	//notification.setLatestEventInfo(this, "标题", "aa", pi);
	    	notification.contentIntent=pi;
	    	notificationManager.notify(1, notification);
		}
	}
	
	public static void sendNot(Context context, String time) {
		if (notificationManager == null) {
			notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		notification=new Notification();
    	//2 可选 如果使用系统默认的图标和文本框 需要使用setLatestEventInfo方法
    	notification.icon=R.drawable.icon;
    	notification.tickerText= context.getString(R.string.app_name) + "：地块有信息，请查看";
    	//通知方法 响铃 震动等
    	notification.defaults=Notification.DEFAULT_ALL;
    	//标签是否可以清除等
    	notification.flags=Notification.FLAG_AUTO_CANCEL;
    	//点击后是否事件
    	Intent intent=new Intent(context, NewRealPlayActivity.class);
    	
    	//自定义的界面
    	RemoteViews v=new RemoteViews(context.getPackageName(), R.layout.notifcation_item);
    	notification.contentView =v;
    	v.setTextViewText(R.id.not_msg, context.getString(R.string.app_name) + "：地块有信息，请查看");
    	v.setTextViewText(R.id.not_time, time);
    	//跳转的界面
    	PendingIntent pi=PendingIntent.getActivity(context,0, intent, 0);
    	//notification.setLatestEventInfo(this, "标题", "aa", pi);
    	notification.contentIntent=pi;
    	notificationManager.notify(2, notification);
	}
}
