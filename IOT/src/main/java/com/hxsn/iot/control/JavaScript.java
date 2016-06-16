package com.hxsn.iot.control;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hxsn.iot.R;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.model.ControlDevices;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.GrowthCycle;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.model.QxEntity;
import com.hxsn.iot.util.NetConnUtil;
import com.hxsn.iot.util.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhjx
 * android调用js的对象方法
 */
public class JavaScript{
	private ArrayList<String> devIdlist;
	private ArrayList<HashMap<String,String>> devIdMap;
	private String dyId;
	private Context context;
	
	public JavaScript(String dyId,Context context){
		this.dyId = dyId;
		this.context = context;
		devIdlist = new ArrayList<String>();//解析出的设备id列表
		devIdMap = new ArrayList<HashMap<String,String>>();//解析出的每个设备状态
	}
	
	/*
	 * 得到控制设备的xml信息
	 */
	public String getControlDevice(){
		return DataController.getControlDevice(dyId);
	}
	//单棚控制根据单元id获得设备列表，参数：单元id
	public String getDevices() {
		StringBuffer sb = new StringBuffer();
		ControlDevices devices = DataController.getControlData(dyId);
		
		if(devices != null){
			String code = devices.getCode();
			if(!"200".equals(code)){
				if("201".equals(code)){
					NetworkUtil.logout(context);
				}else{
					devices = new ControlDevices();
					Toast.makeText(context, context.getResources().getString(R.string.server_unusual_msg), Toast.LENGTH_LONG).show();
				}
			}
		} else {
			MessDialog.show(context.getResources().getString(R.string.server_unusual_msg), context);
			devices = new ControlDevices();
		}
		
		for(int i=0; i<devices.getList().size(); i++){
			ArrayList<HashMap<String,String>> hashMap = (ArrayList<HashMap<String,String>>)(devices.getList().get(i).get("status"));
			int num = hashMap.size();
			sb.append(devices.getList().get(i).get("deviceName").toString()+num+",");
			devIdlist.add(devices.getList().get(i).get("deviceId").toString());
			
			HashMap<String,String> map = new HashMap<String,String>();
			for (int j = 0; j < num; j++) {
				map.put("id"+j, hashMap.get(j).get("id"));
				map.put("name"+j, hashMap.get(j).get("name"));
			}
			devIdMap.add(map);
		}
		String str = sb.toString();
		str = str.substring(0,str.length()-1);
		return str;
	}
	
	/**
	 * 
	 * @param deviceid 设备ID
	 * @param statusid 状态ID
	 * @param timing 执行时间
	 * @param timingUnit 执行时间单位
	 * @return
	 */
	public String getDeviceControl(String deviceid,String statusid,String timing,String timingUnit) {
		HashMap<String, String> map = DataController.getSingleControl(dyId, deviceid, statusid, timing, timingUnit);
		String code = map.get("code");
		if(!"200".equals(code)){
			Toast.makeText(context, NetConnUtil.getServiceMsg(Integer.parseInt(code)), Toast.LENGTH_LONG).show();
		}
		return code;
	}
	
	//控制设备密码验证
	public String verifyPwd(String pwd) {
		HashMap<String,String> map = DataController.verifyPwdControl(pwd);
		
		if(map != null){
			if(NetworkUtil.isErrorCode(map.get("code"), context)){
				map = new HashMap<String,String>();
			} 
		} else {
			MessDialog.show(context.getResources().getString(R.string.server_unusual_msg), context);
			map = new HashMap<String,String>();
		}
		
		return map.get("code");
	}
	
	//废弃          群棚控制根据设备类型获得大棚列表    
	public String getShedsData(String type) {
		StringBuffer sb = new StringBuffer();
		List<JiDi> list = DataController.getShedsData(type);
		for (int i = 0; i < list.size(); i++) {
			String jDName = list.get(i).getName();
			List<Dapeng> listDp = list.get(i).getList();
			for (int j = 0; j < listDp.size(); j++) {
				String dPName = listDp.get(j).getName();
				List<Danyuan> listDy = listDp.get(j).getList();
				for (int k = 0; k < listDy.size(); k++) {
					String dYName = listDy.get(k).getName();
					sb.append(jDName).append(dPName).append(dYName).append(",");
				}
			}
		}
		return sb.toString();
	}
	
	//废弃        农事管理:作物生长周期
	public String getGrowthCycle(String dyId){
		GrowthCycle growth = DataController.getGrowthCycle(dyId);
		
		if(growth != null){
			if(NetworkUtil.isErrorCode(growth.getCode(), context)){
				growth = new GrowthCycle();
			} 
		} else {
			MessDialog.show(context.getResources().getString(R.string.server_unusual_msg), context);
			growth = new GrowthCycle();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(growth.getName()+";").append(growth.getTotalDays()+";"+
				growth.getCurrentDay()+";");
		for (int i = 0; i < growth.getGroupList().size(); i++) {
			HashMap<String,String> map = growth.getGroupList().get(i);
			sb.append("("+map.get("name")).append(","+map.get("type")).append(","+map.get("value")+")");
		}
		return sb.toString();
	}
	
	//废弃          农事活动列表
	public String getGrowthList(String username,String dyId){
		StringBuffer sb = new StringBuffer();
		ArrayList<HashMap<String,String>> list = null;
		ParseDatas parse = DataController.getGrowthList(username, dyId);
		
		if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), context)){
				list = new ArrayList<HashMap<String,String>>();
			} else {
				list = parse.getList();
			}
		} else {
			MessDialog.show(context.getString(R.string.server_unusual_msg), context);
			list = new ArrayList<HashMap<String,String>>();
		}
		
		for (int i = 0; i < list.size(); i++) {
			HashMap<String,String> map = list.get(i);
			sb.append(map.get("type")+",").append(map.get("name")+",").append(map.get("time")+",").
				append(map.get("mark")+";");
		}
		return sb.toString();
	}
	
	//废弃         农事活动项
	public String getGrowthTerm(){
		StringBuffer sb = new StringBuffer();
		ArrayList<String> list = DataController.getGrowthTerm();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)+",");
		}
		return sb.toString();
	}
	
	//废弃            添加农事活动
	public String getAddGrowth(String username,String dyId,String type, String mark){
		HashMap<String, String> map = DataController.getAddGrowth( username, dyId, type, mark);
		return map.get("code");
	}
	
	public String a() {
		return "haha";
	}
	
	//获取曲线信息
	
	public String getQxData(){
		
		LoginController lc = new LoginController();
		Map map = lc.getQxData(dyId);
		List<QxEntity> list = new ArrayList<QxEntity>();
		list = (List<QxEntity>) map.get("list");
		Gson gson = new Gson();
		String str = gson.toJson(list);
		Log.i("Qx","曲线数据:"+str);
		return str;
	}
	
	//废弃               获取专家列表
	
	public String getExperts(){
		LoginController lc = new LoginController();
		List<Map<String,String>> list = lc.getExpertDataList();
		Gson gson = new Gson();
		String str = gson.toJson(list);
		Log.i("Qx","曲线数据:"+str);
		return str;
	}
	

}
