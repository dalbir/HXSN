package com.hxsn.town.data;

public class IConnPars {
	public static final String URL = "http://xiangwang.info";//o2o.inagri.cn  / xiangwang.org.cn/m/index.shtml
	//public static final String URL = "http://192.168.12.41:8080/o2o";//o2o.inagri.cn  / xiangwang.org.cn/m/index.shtml
	//图片上传地址
	public static final String UPLOAD_URL = URL+"/uploadImg.json";
	//图片上传返回地址，这个地址随便定义没有实际意义
	public static final String IMAGE_RETURN_CODE = URL+"/uploadImgReturnCode";
	//主页地址
	public static final String HOME_PAGE_URL = URL + "/m/index.shtml";
	//园区地址
	public static final String PARK_PAGE_URL = URL + "/m/yardlist.shtml";
	//购物车地址
	public static final String SHOP_CART_URL = URL + "/m/shopcart.shtml";
	//我地址
	public static final String MY_PAGE_URL = URL + "/m/uc/usercenter.shtml";
	//周边地址
	public static final String CIRCUM_URL = URL + "/api.shtml?";
	
	//周边园区方法名
	public static final String METHOD_ZBYQ = "func=zbyq";
	//周边条件列表方法名
	public static final String METHOD_ZBTJLB = "func=zbtjlb";
	//搜索周边园区列表方法名
	public static final String METHOD_ZBYQLB = "func=zbyqlb";
	
	//id参数名
	public static final String PARS_ID = "id=";
	//左上经度参数名
	public static final String PARS_LTLON = "ltlon=";
	//左上纬度参数名
	public static final String PARS_LTLAT = "ltlat=";
	//右下经度参数名
	public static final String PARS_RBLON = "rblon=";
	//右下纬度参数名
	public static final String PARS_RBLAT = "rblat=";
	//支付成功跳转页面
	public static final String SUCCESS = URL +  "/m/uc/success.shtml";
	//支付失败跳转页面
	public static final String FAIL = URL +  "/m/uc/fail.shtml";
	//响应异常跳转页面
	public static final String WAIT = URL + "/m/uc/wait.shtml";
	
}
