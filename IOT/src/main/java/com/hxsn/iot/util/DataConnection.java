package com.hxsn.iot.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.db.DbHelper;
import com.hxsn.iot.data.IConnPars;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.model.Contents;

import java.util.ArrayList;
import java.util.HashMap;

//两种获取数据的方式，返回使用的方式，内容
/**
 * @author Administrator
 *
 */
public class DataConnection{
	private static SQLiteDatabase dbRead;
	private static SQLiteDatabase dbWrite;
	
	public static void closeReadDB(){
		if(null!=dbRead){
			DbUtil.closeDb(dbRead);
		}
	}
	public static void closeWriteDB(){
		if(null!=dbWrite){
			DbUtil.closeDb(dbWrite);
		}
	}

	public static void deleteAlarmTable(Context context){
		if(null == dbWrite){
			dbWrite=DbUtil.getWriteDb(context);
		}
		dbWrite.execSQL("delete from alarm_phone");
		dbWrite.execSQL("delete from alarm_content");
	}


	public static boolean conUpdateReadAlarm(Context context,String msgid){
		try
		{
			dbWrite=DbUtil.getWriteDb(context);
			dbWrite.execSQL("update alarm_phone set alarm_status =? where alarm_msgid =?",
					new Object[]{"readed",msgid});
//			DbUtil.closeDb(dbWrite);
			return true;
		}
		catch (Exception e)
		{
			DbUtil.closeDb(dbWrite);
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	//获取数据库中单元数据的全部，已读，今日的数据
	public static ArrayList<HashMap<String,String>> conDbDPPhoneAlarmOfType(Context context,int count,String type,String dpId){
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		dbRead = DbUtil.getReadDb(context);
		Cursor cur = null;
		if(type.equals("all")){
			cur = dbRead.rawQuery("select * from alarm_phone where dpid='"+dpId+"' order by alarm_time desc limit "+count+",30",null);
		} else if(type.equals("readed")){
			cur = dbRead.rawQuery("select * from alarm_phone where dpid='"+dpId+"' and alarm_status='readed' order by alarm_time desc limit "+count+",30", null);
		} else {
			cur = dbRead.rawQuery("select * from alarm_phone where dpid='"+dpId+"' and strftime('%Y%m%d',alarm_time)>=(select strftime('%Y%m%d',datetime('now','localtime')))", null);
		}
		if(cur.getCount()>0){
			while(cur.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("alarmtime", cur.getString(cur.getColumnIndex("alarm_time")));
				map.put("level", cur.getString(cur.getColumnIndex("alarm_level")));
				map.put("dpname", cur.getString(cur.getColumnIndex("dp_name")));
				map.put("dpid", cur.getString(cur.getColumnIndex("dpid")));
				map.put("msgid", cur.getString(cur.getColumnIndex("alarm_msgid")));
				map.put("title", cur.getString(cur.getColumnIndex("alarm_title")));
				map.put("text", cur.getString(cur.getColumnIndex("alarm_text")));
				map.put("status", cur.getString(cur.getColumnIndex("alarm_status")));
				list.add(map);
			}
			cur.close();
			return list;
		}
		cur.close();
		return list;
		
	}
	
	//获取数据库中所有数据的全部，已读，今日的数据
	public static ArrayList<HashMap<String,String>> conDbPhoneAlarmOfType(Context context,int count,String type){
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		dbRead = DbUtil.getReadDb(context);
		Cursor cur = null;
		if(type.equals("all")){
			cur = dbRead.rawQuery("select * from alarm_phone order by alarm_time desc limit "+count+",15",null);
		} else if(type.equals("readed")){
			cur = dbRead.rawQuery("select * from alarm_phone where alarm_status='readed' order by alarm_time desc limit "+count+",15", null);
		} else {
			cur = dbRead.rawQuery("select * from alarm_phone where strftime('%Y%m%d',alarm_time)>=(select strftime('%Y%m%d',datetime('now','localtime'))) order by alarm_time desc limit "+count+",15", null);
		}
		if(cur.getCount()>0){
			while(cur.moveToNext()){
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("alarmtime", cur.getString(cur.getColumnIndex("alarm_time")));
				map.put("level", cur.getString(cur.getColumnIndex("alarm_level")));
				map.put("dpname", cur.getString(cur.getColumnIndex("dp_name")));
				map.put("dpid", cur.getString(cur.getColumnIndex("dpid")));
				map.put("msgid", cur.getString(cur.getColumnIndex("alarm_msgid")));
				map.put("title", cur.getString(cur.getColumnIndex("alarm_title")));
				map.put("text", cur.getString(cur.getColumnIndex("alarm_text")));
				map.put("status", cur.getString(cur.getColumnIndex("alarm_status")));
				list.add(map);
			}
			cur.close();
			return list;
		}
		cur.close();
		return list;
		
	}
	
	/**通过DB获取报警数据 list*/
	public static ArrayList<HashMap<String, String>> conDbPhoneAlarm(Context context,int start,String dpId){
			ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
			dbRead=DbUtil.getReadDb(context);
			Cursor cur = null;
			if("ALL".equals(dpId)){
				cur=dbRead.rawQuery("select * from alarm_phone order by alarm_time desc limit "+start+",30",null);
				
			}else {
				 cur=dbRead.rawQuery("select * from alarm_phone where dpid='"+dpId+"' order by alarm_time desc limit "+start+",30",null);
			}
			if(cur.getCount()>0){
				while(cur.moveToNext()){
					HashMap<String, String> map=new HashMap<String, String>();
					map.put("alarmtime", cur.getString(cur.getColumnIndex("alarm_time")));
					map.put("level", cur.getString(cur.getColumnIndex("alarm_level")));
					map.put("dpname", cur.getString(cur.getColumnIndex("dp_name")));
					map.put("dpid", cur.getString(cur.getColumnIndex("dpid")));
					map.put("msgid", cur.getString(cur.getColumnIndex("alarm_msgid")));
					map.put("title", cur.getString(cur.getColumnIndex("alarm_title")));
					map.put("text", cur.getString(cur.getColumnIndex("alarm_text")));
					map.put("status", cur.getString(cur.getColumnIndex("alarm_status")));
					list.add(map);
				}
				cur.close();
				return list;
			}
			cur.close();
			return null;
	}
	
	public static boolean conUpdateDbPhoneAlarmByIemi(Context context,String imei,String begin) throws Exception{
		ArrayList<HashMap<String, String>> up_alarm_list = new ArrayList<HashMap<String,String>>();
		String ei = IConnPars.PARS_IMEI + imei;
		//增加时间串，防止重复刷新
		String timeMask = getMaxTimemask(context);
		String timeParam = "";
		if(timeMask==null){
			timeParam = IConnPars.PARS_BEGIN + begin;
		}else{
			timeParam = IConnPars.PARS_TIME_MARK + timeMask;
		}
//		String tmark = IConnPars.PARS_TIME_MARK + timemark;
		String pars = NetConnUtil.setPars(ei, timeParam);
		
		ArrayList<HashMap<String, String>> list = null;
		ParseDatas parse = DataController.getAlarmInfoData(pars);
		if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), context)){
				list = new ArrayList<HashMap<String,String>>();
			} else {
				list = parse.getList();
			}
		} else {
			MessDialog.show(context.getResources().getString(R.string.server_unusual_msg), context);
			list = new ArrayList<HashMap<String,String>>();
		}
		
		if(null != list && list.size()!= 0){
			dbWrite=DbUtil.getWriteDb(context);
			dbWrite.beginTransaction();
			for(int i=0;i<list.size();i++){
				HashMap<String, String> alarm_map=list.get(i);
				dbWrite.execSQL("insert into alarm_phone(id,dp_name,jd_name,dpid,alarm_time ,alarm_msgid ,alarm_level ,alarm_title ,alarm_text ,alarm_timemark,sessionid,alarm_status) values (?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[]{DbHelper.getUuid(),alarm_map.get("dp_name"),alarm_map.get("jd_name"),alarm_map.get("dpid"),alarm_map.get("alarm_time"),alarm_map.get("alarm_msgid"),alarm_map.get("alarm_level"),alarm_map.get("alarm_title"),alarm_map.get("bjtext"),alarm_map.get("alarm_timemark"), AbsApplication.getInstance().getSessionid(),"noread"});
				up_alarm_list.add(alarm_map);
			}
			DbHelper.update_alarm_list = up_alarm_list;
			dbWrite.setTransactionSuccessful();
			dbWrite.endTransaction();
			return true;
		}
		return false;
	}
	public static String getAlarmTimeMark(Context context){
		dbRead=DbUtil.getReadDb(context);
		Cursor cur=dbRead.rawQuery("select timemark from alarm_phone order by timemark desc limit 0,1",null);
		if(cur != null && cur.moveToFirst()){
			if(cur.getCount()>0){
				String time = cur.getString(cur.getColumnIndex("timemark"));
				cur.close();
				return time;
			}
		}
		cur.close();
		return "";
	}
	public static String getAlarmTime(Context context){
		dbRead=DbUtil.getReadDb(context);
		Cursor cur=dbRead.rawQuery("select time from alarm_phone order by alarm_time desc limit 0,1",null);
		if(cur != null && cur.moveToFirst()){
			if(cur.getCount()>0){
				String time = cur.getString(cur.getColumnIndex("time"));
				cur.close();
				return time;
			}
		}
		cur.close();
		return "0";
	}
	
	public static HashMap<String, String> conDbAlarmContent(Context context,String messageid){
		HashMap<String, String> map=new HashMap<String, String>();
		dbRead=DbUtil.getReadDb(context);
		Log.i("sqlite", "read message id:" + messageid);
		Cursor cur=dbRead.rawQuery("select * from alarm_content where alarm_msgid= ?", new String[]{messageid});
		if(cur.getCount()>0){
			while(cur.moveToNext()){
				String alarmtime=cur.getString(cur.getColumnIndex("alarm_time"));
				String level=cur.getString(cur.getColumnIndex("alarm_level"));
				String phone=cur.getString(cur.getColumnIndex("alarm_phone"));
				String name=cur.getString(cur.getColumnIndex("alarm_name"));
				String titile=cur.getString(cur.getColumnIndex("alarm_title"));
				String content=cur.getString(cur.getColumnIndex("alarm_content"));
				String dpname=cur.getString(cur.getColumnIndex("dp_name"));
				String jdname=cur.getString(cur.getColumnIndex("jd_name"));
				map.put("alarmtime", alarmtime);
				map.put("level", level);
				map.put("phoneno", phone);
				map.put("name", name);
				map.put("title", titile);
				map.put("content", content);
				map.put("dpname", dpname);
				map.put("jdname", jdname);
			}
			cur.close();
//			DbUtil.closeDb(dbRead);
			return map;
		}
		cur.close();
//		DbUtil.closeDb(dbRead);
		return null;
	}
	
	public static String conUpdataDbContentByMegId(Context context,String messageid) throws Exception{
		String msg = "e";
		String msgid = IConnPars.PARS_MESG_ID + messageid;
		String pars = NetConnUtil.setPars(msgid);
		
		HashMap<String, String> map = DataController.getAlarmInfoSearch(pars);
		
		int code =Integer.parseInt(map.get("code"));
		if(IConnPars.SUCCESS_OK == code){
			dbWrite=DbUtil.getWriteDb(context);
			Cursor cur = dbWrite.rawQuery("select alarm_msgid from alarm_content where alarm_msgid=?", new  String[]{messageid});
			if(cur.getCount()>0){
			}else{
				dbWrite.execSQL("insert into alarm_content(alarm_msgid, alarm_time, alarm_level, alarm_phone, alarm_name, alarm_title, alarm_content, dp_name, jd_name) values (?,?,?,?,?,?,?,?,?)",
					new Object[]{messageid,map.get("time"),map.get("level"),map.get("receivePhone"),map.get("receiver"),map.get("title"),map.get("content"),map.get("dpname"),map.get("jdname")});
			}
			cur.close();
			msg ="0";
		}else{
//			msg = NetConnUtil.getServiceMsg(code);
//			NetConnUtil.connTimeOut(context, code);//超时处理
			if(!NetConnUtil.connTimeOut(context, code)){
				msg = NetConnUtil.getServiceMsg(code);
			}
		}
		return msg;
		
	
	}
	
	public static String conUpdateDbAlarmContentbySession(Context context,String messageid,String sessionid) throws Exception{
		String msg = "e";
		NetConnUtil netconn = new NetConnUtil();
		XMLParse xml = XMLParse.getInstance();
		String sid = IConnPars.PARS_SESSIONID + sessionid;
		String msgid = IConnPars.PARS_MESG_ID + messageid;
		String pars = NetConnUtil.setPars(sid,msgid);
		HashMap<String, String> map = xml.parserAlarmContent(xml.parserToList(xml.parser(
				netconn.conServicebyHttp(IConnPars.METHOD_BJXQ, pars))));
		int code =Integer.parseInt(map.get("code"));
		if(IConnPars.SUCCESS_OK == code){
			dbWrite=DbUtil.getWriteDb(context);
			dbWrite.execSQL("insert into alarm_content(alarm_msgid, alarm_time, alarm_level, alarm_phone, alarm_state, alarm_name, alarm_title, alarm_content, dp_name, jd_name) values (?,?,?,?,?,?,?,?,?,?)",
					new Object[]{messageid,map.get("time"),map.get("level"),map.get("receivePhone"),map.get("msgstatus"),map.get("receiver"),map.get("title"),map.get("content"),map.get("dpname"),map.get("jdname")});
			msg ="0";
		}else{
//			msg = NetConnUtil.getServiceMsg(code);
//			NetConnUtil.connTimeOut(context, code);//超时处理
			if(!NetConnUtil.connTimeOut(context, code)){
				msg = NetConnUtil.getServiceMsg(code);
			}
		}
		return msg;
		
	}
	
	public static String deleteAlarmPhoneData(String msgId,Context context){
		try{
			dbWrite=DbUtil.getWriteDb(context);
			dbWrite.execSQL("delete from alarm_phone where alarm_msgid='"+msgId+"'");
			
		}catch(SQLiteException e){
			e.printStackTrace();
		}
		return "删除成功";
	}
	
	/**
	 * 百度云推送                  
	 * 用户请求获取数据
	 * **/
	public static boolean conUpdateDbPhoneAlarmByBaiDuClound(Context context,String imei,String timemark) throws Exception{
		ArrayList<HashMap<String, String>> up_alarm_list = new ArrayList<HashMap<String,String>>();
		NetConnUtil netconn = new NetConnUtil();
		XMLParse xml = XMLParse.getInstance();
		String ei = IConnPars.PARS_IMEI + imei;
		//String bgin = IConnPars.PARS_BEGIN + begin;
		String tmark = IConnPars.PARS_TIME_MARK + timemark;
		String pars = NetConnUtil.setPars(ei,tmark);
		
		ArrayList<HashMap<String, String>> list = null;
		ParseDatas parse = DataController.getAlarmInfoData(pars);
		if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), context)){
				list = new ArrayList<HashMap<String,String>>();
			} else {
				list = parse.getList();
			}
		} else {
			MessDialog.show(context.getResources().getString(R.string.server_unusual_msg), context);
			list = new ArrayList<HashMap<String,String>>();
		}
		
		Contents.getInstance().setAlarmCount(list.size());//设置推送的新报警信息
		if(null != list && list.size()!= 0){
			dbWrite=DbUtil.getWriteDb(context);
			dbWrite.beginTransaction();
			for(int i=0;i<list.size();i++){
				HashMap<String, String> alarm_map=list.get(i);
				dbWrite.execSQL("insert into alarm_phone(id,dp_name,jd_name,dpid,alarm_time ,alarm_msgid ,alarm_level ,alarm_title ,alarm_text ,alarm_timemark,sessionid,alarm_status) values (?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[]{DbHelper.getUuid(),alarm_map.get("dp_name"),alarm_map.get("jd_name"),alarm_map.get("dpid"),alarm_map.get("alarm_time"),alarm_map.get("alarm_msgid"),alarm_map.get("alarm_level"),alarm_map.get("alarm_title"),alarm_map.get("bjtext"),alarm_map.get("alarm_timemark"),AbsApplication.getInstance().getSessionid(),"noread"});
				up_alarm_list.add(alarm_map);
			}
			DbHelper.update_alarm_list = up_alarm_list;
			dbWrite.setTransactionSuccessful();
			dbWrite.endTransaction();
			return true;
		}
		return false;
	}
	
	public static String getMaxTimemask(Context context){
		SQLiteDatabase db = DbUtil.getReadDb(context);
		Cursor cur=db.rawQuery("select max(alarm_timemark) from alarm_phone", null);
		if(cur.getCount()==0){
			return null;
		}
		cur.moveToNext();
		String timemask = String.valueOf(cur.getInt(0));
		Log.i("Timemask", timemask);
		cur.close();
		db.close();
		return timemask;
	}
}
