/**
 * Copyright 2016 
 *
 *All right reserved
 *
*/
package com.hxsn.intelliwork.utils;



public class GetURL {
	/*****************后台服务器基址**********************************/
	/*****************除去登录接口，每个接口必须使用uid和app做为其余接口验证的必要参数****/
	//旧地址
	//public static String BASEURL="http://60.10.151.28:7700/ssnc";
	//新地址
	public static String BASEURL="http://60.10.69.100:8889/ssnc";
	
	/*****************登录模块**********************************/
	// ("logname", "peter"),("logpwd", "12345"),("app", "1") //1智慧农场 2农场管理 3工作人员
	public static String LOGIN=BASEURL+"/app/login.json?app=3&";
    //{"code":200,"result":"40288ca151be12490151be12aefa0002" }
	
	public static String GETSERVERS=BASEURL+"/app/getservices.json?app=3&uid=";
	/*****************我的模块****************************/	
    //意见反馈
	public static String YJFK=BASEURL+"/app/feedback.do?app=3&uid=";
	//获取个人信息
	public static String GETMYINFO=BASEURL+"/app/getpersonal.json?app=3&uid=";
    //更新头像
	public static String UPDATEHEAD=BASEURL+"/app/setpersonal.json";
	//更新昵称
	public static String UPDATENAME=BASEURL+"/app/setpersonal.json?app=3&uid=";
	//更新性别
	public static String UPDATESEX=BASEURL+"/app/setpersonal.json?app=3&uid=";
	//更新地址
	public static String UPDATEADDRESS=BASEURL+"/app/setpersonal.json?app=3&uid=";
	// 关于农庄
	public static String ABOUTFARM = BASEURL + "/app/nongzhuang.do?app=3&uid=";
	public static String WELCOME = BASEURL + "/app/welcome.do?app=3&uid=";
 	/*****************追溯模块**********************************/	
    public static String  ZHUISU=BASEURL+"/app/ zhuisu.do?app=3&uid=";
	
	/*****************游园模块**********************************/	
    public static String  YOUYUAN=BASEURL+"/app/dikuai.do?app=3&uid=";
    public static String  DKFQ=BASEURL+"/app/dikuaifq.do?app=3&uid=";
    
    
    
    /*****************农事模块**********************************/	
    public static String  NONGSHI=BASEURL+"/app/zixun.do?app=3&uid=";
    public static String  DIKUAI=BASEURL+"/app/getdikuai.json?app=3&uid=";
    //提交参数app=3&uid=userid&dkid=G2A04
    public static String  ProductLL=BASEURL+"/app/lvli.do?app=3&uid=";
    
    
    /*****************采摘模块**********************************/	
    public static String  CAIZHAI=BASEURL+"/app/caizhai.do?app=3&uid=";
    
    
    /*****************作业模块**********************************/	
    
    //获取我的作业
    public static String  DK_NAME=BASEURL+"/app/getDkInfo.json";
    
    //提交作业登记数据接口
	//dkcodes hjname neirong filedata
    public static String  SUBMITWORK=BASEURL+"/app/addzuoye.json?";
	//获取我的作业
    public static String  GETMYWORK=BASEURL+"/app/myzuoye.do?app=3&uid=";
	//获取收获取接口 code
	//{"code":200,"result":{"name":"芹菜",//生成的商品名称 "lvnums":"5",//履历信息个数"units":"Kg"}}
	public static String SHSM=BASEURL+"/app/getHarvestInfo.json?app=3&uid=";
   /**提交收货接口
   code	地块编码，扫描的二维码内容
   name	商品名称
   lvnums	履历信息个数
   caidan	更新菜单主料：0不更新、1更新
   songcai	更新送菜清单：0不更新、1更新
   qrcode	打印二维码：0不打印、1打印（手持二维码打印机用）
   nums	收获数量**/
   public static String SUBMITSH=BASEURL+"/app/addHarvestInfo.json?";
   //采摘登记URL
   public static String CAIZHAIDJ=BASEURL+"/app/caizhaidj.do?app=3&uid=";
   //作业信息详情页面的URL
   //jobuuid
   //wst.javaFun(‘作业信息ID’,2);
    public static String ZYDETAIL=BASEURL+"/app/myzuoyexq.do?app=3&uid=";
    /*****************视频服务器地址**********************************/	
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
