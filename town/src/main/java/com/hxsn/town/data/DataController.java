package com.hxsn.town.data;

import com.hxsn.town.model.ConditionListModel;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;


/*
 * 取得数据方法类，包含测试数据
 */
public class DataController {
	private static boolean isTest = false;
	public static final String ZBYQ_XML = "com/snsoft/phone/test/xml/zbyq.xml";
	public static final String ZBTJLB_XML = "com/snsoft/phone/test/xml/zbtjlb.xml";
	public static final String XXTS_XML = "com/snsoft/phone/test/xml/xxts.xml";
	
	//解析周边园区列表
	public static ArrayList<HashMap<String, String>> getRimParkData(double ltLon,double ltLat,double rbLon,double rbLat,String id){
		NetworkDataController data = new NetworkDataController();
		NodeList nodelist;
		ArrayList<HashMap<String, String>> list = null;
		if(isTest){
			nodelist = data.getXmlFile(ZBYQ_XML);
		} else {
			nodelist = data.getXmlData(IConnPars.CIRCUM_URL+IConnPars.METHOD_ZBYQ+"&"+IConnPars.PARS_LTLON+ltLon+"&"+IConnPars.PARS_LTLAT+ltLat+"&"+IConnPars.PARS_RBLON+rbLon+"&"+IConnPars.PARS_RBLAT+rbLat+"&"+IConnPars.PARS_ID+""+"&ctype=0");
		}
		if(nodelist != null){
			XMLParse xml = XMLParse.getInstance();
			list = xml.parserRimParkList(nodelist);
		}
		
		return list;
	}
	
	//周边模块搜索条件列表，目前只有百度地图自带
	public static ConditionListModel getConditionListData(){
		NetworkDataController data = new NetworkDataController();
		NodeList nodelist= data.getXmlFile(ZBTJLB_XML);

		XMLParse xml = XMLParse.getInstance();
		ConditionListModel model = xml.parserConditionList(nodelist);
		return model;
	}
	
	//推送消息
	public static HashMap<String,String> getPushData(){
		NetworkDataController data = new NetworkDataController();
		NodeList nodelist;
		if(isTest){
			nodelist = data.getXmlFile(XXTS_XML);
		} else {
			nodelist = data.getXmlData(IConnPars.CIRCUM_URL+"&ctype=0");
		}
		XMLParse xml = XMLParse.getInstance();
		HashMap<String,String> map = xml.parserPushData(nodelist);
		return map;
	}
	
}
