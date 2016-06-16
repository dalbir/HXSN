package com.hxsn.iot;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Xml;

import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.model.NotifyInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AbsApplication extends Application {

	private static AbsApplication mInstance = null;
	
	private String sessionid; //全局变量
	private Dapeng dapeng;	//大棚实体
	private Danyuan curDy;
	private String imei;//手机的IMEI
	private List<JiDi> jidi_list = new ArrayList<JiDi>(); //地实体列表
	
	private List<Activity> activityList = new LinkedList<Activity>();
    
    public static int localVersion = 0;
	public static int serverVersion = 1;
	public static String downloadDir = "AiotTesting/";
	public static String version_url = "http://192.168.12.80:8080/download/version.xml";
	public static final String down_url = "http://192.168.12.80:8080/download/sn-aiot-2.0-phone.apk";

	public static NotifyInfo notifyInfo=null;
    
    @Override
    public void onCreate(){
    	super.onCreate();

    }
    
    //获得本地版本号
    public int getLocalVersion() {
		PackageInfo packageInfo;
		try {
			packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
			return localVersion;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
    }
    
    //获得服务器版本号,   弃用
    public static int getVersionInfo() throws Exception {
		URL url = new URL(version_url);    
        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();     
        conn.setConnectTimeout(5000);    
        InputStream is =conn.getInputStream();     
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();
		int version = 0;
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					version = Integer.parseInt(parser.nextText()); 
				} 
				break;
			}
			type = parser.next();
		}
		return version;
	}
    
    
    public static AbsApplication getInstance(){ 
		if(null == mInstance){
			mInstance = new AbsApplication();
		}
		return mInstance;
	}
	
	/**
	 * 获取sessionid
	 * @return
	 */
	public String getSessionid()
	{
		return sessionid;
	}
	
	/**
	 * 设置sessionid
	 * @param sessionid
	 */
	public void setSessionid(String sessionid)
	{
		this.sessionid = sessionid;
	}
	
	/**
	 * 获取大棚实体
	 * @return
	 */
	public Dapeng getDapeng()
	{
		return dapeng;
	}
	
	/**
	 * 设置大棚实体
	 * @param dapeng
	 */
	public void setDapeng(Dapeng dapeng)
	{
		this.dapeng = dapeng;
	}
	
	/**
	 * 获取基地实体列表
	 * @return
	 */
	public List<JiDi> getJiDiList(){
		return jidi_list;
	}
	
	/**
	 * 设置基地实体列表
	 * @param list
	 */
	public void setJiDiList(List<JiDi> list){
		this.jidi_list = list;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
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
		 	System.exit(0);
	 }

	public Danyuan getCurDy() {
		return curDy;
	}

	public void setCurDy(Danyuan curDy) {
		this.curDy = curDy;
	}
	 
}
