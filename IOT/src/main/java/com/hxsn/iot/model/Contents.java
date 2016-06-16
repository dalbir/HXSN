package com.hxsn.iot.model;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
/**
 * 
 *保存需要保存的中间量
 */

public class Contents {
	
	private static Contents instance;
	private int screenWidth;
	private Activity activity;
	private String id;
	private boolean isChecked;//设置界面是否记住控制密码
	private boolean empty;//设置界面控制密码是否为空
	private boolean isSingleAlarm;//是否进入单棚报警界面
	private int alarmCount;//新报警数量
	private ArrayList<Danyuan> list;
	private View view;
	private Fragment fragment;
	private String serverUrl;
	
	private Contents(){}
	public static Contents getInstance(){
		if(instance == null){
			instance = new Contents();
		}
		return instance;
	}
	
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public void setFragment(Fragment fragment){
		this.fragment = fragment;
	}
	
	public Fragment getFragment(){
		return fragment;
	}
	
	public void setView(View view){
		this.view = view;
	}
	
	public View getView(){
		return view;
	}
	
	public void setList(ArrayList<Danyuan> list){
		this.list = list;
	}
	
	public ArrayList<Danyuan> getList(){
		return list;
	}
	
	public void setAlarmCount(int count){
		this.alarmCount = count;
	}
	
	public int getAlarmCount(){
		return alarmCount;
	}
	
	public void setIsSingleAlarm(boolean isSingleAlarm){
		this.isSingleAlarm = isSingleAlarm;
	}
	
	public boolean getIsSingleAlarm(){
		return isSingleAlarm;
	}
	
	public void setEmpty(boolean empty){
		this.empty = empty;
	}
	
	public boolean getEmpty(){
		return empty;
	}
	
	public void setIsChecked(boolean isChecked){
		this.isChecked = isChecked;
	}
	
	public boolean getIsChecked(){
		return isChecked;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setScreenWidth(int width){
		this.screenWidth = width;
	}
	
	public int getScreenWidth(){
		return screenWidth;
	}
	
	public void setActivity(Activity activity){
		this.activity = activity;
	}
	
	public Activity getActivity(){
		return activity;
	}
	
}
