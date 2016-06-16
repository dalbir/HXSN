package com.hxsn.iot.control;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.activity.AiotActivity;
import com.hxsn.iot.activity.LoginActivity;
import com.hxsn.iot.model.NotifyInfo;
import com.hxsn.iot.service.AlarmService_1;
import com.hxsn.iot.util.JsonUtil;

import java.util.List;

public class MyMessageReceiver extends PushMessageReceiver {

	@Override
	public void onBind(Context arg0, int arg1, String arg2, String arg3,
			String arg4, String arg5) {
		
	}

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(Context arg0, String arg1, String arg2) {
		/**
		 * 读取消息
		 * **/
		
	}

	@Override
	public void onNotificationClicked(Context context, String title, String description, String customContentString) {

		NotifyInfo notifyInfo = getNotifyInfo(description,title,customContentString);
		AbsApplication.notifyInfo = notifyInfo;
	   //启动服务加载数据到本地数据库
		Intent i = new Intent(context,AlarmService_1.class);
		context.startService(i);
		//用户点击通知
		if(TextUtils.isEmpty(AbsApplication.getInstance().getSessionid())){
			Intent intent = new Intent(context,LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}else{
			
			Intent intent = new Intent(context,AiotActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	private NotifyInfo getNotifyInfo(String title, String description, String customContentString){
		NotifyInfo notifyInfo = new NotifyInfo();
		notifyInfo.setTitle(title);
		notifyInfo.setDescription(description);
		NotifyInfo notifyInfo2= JsonUtil.getNotifyInfo(customContentString);
		notifyInfo.setId(notifyInfo2.getId());
		notifyInfo.setType(notifyInfo2.getType());
		return  notifyInfo;
	}

	@Override
	public void onNotificationArrived(Context context, String s, String s1, String s2) {

	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		
		
	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

}
