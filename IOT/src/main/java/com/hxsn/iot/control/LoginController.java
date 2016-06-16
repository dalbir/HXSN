package com.hxsn.iot.control;

import android.content.Context;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.data.IConnPars;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.data.TestGetData;
import com.hxsn.iot.util.XMLParse;
import com.hxsn.iot.model.User;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginController {

	public static final String LOGIN_XML = "com/snsoft/aiot/phone/xml/login.xml";
	public static final String BJXX_XML = "com/snsoft/aiot/phone/xml/bjxx.xml";
	public static final String BJXQ_XML = "com/snsoft/aiot/phone/xml/bjxq.xml";
	public static final String QXSJ_XML = "com/snsoft/aiot/phone/xml/qxsj.xml";
	public static final String EXPERTXX_XML = "com/snsoft/aiot/phone/xml/expertxx.xml";
	public static final String EXPERTXQ_XML = "com/snsoft/aiot/phone/xml/expertxx.xml";
	public static final Boolean isTest = true;
	private static Context context;

	public LoginController() {
	}

	public LoginController(Context context) {

		this.context = context;
	}
	
	//已废弃
	public static User getLoginData() {

		TestGetData data = new TestGetData();
		NodeList nodelist = data.getXmlFile(LOGIN_XML);

		XMLParse xml = XMLParse.getInstance();
		User user = xml.parserLogin(nodelist);

		return user;
	}

//	/**
//	 * 报警信息的解析
//	 * */
//	public static ArrayList<HashMap<String, String>> getBjxxData(String dpId) {
//		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
//		TestGetData data = new TestGetData();
//		NodeList nodelist;
//		nodelist = data.getXmlFile(BJXX_XML);
//		XMLParse xml = XMLParse.getInstance();
//		list = xml.parserAlarmList(nodelist);
//		return list;
//
//	}

//	/**
//	 * 报警详情查询
//	 * */
//	public static HashMap<String, String> getBjxqData(String msgid) {
//
//		HashMap<String, String> map = new HashMap<String, String>();
//		TestGetData data = new TestGetData();
//		NodeList nodelist;
//		nodelist = data.getXmlFile(BJXQ_XML);
//		XMLParse xml = XMLParse.getInstance();
//		map = xml.parserAlarmContent(nodelist);
//
//		return map;
//
//	}

	/**
	 * 获取曲线信息
	 * */
	public static HashMap<String, Object> getQxData(String dpId) {

		HashMap<String, Object> map = null;
		TestGetData data = new TestGetData();
		NodeList nodelist;
		if (DataController.isTest) {
			nodelist = data.getXmlFile(DataController.QXSJ_XML);
			if(DataController.isLocal){
				nodelist = data.getXmlFile(DataController.BASE_URI+DataController.QXSJ_XML);
			} else {
				nodelist = data.getXmlData(DataController.QXSJ_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_QXCSH+"&"
					+ IConnPars.PARS_SESSIONID
					+ AbsApplication.getInstance().getSessionid()+"&"
					+ IConnPars.PARS_DYUUID + dpId + "&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserLineDatas(nodelist);
		}
		return map;
	}

	/**
	 * 获取专家信息          废弃
	 * **/
	public static List<Map<String, String>> getExpertDataList() {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		TestGetData data = new TestGetData();
		NodeList nodelist = data.getXmlFile(EXPERTXX_XML);
		
		XMLParse xml = XMLParse.getInstance();
		ParseDatas pard = xml.parserexpertList(nodelist);
		list = (List<Map<String, String>>) pard.getObject();

		return list;
	}


}
