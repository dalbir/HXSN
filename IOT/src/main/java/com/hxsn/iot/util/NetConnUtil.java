package com.hxsn.iot.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hxsn.iot.activity.LoginActivity;
import com.hxsn.iot.db.DbHelper;
import com.hxsn.iot.data.IConnPars;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NetConnUtil{
	
	private static final String and ="&";

	/**
	 * 格式化 参数  使session="111",dpid="222"变为&session="111"&dpid="222"
	 * @param strings  
	 * @return
	 */
	public static String setPars(String... strings){
		String pars="";
		for(int i=0; i<strings.length;i++){
			pars=pars+and+strings[i];			
		}
		return pars;
	}
	
	/**
	 * 旧版本的。。。使用http协议的  在报警处使用
	 * @param func
	 * @param pars 格式化后的参数
	 * @return
	 * @throws Exception
	 */
	public InputStream conServicebyHttp(String func,String pars) throws Exception{
//		String spec = "http://192.168.13.214:8080/sn-aiot-1.2/xservice/android?";
		String spec = DbHelper.getWebAddress()+"?";
		spec =spec+func+pars+"&ctype=0";//手机版
		URL url = new URL(spec);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		
		InputStream input = conn.getInputStream();  
		return input;
	}
	
	/**
	 * 获取返回信息
	 * @param status  服务器接口code码
	 * @return
	 */
	public static String getServiceMsg(int status){
		String msg;
		switch (status)
		{
		case 0:
			msg = IConnPars.RESPONSE_CODE00;
			break;
		case 1:
			msg = IConnPars.RESPONSE_CODE01;
			break;
		case 2:
			msg = IConnPars.RESPONSE_CODE02;
			break;
		case 3:
			msg = IConnPars.RESPONSE_CODE03;
			break;
		case 4:
			msg = IConnPars.RESPONSE_CODE04;
			break;
		case 5:
			msg = IConnPars.RESPONSE_CODE05;
			break;
		case 6:
			msg = IConnPars.RESPONSE_CODE06;
			break;
		case 7:
			msg = IConnPars.RESPONSE_CODE07;
			break;
		case 8:
			msg = IConnPars.RESPONSE_CODE08;
			break;
		case 9:
			msg = IConnPars.RESPONSE_CODE09;
			break;
		case 10:
			msg = IConnPars.RESPONSE_CODE10;
			break;
		case 101:
			msg = IConnPars.RESPONSE_CODE101;
			break;
		case 200:
			msg = IConnPars.RESPONSE_CODE200;
			break;
		case 201:
			msg = IConnPars.RESPONSE_CODE201;
			break;
		case 301:
			msg = IConnPars.RESPONSE_CODE301;
			break;
		case 302:
			msg = IConnPars.RESPONSE_CODE302;
			break;
		case 303:
			msg = IConnPars.RESPONSE_CODE303;
			break;
		case 401:
			msg = IConnPars.RESPONSE_CODE401;
			break;
		case 402:
			msg = IConnPars.RESPONSE_CODE402;
			break;
		case 403:
			msg = IConnPars.RESPONSE_CODE403;
			break;
		case 404:
			msg = IConnPars.RESPONSE_CODE404;
			break;
		case 405:
			msg = IConnPars.RESPONSE_CODE405;
			break;
		case 500:
			msg = IConnPars.RESPONSE_CODE500;
			break;
		case 505:
			msg = IConnPars.RESPONSE_CODE505;
			break;
		default:
			msg = "错误";
			break;
		}
		return msg;
	}
	
	public static boolean isTimeOut(int status){
		if(3 == status)
			return true;
		return false;
	}
	
	/**
	 * @param context
	 * @param code
	 * 超时处理
	 */
	public static boolean connTimeOut(Context context ,int code){
		if(201!=code){
			return false;
		}
		((Activity)context).finish();
//		App.getInstance().exit();
		Intent it=new Intent(context, LoginActivity.class);
		context.startActivity(it);
		return true;
	}
}
