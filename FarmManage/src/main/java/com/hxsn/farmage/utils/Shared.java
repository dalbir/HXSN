package com.hxsn.farmage.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class Shared {

	private static SharedPreferences shared;
	
	public static void init(Context context) {
		shared = context.getSharedPreferences("HXSNInfo", Context.MODE_PRIVATE);
	}

	public static void setValue(String key, String value){
		shared.edit().putString(key, value).apply();
	}

	public static String getValue(String key){
		return shared.getString(key, "null");
	}
	
	public static void setVersion(String version) {
		shared.edit().putString("version", version).apply();
	}
	public static String getVersion() {
		return shared.getString("version", "");
	}
	
	public static void setIsFirstRun(boolean isFirstRun) {
		shared.edit().putBoolean("isFirstRun", isFirstRun).apply();
	}
	public static boolean getIsFirstRun() {
		return shared.getBoolean("isFirstRun", true);
	}
	
	public static void setUserID(String userID) {
		shared.edit().putString("userID", userID).apply();
	}
	public static String getUserID() {
		return shared.getString("userID", "");
	}
	
	/**
	 * 不需要加GetURL.BASEURL
	 */
	public static void setUserHead(String userHead) {
		shared.edit().putString("userHead", userHead).apply();
	}
	public static String getUserHead() {
		return shared.getString("userHead", "");
	}
	
	public static void setUserName(String username) {
		shared.edit().putString("username", username).apply();
	}
	public static String getUserName() {
		return shared.getString("username", "");
	}
	
	public static void setPassword(String password) {
		shared.edit().putString("password", password).apply();
	}
	public static String getPassword() {
		return shared.getString("password", "");
	}
	
	public static void setNickName(String name) {
		shared.edit().putString("nickname", name).apply();
	}

	public static String getNickName() {
		return shared.getString("nickname", "");
	}
	
	
	public static void setCode(String code) {
		shared.edit().putString("code", code).apply();
	}
	
	public static String getCode() {
		return shared.getString("code", "");
	}

	public static void setSex(String sex) {
		shared.edit().putString("sex", sex).apply();
	}
	
	public static String getSex() {
		return shared.getString("sex", "男");
	}
	
	public static void setAddress(String addresName) {
		shared.edit().putString("address", addresName).apply();
	}
	public static String getAddress() {
		return shared.getString("address", "0");
	}
	
	public static void setUserHeadUrl(String userID, String UserHeadUrl) {
		shared.edit().putString(userID, UserHeadUrl).apply();
	}
	public static String getUserHeadUrl(String userID) {
		return shared.getString(userID, "");
	}
	
	//消息提醒设置
	public static void setMessageAlert(String flag) {
		shared.edit().putString("messageAlert", flag).apply();
	}
	public static String getMessageAlert() {
		return shared.getString("messageAlert", "1");
	}
}
