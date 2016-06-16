package com.hxsn.intelliwork.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class Shared {

	private static SharedPreferences shared;
	
	public static void init(Context context) {
		// TODO Auto-generated constructor stub
		shared = context.getSharedPreferences("HXSNInfo", Context.MODE_PRIVATE);
	}
	
	public static void setVersion(String version) {
		shared.edit().putString("version", version).commit();
	}
	public static String getVersion() {
		// TODO Auto-generated method stub
		return shared.getString("version", "");
	}
	
	public static void setIsFirstRun(boolean isFirstRun) {
		shared.edit().putBoolean("isFirstRun", isFirstRun).commit();
	}
	public static boolean getIsFirstRun() {
		// TODO Auto-generated method stub
		return shared.getBoolean("isFirstRun", true);
	}
	
	public static void setUserID(String userID) {
		shared.edit().putString("userID", userID).commit();
	}
	public static String getUserID() {
		// TODO Auto-generated method stub
		return shared.getString("userID", "");
	}
	
	/**
	 * 不需要加GetURL.BASEURL
	 */
	public static void setUserHead(String userHead) {
		shared.edit().putString("userHead", userHead).commit();
	}
	public static String getUserHead() {
		// TODO Auto-generated method stub
		return shared.getString("userHead", "");
	}
	
	public static void setUserName(String username) {
		shared.edit().putString("username", username).commit();
	}
	public static String getUserName() {
		// TODO Auto-generated method stub
		return shared.getString("username", "");
	}
	
	public static void setPassword(String password) {
		shared.edit().putString("password", password).commit();
	}
	public static String getPassword() {
		// TODO Auto-generated method stub
		return shared.getString("password", "");
	}
	
	public static void setNickName(String name) {
		shared.edit().putString("nickname", name).commit();
	}

	public static String getNickName() {
		// TODO Auto-generated method stub
		return shared.getString("nickname", "");
	}
	
	
	public static void setCode(String code) {
		shared.edit().putString("code", code).commit();
	}
	
	public static String getCode() {
		// TODO Auto-generated method stub
		return shared.getString("code", "");
	}
	
	
	
	public static void setSex(String sex) {
		shared.edit().putString("sex", sex).commit();
	}
	
	public static String getSex() {
		// TODO Auto-generated method stub
		return shared.getString("sex", "男");
	}
	
	public static void setAddress(String addresName) {
		shared.edit().putString("address", addresName).commit();
	}
	public static String getAddress() {
		// TODO Auto-generated method stub
		return shared.getString("address", "0");
	}
	
	public static void setUserHeadUrl(String userID, String UserHeadUrl) {
		shared.edit().putString(userID, UserHeadUrl).commit();
	}
	public static String getUserHeadUrl(String userID) {
		// TODO Auto-generated method stub
		return shared.getString(userID, "");
	}
	
	//消息提醒设置
	public static void setMessageAlert(String flag) {
		shared.edit().putString("messageAlert", flag).commit();
	}
	public static String getMessageAlert() {
		// TODO Auto-generated method stub
		return shared.getString("messageAlert", "1");
	}
}
