/**
 * Copyright 2016 
 *
 *All right reserved
 *
 */
package com.hxsn.farmage.utils;

public class GetURL {
	/***************** 后台服务器基址 **********************************/
	/***************** 除去登录接口，每个接口必须使用uid和app做为其余接口验证的必要参数 ****/
	// public static String BASEURL="http://60.10.151.28:7700/ssnc";
	// 新地址
	public static String BASEURL = "http://60.10.69.100:8889/ssnc";
	// 内网地址
	// public static String BASEURL="http://192.168.15.3:8889/ssnc";
	/***************** 登录模块 **********************************/
	// ("logname", "peter"),("logpwd", "12345"),("app", "2") //1智慧农场 2农场管理 3工作人员
	public static String LOGIN = BASEURL + "/app/login.json?app=2&";
	// {"code":200,"result":"40288ca151be12490151be12aefa0002" }

	public static String GETSERVERS = BASEURL + "/app/getservices.json?app=2&uid=";

	/***************** 我的模块 ****************************/
	// 意见反馈
	public static String YJFK = BASEURL + "/app/feedback.do?app=2&uid=";
	// 获取个人信息
	public static String GETMYINFO = BASEURL
			+ "/app/getpersonal.json?app=2&uid=";
	// 更新头像
	public static String UPDATEHEAD = BASEURL + "/app/setpersonal.json";
	// 更新昵称
	public static String UPDATENAME = BASEURL
			+ "/app/setpersonal.json?app=2&uid=";
	// 更新性别
	public static String UPDATESEX = BASEURL
			+ "/app/setpersonal.json?app=2&uid=";
	// 更新地址
	public static String UPDATEADDRESS = BASEURL
			+ "/app/setpersonal.json?app=2&uid=";
	// 关于农庄
	public static String ABOUTFARM = BASEURL + "/app/nongzhuang.do?app=2&uid=";
	public static String WELCOME = BASEURL + "/app/welcome.do?app=2&uid=";

	/***************** 监控模块 **********************************/
	public static String YOUYUAN = BASEURL + "/app/dikuai.do?app=2&uid=";
	public static String DKFQ = BASEURL + "/app/dikuaifq.do?app=2&uid=";
	public static String DZDAOHANG = BASEURL + "/app/dzyouyuan.do?app=2&uid=";
	//story.do
    public static String  MyStory=BASEURL+"/app/story.do?app=2&uid=";

	/******************* --管理模块-- *******************/
	public static String ProductStatus = BASEURL
			+ "/app/chanpinzt.do?app=2&uid=";
	public static String NongShiWork = BASEURL + "/app/nongshizy.do?app=2&uid=";
	public static String MenJin = BASEURL + "/app/menjinjk.do?app=2&uid=";
	public static String AnFang = BASEURL + "/app/anfangbj.do?app=2&uid=";

	/***************** 农事模块 **********************************/
	public static String DIKUAI = BASEURL + "/app/getdikuai.json?app=2&uid=";
	// 提交参数app=2&uid=userid&dkid=G2A04
	public static String ProductLL = BASEURL + "/app/lvli.do?app=2&uid=";
	/***************** 追溯模块 **********************************/
	public static String ZHUISU = BASEURL + "/app/zhuisu.do?app=2&uid=";
	public static String ZHUISUNAV = BASEURL + "/app/cplist.do?app=2&uid=";

	/***************** 采摘模块 **********************************/
	public static String CAIZHAI = BASEURL + "/app/caizhai.do?app=2&uid=";

	/***************** 视频服务器地址 **********************************/
	//旧视频地址
    //public static String  VideoURL="http://60.10.151.28";
    //新视频地址--外网
  //  public static String  VideoURL="http://60.10.69.100";
    public static String VideoIP="60.10.69.100";
    //内网视频地址
    //public static String  VideoIP="192.168.15.2";
    public static String  VideoName="appsystem";
    public static String  VideoPwd="a12345678";
    //
    
    //旧密码
    //public static String  VideoName="system";
    //public static String  VideoOldPwd="hxsn1234";

}
