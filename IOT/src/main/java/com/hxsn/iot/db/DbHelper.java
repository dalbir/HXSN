package com.hxsn.iot.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DbHelper
{
	/**socket 地址*/
	public static final String SOCKET_ADD="192.168.11.111";
	/**socket 端口*/
	public static final int SOCKET_PORT=10086;
	public static String web_add="http://msc.fweb.cn:9090/sn-aiot-1.1";//  http://192.168.12.30:8080 http://192.168.13.42:8080
	/**webService 地址*/
//	public static String WEB_SERVICE_ADD=web_add+"/sn-aiot-1.1/webservice/";//1.0
	private static final String VERSION = "/xservice/android";
	/**webService 命名空间*/
	public static final String WEB_SERVICE_NAMESPACE="http://interfaces.xservice.aiot.snsoft.com/";
	/**存储地址*/
	public static final String PATH=""+Environment.getExternalStorageDirectory().getAbsolutePath();
	/**dp ids*/
//	public static ArrayList<String> ids=new ArrayList<String>();
	/**jd names*/
//	public static ArrayList<String> jdn=new ArrayList<String>();
	/**alarm content map*/
//	public static HashMap<String, String> acmap=new HashMap<String, String>();
	/**dp map*/
//	public static HashMap<String, String> dp_map = new HashMap<String, String>();
	/**dps list*/
//	public static ArrayList<HashMap<String, String>> dps_list = new ArrayList<HashMap<String,String>>();
	/**新报警信息list*/
	public static ArrayList<HashMap<String, String>> update_alarm_list = new ArrayList<HashMap<String,String>>();
	
	/**
	 * 设备时间
	 */
	public static int COMax=180;
	public static int JLMax=180;
	public static int JMMax=180;
	
//	public static int viewCount=0;
//	public static int farmlistCount=0;
//	public static int alarmlistCount=0;
	public static final String PREFS_NAME="SHENON_PrefsFile";
//	public static String username="zheng";
//	public static String password="123";
	public static String susername=null;
	public static String suserid="123456";
	public static String spassword=null;
//	public static int first=0;
//	public static boolean isdpselected=false;
//	public static String alarmday=null;
//	public static String histroyday=null;
	private static boolean loaded=false;
	
	/**
	 * 获取登录数据
	 * @param context
	 */
	public static void load(Context context){
		if(loaded){
			return;
		}
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		susername=prefs.getString("susername", "");
		spassword=prefs.getString("spassword", "");
		suserid=prefs.getString("suserid", null);
		COMax=prefs.getInt("comax", 180);
		JLMax=prefs.getInt("jlmax", 180);
		JMMax=prefs.getInt("jmmax", 180);
		web_add=prefs.getString("webadd", "");

		loaded=true;
	}
	
//	public static void saveAlarmDay(Context context){
//		SharedPreferences prefs=context.getSharedPreferences(PREFS_NAME, 0);
//		SharedPreferences.Editor editor=prefs.edit();
//		editor.putString("aday",alarmday);
//		editor.commit();
//		loaded=false;
//	}
//	public static void saveHistroyDay(Context context){
//		SharedPreferences prefs=context.getSharedPreferences(PREFS_NAME, 0);
//		SharedPreferences.Editor editor=prefs.edit();
//		editor.putString("hday",histroyday);
//		editor.commit();
//		loaded=false;
//	}
	
	public static void saveWebAdd(Context context){
		SharedPreferences prefs=context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("webadd",web_add);
		editor.commit();
		loaded=false;
	}
	
	public static void saveDevicesTime(Context context){
		SharedPreferences prefs=context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putInt("comax", COMax);
		editor.putInt("jlmax", JLMax);
		editor.putInt("jmmax", JMMax);
		editor.commit();
		loaded=false;
	}
	
	public static void savespass(Context context){
		SharedPreferences prefs=context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("susername", susername);
		editor.putString("spassword", spassword);
		editor.commit();
		loaded=false;
	}
	
	//取得uuid
	public static String getUuid(){
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");		
		return uuid;
		
	}
	public static String getWebAddress(){
		return web_add+VERSION;
	}
}
