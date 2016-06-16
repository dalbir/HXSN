package com.hxsn.town;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import java.util.LinkedList;
import java.util.List;

public class AppApplication extends Application{
	private static AppApplication mInstance = null;
	private List<Activity> activityList = new LinkedList<Activity>();
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}
	
	public static AppApplication getInstance(){
		if(null == mInstance){
			mInstance = new AppApplication();
		}
		return mInstance;
	}
	
	//添加Activity到容器中
	 public void addActivity(Activity activity){
		 activityList.add(activity);
	 }
	 //遍历所有Activity并finish
	 public void exit(){
		 for(Activity activity:activityList){
			 activity.finish();
		 }
	 }
}
