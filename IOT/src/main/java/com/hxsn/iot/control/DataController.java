package com.hxsn.iot.control;

import android.util.Log;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.data.IConnPars;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.data.TestGetData;
import com.hxsn.iot.model.VideoData;
import com.hxsn.iot.util.XMLParse;
import com.hxsn.iot.model.FrameWork;
import com.hxsn.iot.model.ControlDevices;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.DetailQuestion;
import com.hxsn.iot.model.GrowthCycle;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.model.User;
import org.w3c.dom.NodeList;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * 网络数据控制。isTest:是否测试状态。isLocal:是否本地测试。
 */
public class DataController {
	public static final String TAG = "DataController";
	public static boolean isTest = false;
	public static boolean isLocal = false;
	public static boolean isOneTest = false;//第一轮未完成项用本地假数据（完成项包括：登录、园区、监测、控制、生长期、曲线和视频）
	
	public static final String DAPENG_XML = "dapeng.xml";
	public static final String MONITOR_XML = "monitor.xml";
	public static final String CONTROL_XML = "control.xml";
	public static final String SINGLE_CONTROL = "single_control.xml";
	public static final String SHEDS_XML = "sheds.xml";
	public static final String SHEDS_DEVICES = "shedsDevices.xml";
	public static final String UPDATE_XML = "update.xml";
	public static final String SHEDS_CONTROL_XML = "shedsControl.xml";
	public static final String WEATHER_POST_XML = "weatherPost.xml";
	public static final String GROWTH_CYCLE = "growth.xml";
	public static final String GROWTH_TERM = "growthTerm.xml";
	public static final String GROWTH_ADD = "growthAdd.xml";
	public static final String GROWTH_LIST = "growthList.xml";
	public static final String EXPERT_INFO_LIST = "expertInfoList.xml";
	public static final String EXPERT_INFO_CHANGE = "expertInfoChange.xml";
	public static final String EXPERT_INFO_LIST_ONLINE = "expertInfoListOnline.xml";
	public static final String EXPERT_QUESTION_LIST = "expertQuestionList.xml";
	public static final String EXPERT_QUESTION_DETAIL = "expertQuestionDetail.xml";
	public static final String EXPERT_MY_QUESTION = "my_question.xml";
	public static final String EXPERT_PRODUCT_LIST = "expertProductList.xml";
	public static final String EXPERT_DISEASE_LIST = "expertDiseaseList.xml";
	public static final String EXPERT_DISEASE_QUESTION_LIST = "expertDiseaseQuestionList.xml";
	public static final String EXPERT_DISEASE_QUESTION_DATA = "expertDiseaseQuestionData.xml";
	public static final String EXPERT_HOT_QUESTION_LIST = "expertHotQuestionList.xml";
	public static final String SETTING_FEEDBACK = "feedback.xml";
	public static final String SETTING_RESET_PASSWORD = "resetpwd.xml";
	public static final String LOGIN_RETURN = "login.xml";
	public static final String RESET_CONTROL_PASSWORD = "resetcontrolpwd.xml";
	public static final String QXSJ_XML = "qxsj.xml";
	public static final String BJXX_XML = "bjxx.xml";
	public static final String BJXQ_XML = "bjxq.xml";
	public static final String VIDEO_SHOW = "videoShow.xml";

	public static String BASE_URI = "com/snsoft/aiot/phone/xml/";
	public static String SYMBOL = "&";
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @param imei
	 * @return
	 */
	public static User getLoginData(String username,String password,String imei){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		User user = null;
		String url = "";
		if(isTest){
			if(isLocal){
				url = BASE_URI+LOGIN_RETURN;
				nodelist = data.getXmlFile(url);
			} else {
				url = BASE_URI+LOGIN_RETURN;
				nodelist = data.getXmlData(url);
			}
		} else {
			url = IConnPars.METHOD_LOGIN+SYMBOL+IConnPars.PARS_USERNAME+username+SYMBOL+IConnPars.PARS_PASSWORD+password+SYMBOL+IConnPars.PARS_IMEI+imei+"&ctype=0";
			nodelist = data.getXmlData(url);
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			user = xml.parserLogin(nodelist);
		}
		
		return user;
	}
	
	//获得软件更新数据
	public static HashMap<String,String> getUpdateData(int version){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String,String> map = null;
		if(isOneTest){
			nodelist = data.getXmlData(UPDATE_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+UPDATE_XML);
			} else {
				nodelist = data.getXmlData(UPDATE_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_UPDATE+SYMBOL+IConnPars.PARS_SESSIONID+ AbsApplication.getInstance().getSessionid()+SYMBOL+IConnPars.UPDATE_BB+version+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserUpdate(nodelist);
		}
		
		return map;
	}
	//获得基地大棚数据
	public static ParseDatas getJidiDapengData() {
		
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas pard = null;
//		ArrayList<JiDi> list = null;
		if (isTest) {
			nodelist = data.getXmlData(DAPENG_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+DAPENG_XML);
			} else {
				nodelist = data.getXmlData(DAPENG_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_JDDP+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			pard= xml.parserJidiDape(nodelist);
		}
		
		return pard;
	}
	//获得基地大棚数据code
	public static String getJidiDapengDataCode() {
		
		TestGetData data = new TestGetData();
		NodeList nodelist;
		String list = null;
		if (isTest) {
			nodelist = data.getXmlData(DAPENG_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+DAPENG_XML);
			} else {
				nodelist = data.getXmlData(DAPENG_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_JDDP+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			ParseDatas pard= xml.parserJidiDape(nodelist);
			list = (String) pard.getCode();
		}
		
	
		return list;
	}
	//通过基地和大棚名字获得单元列表
	public static List<Danyuan> getDanyuanOfDapeng(String jidi,String dapeng) {
		List<JiDi> list = (ArrayList<JiDi>)(getJidiDapengData().getObject());
		for (int i = 0; i < list.size(); i++) {
			String jd = list.get(i).getName();
			if(jd.equals(jidi)){
				for (int j = 0; j < list.get(i).getList().size(); j++) {
					String dp = list.get(i).getList().get(j).getName();
					if (dp.equals(dapeng)) {
						return list.get(i).getList().get(j).getList();
					}
				}
			}
		}
		return null;
	}
	//根据单元id获得单元对象
	public static Danyuan getDanyuanWithId(String id){
		List<JiDi> list = (ArrayList<JiDi>)(getJidiDapengData().getObject());
		for (int i = 0; i < list.size(); i++) {
			ArrayList<Dapeng> dpList = list.get(i).getList();
			for (int j = 0; j < list.get(i).getList().size(); j++) {
				ArrayList<Danyuan> dyList = dpList.get(j).getList();
				for (int k = 0; k < dyList.size(); k++) {
					if(id.equals(dyList.get(k).getId())){
						return dyList.get(k);
					}
				}
			}
		}
		return null;
	}
	
	//获得监测数据
	public static ParseDatas getDapeData(String dyuuid) {
		
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas pard = null;
		if (isTest) {
			nodelist = data.getXmlData(MONITOR_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+MONITOR_XML);
			} else {
				nodelist = data.getXmlData(MONITOR_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_JCSJ+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_DYUUID +dyuuid+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			pard = xml.parserDapeData(nodelist);
		}
		return pard;
	}
	//获得控制数据
	public static ControlDevices getControlData(String dyuuid) {
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ControlDevices pard = null;
		if (isTest) {
			nodelist = data.getXmlData(CONTROL_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+CONTROL_XML);
			} else {
				nodelist = data.getXmlData(CONTROL_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_KZQSL+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_DYUUID +dyuuid+"&ctype=0");
			System.out.println(nodelist);
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			pard = xml.parserDevices(nodelist);
		}
		return pard;
	}
	
	/**
	 * 得到控制设备信息
	 * @param dyuuid
	 * @return
	 */
	public static String getControlDevice(String dyuuid) {
		TestGetData data = new TestGetData();
		String xml = data.getXmlString(IConnPars.METHOD_KZQSL+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
				+IConnPars.PARS_DYUUID +dyuuid+"&ctype=0");
		Log.i(TAG, xml);
		return xml;
	}
	
	
	//获得单个设备控制状态
	public static HashMap<String, String> getSingleControl(String dyId,String deviceid,String statusid,String timing,String timingUnit) {
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String,String> map = null;
		if (isTest) {
			nodelist = data.getXmlData(SINGLE_CONTROL);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+SINGLE_CONTROL);
			} else {
				nodelist = data.getXmlData(SINGLE_CONTROL);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_SBKZ+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_DYUUID +dyId+SYMBOL+IConnPars.PARS_STATUSID + statusid+SYMBOL+IConnPars.PARS_DEVICEID +deviceid+SYMBOL+IConnPars.PARS_TIMING+timing+SYMBOL + IConnPars.PARS_TIME_UNIT +timingUnit+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserDeviceCtrl(nodelist);
		}
		return map;
	}
	
	//设备控制密码验证
	public static HashMap<String, String> verifyPwdControl(String password) {
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String,String> map = null;
		if (isTest) {
			nodelist = data.getXmlData(SINGLE_CONTROL);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+SINGLE_CONTROL);
			} else {
				nodelist = data.getXmlData(SINGLE_CONTROL);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_VERIFY_PWD+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_PASSWORD +password+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserVerifyPwd(nodelist);
		}
		return map;
	}
	
	//修改控制密码
	public static HashMap<String, String> resetControlPwd(String username,String oldpassword,String newpassword,String imei) {
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String,String> map = null;
		if (isOneTest) {
			nodelist = data.getXmlData(RESET_CONTROL_PASSWORD);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+RESET_CONTROL_PASSWORD);
			} else {
				nodelist = data.getXmlData(RESET_CONTROL_PASSWORD);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_XGKZMM+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_USERNAME+username+SYMBOL+IConnPars.PARS_OLDPASSWORD+oldpassword+SYMBOL+IConnPars.PARS_PASSWORD+newpassword+SYMBOL+IConnPars.PARS_IMEI+imei+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserControlRestPass(nodelist);
		}
		return map;
	}
	
	//群棚控制获得设备列表
	public static ControlDevices getShedsDevicesList(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ControlDevices device = null;
		if (isOneTest) {
			nodelist = data.getXmlData(SHEDS_DEVICES);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+SHEDS_DEVICES);
			} else {
				nodelist = data.getXmlData(SHEDS_DEVICES);
			}
		} else {
			//
			//负责传入的id,应作为参数传入，获得那个大棚的控制状态
			nodelist = data.getXmlData(IConnPars.METHOD_QPSBLB+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			device = xml.parserShedsDevicesList(nodelist);
		}
		return device;
	}
	
	//群棚控制获得大棚列表
	public static List<JiDi> getShedsData(String id) {
		
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ArrayList<JiDi> list = null;
		if (isOneTest) {
			nodelist = data.getXmlData(SHEDS_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+SHEDS_XML);
			} else {
				nodelist = data.getXmlData(SHEDS_XML);
			}
		} else {
			//
			//负责传入的id,应作为参数传入，获得那个大棚的控制状态
			nodelist = data.getXmlData(IConnPars.METHOD_QPKZLB+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_ID +id+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			ParseDatas pard= xml.parserShedsJidiDape(nodelist);
			list = (ArrayList<JiDi>) pard.getObject();
		}
		return list;
	}
	
	//群棚控制
	public static ParseDatas getShedsControl(String kind, String statusId, String sbsId,String time) {
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(SHEDS_CONTROL_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+SHEDS_CONTROL_XML);
			} else {
				nodelist = data.getXmlData(SHEDS_CONTROL_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_QPKZ+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_KIND +kind+SYMBOL+IConnPars.PARS_STATUSID+statusId+SYMBOL+IConnPars.PARS_SBSID+sbsId+SYMBOL+IConnPars.PARS_TIMING+time+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserShedsControl(nodelist);
		}
		return parse;
	}
	
	//获得气象哨
	public static ParseDatas getWeatherPost(String jdId){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas pard = null;
		if(isTest){
			nodelist = data.getXmlData(WEATHER_POST_XML);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+WEATHER_POST_XML);
			} else {
				nodelist = data.getXmlData(WEATHER_POST_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_JDQXS+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_JDUUID+jdId+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			pard = xml.parserWeatherPost(nodelist);
//			sensorScreen = (SensorScreen)pard.getObject();
		}
		return pard;
	}
	
	//获得作物生长周期
	public static GrowthCycle getGrowthCycle(String dyId){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		GrowthCycle growth = null;
		if(isTest){
			nodelist = data.getXmlData(GROWTH_CYCLE);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+GROWTH_CYCLE);
			} else {
				nodelist = data.getXmlData(GROWTH_CYCLE);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_SYQ+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_DYUUID+dyId+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			growth = xml.parserGrowthCycle(nodelist);
		}
		return growth;
	}
	
	//获得农事活动列表
	public static ParseDatas getGrowthList(String username,String dyId){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(GROWTH_LIST);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+GROWTH_LIST);
			} else {
				nodelist = data.getXmlData(GROWTH_LIST);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_NSHDLB+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_USERNAME+username+SYMBOL+IConnPars.PARS_DYUUID+dyId+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserGrowthList(nodelist);
		}
		return parse;
	}
	
	//农事活动项
	public static ArrayList<String> getGrowthTerm(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ArrayList<String> list = null;
		if(isOneTest){
			nodelist = data.getXmlData(GROWTH_TERM);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+GROWTH_TERM);
			} else {
				nodelist = data.getXmlData(GROWTH_TERM);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_TERM+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			list = xml.parserGrowthTerm(nodelist);
		}
		return list;
	}
	
	//农事活动项, 刘志远 2015/3/30
	public static ArrayList<FrameWork> getGrowthTermObjectList(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ArrayList<FrameWork> list = null;
		if(isOneTest){
			nodelist = data.getXmlData(GROWTH_TERM);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+GROWTH_TERM);
			} else {
				nodelist = data.getXmlData(GROWTH_TERM);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_TERM+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			list = xml.parserGrowthTermObject(nodelist);
		}
		return list;
	}
	
	//添加农事活动项
	public static HashMap<String, String> getAddGrowth(String username,String dyId,String type, String mark){
		try {
			username = URLEncoder.encode(username, "utf-8");
			type = URLEncoder.encode(type, "utf-8");
			mark = URLEncoder.encode(mark, "utf-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String, String> map = null;
		if(isOneTest){
			nodelist = data.getXmlData(GROWTH_ADD);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+GROWTH_ADD);
			} else {
				nodelist = data.getXmlData(GROWTH_ADD);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_ADD+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_USERNAME+username+SYMBOL+IConnPars.PARS_DYUUID+dyId+SYMBOL+IConnPars.PARS_TYPE+type+SYMBOL+IConnPars.PARS_MARK+mark+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserAddGrowth(nodelist);
		}
		return map;
	}
	
	//专家信息解析
	public static ParseDatas getExpertList(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_INFO_LIST);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_INFO_LIST);
			} else {
				nodelist = data.getXmlData(EXPERT_INFO_LIST);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_ZJXX+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserexpertList(nodelist);
		}
		return parse;
	}
	
	//专家信息是否变动
	public static HashMap<String, String> getInfoChange(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String, String> map = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_INFO_CHANGE);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_INFO_CHANGE);
			} else {
				nodelist = data.getXmlData(EXPERT_INFO_CHANGE);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_ZJXXBD+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.getInfIsChange(nodelist);
		}
		return map;
	}
	
	//在线专家信息
	public static ParseDatas getExpertListOnLine(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_INFO_LIST_ONLINE);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_INFO_LIST_ONLINE);
			} else {
				nodelist = data.getXmlData(EXPERT_INFO_LIST_ONLINE);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_ZXZJ+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserexpertList(nodelist);
		}
		return parse;
	}
	
	//我的问题列表
	public static ParseDatas getQuestionList(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_QUESTION_LIST);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_QUESTION_LIST);
			} else {
				nodelist = data.getXmlData(EXPERT_QUESTION_LIST);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_WTLB+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserQuestionList(nodelist);
		}
		return parse;
	}
	
	//问题详细内容
	public static DetailQuestion getDetailQuestion(String id){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		DetailQuestion question = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_QUESTION_DETAIL);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_QUESTION_DETAIL);
			} else {
				nodelist = data.getXmlData(EXPERT_QUESTION_DETAIL);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_WTXXLB+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_ID+id+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			question = xml.parserDetailQuestion(nodelist);
		}
		return question;
	}
	//我的提问
	public static HashMap<String, String> getMyQuestion(String expertId,String title,String content){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String, String> map = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_MY_QUESTION);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_MY_QUESTION);
			} else {
				nodelist = data.getXmlData(EXPERT_MY_QUESTION);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_WDTW+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_EXPERT_ID+expertId+SYMBOL+IConnPars.PARS_TITLE+title+SYMBOL+IConnPars.PARS_CONTENT+content+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserMyQuestion(nodelist);
		}
		return map;
	}
	
	//弄产品种类列表
	public static ParseDatas getProductList(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_PRODUCT_LIST);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_PRODUCT_LIST);
			} else {
				nodelist = data.getXmlData(EXPERT_PRODUCT_LIST);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_NCPLB+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserDiseaseType(nodelist);
		}
		return parse;
	}
	
	//病害列表
	public static ParseDatas getDiseaseList(String id){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_DISEASE_LIST);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_DISEASE_LIST);
			} else {
				nodelist = data.getXmlData(EXPERT_DISEASE_LIST);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_BHLB+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_ID+id+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserDiseaseType(nodelist);
		}
		return parse;
	}
	
	//病害问题列表
	public static ParseDatas getDiseaseQuestionList(String id){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_DISEASE_QUESTION_LIST);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_DISEASE_QUESTION_LIST);
			} else {
				nodelist = data.getXmlData(EXPERT_DISEASE_QUESTION_LIST);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_BHWT+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_ID+id+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserDiseaseType(nodelist);
		}
		return parse;
	}
	
	//问题内容
	public static HashMap<String,String> getDiseaseQuestionContent(String id){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String,String> map = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_DISEASE_QUESTION_DATA);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_DISEASE_QUESTION_DATA);
			} else {
				nodelist = data.getXmlData(EXPERT_DISEASE_QUESTION_DATA);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_BHNR+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_ID+id+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserDiseaseQuestion(nodelist);
		}
		return map;
	}
	
	//热点问题列表
	public static ParseDatas getHotQuestionList(){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas parse = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_HOT_QUESTION_LIST);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_HOT_QUESTION_LIST);
			} else {
				nodelist = data.getXmlData(EXPERT_HOT_QUESTION_LIST);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_RDWT+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			parse = xml.parserQuestionList(nodelist);
		}
		return parse;
	}
	
	//热点问题详细
	public static DetailQuestion getHotQuestionData(String id){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		DetailQuestion question = null;
		if(isOneTest){
			nodelist = data.getXmlData(EXPERT_QUESTION_DETAIL);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+EXPERT_QUESTION_DETAIL);
			} else {
				nodelist = data.getXmlData(EXPERT_QUESTION_DETAIL);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_WTXX+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_ID+id+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			question = xml.parserDetailQuestion(nodelist);
		}
		return question;
	}
	//系统设置意见反馈
	public static HashMap<String, String> getFeedback(String content,String contact){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String, String> map = null;
		if(isTest){
			nodelist = data.getXmlData(SETTING_FEEDBACK);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+SETTING_FEEDBACK);
			} else {
				nodelist = data.getXmlData(SETTING_FEEDBACK);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_YJFK+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_CONTENT+content+SYMBOL+IConnPars.PARS_CONTACT+contact+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserFeedback(nodelist);
		}
		return map;
	}
	
	//修改用户密码
	public static HashMap<String, String> getResetPwd(String username,String oldpassword,String password, String imei){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String, String> map = null;
		if(isTest){
			nodelist = data.getXmlData(SETTING_RESET_PASSWORD);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+SETTING_RESET_PASSWORD);
			} else {
				nodelist = data.getXmlData(SETTING_RESET_PASSWORD);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_XGMM+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_USERNAME+username+SYMBOL+IConnPars.PARS_OLDPASSWORD+oldpassword+SYMBOL+IConnPars.PARS_PASSWORD+password+SYMBOL+IConnPars.PARS_IMEI+imei+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserRestPass(nodelist);
		}
		return map;
	}
	//视频模块
	public static VideoData getVideoData(String dpId){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		VideoData mVideoData = null;
		if(isTest){
			nodelist = data.getXmlData(VIDEO_SHOW);
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+VIDEO_SHOW);
			} else {
				nodelist = data.getXmlData(VIDEO_SHOW);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_VIDEO+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()+SYMBOL
					+IConnPars.PARS_DPUUID+dpId+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			mVideoData = xml.parserVideoData(nodelist);
		}
		return mVideoData;
	}
	
	//报警信息的解析
	public static ParseDatas getAlarmInfoData(String pars){
		TestGetData data = new TestGetData();
		NodeList nodelist;
		ParseDatas mParseDatas = null;
		if(isOneTest){
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+BJXX_XML);
			} else {
				nodelist = data.getXmlData(BJXX_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_BJXX+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+pars+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			mParseDatas = xml.parserAlarmList(nodelist);
		}
		return mParseDatas;
	}
	
	/**
	 * 报警详情查询
	 * */
	public static HashMap<String, String> getAlarmInfoSearch(String pars) {
		
		TestGetData data = new TestGetData();
		NodeList nodelist;
		HashMap<String, String> map = null;
		if(isOneTest){
			if(isLocal){
				nodelist = data.getXmlFile(BASE_URI+BJXQ_XML);
			} else {
				nodelist = data.getXmlData(BJXQ_XML);
			}
		} else {
			nodelist = data.getXmlData(IConnPars.METHOD_BJXQ+SYMBOL+IConnPars.PARS_SESSIONID+AbsApplication.getInstance().getSessionid()
					+pars+"&ctype=0");
		}
		if(nodelist!=null){
			XMLParse xml = XMLParse.getInstance();
			map = xml.parserAlarmContent(nodelist);
		}

		return map;

	}
	
}
