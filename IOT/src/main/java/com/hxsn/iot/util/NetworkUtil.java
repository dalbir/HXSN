package com.hxsn.iot.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.activity.AiotActivity;
import com.hxsn.iot.activity.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 工具类
 */
public class NetworkUtil {

	//网络错误判断
	public static boolean isErrorCode(String code,Context context){
		if ("200".equals(code)) {
			return false;
		} else if("201".equals(code)){
			logout(context);//登录超时进入登录界面
		} else {
			MessDialog.show(NetConnUtil.getServiceMsg(Integer.parseInt(code)), context);
		}
		return true;
	}
	
	//将时间long值按格式显示
	public static String convertTimeFormat(String time){
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//yyyy-MM-dd HH:mm:ss
		String date = sDateFormat.format(new Date(Long.parseLong(time)));
		Log.i("time", time);
		return date;
	}
	
	public static void logout(Context context){
		SharedPreferences sharedata = context.getSharedPreferences("data", 0);//获得SharedPreferences对象  
		String name = sharedata.getString("username", null);
		String passward = sharedata.getString("pwd", null);
		if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(passward)){
	     
			
			Editor esharedata = context.getSharedPreferences("data", 0).edit();
			esharedata.putString("username", null);
			esharedata.putString("pwd", null);
			esharedata.putString("sessionid", null);
			esharedata.putString("controlPwd", null);
			esharedata.commit();
		}
		Intent it=new Intent(context, LoginActivity.class);
		context.startActivity(it);
		((AiotActivity)context).finish();
		AbsApplication.getInstance().exit();
		
	}
	
	//获取sdcard中/aiot/ServerUri.txt中的内容
	public static String getLocalUri() {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
			String str = Environment.getExternalStorageDirectory().getAbsolutePath()+"/aiot/";
			File file = new File(str);
			if(!file.exists()){
				file.mkdirs();
			}
		    File fileDir = new File(file.getAbsolutePath()+"/"+"ServerUri.txt");
			try {
				if(!fileDir.exists()){
					fileDir.createNewFile();
					return "";
			    }
				InputStream is = new FileInputStream(fileDir);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        byte[] array = new byte[1024];
		        int len = -1;
		        while( (len = is.read(array)) != -1){
		            bos.write(array,0,len);
		        }
		        bos.close();
		        is.close();
		        return bos.toString().trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return "";
	}
	
}
