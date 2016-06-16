package com.hxsn.iot.data;

public interface IConnPars{
	
	public static final int SUCCESS_OK =200;
	
	/** 登录方法名*/
	public static final String METHOD_LOGIN = "func=login";
	/** 注销方法名*/
	public static final String METHOD_LOGOUT = "func=logout";
	/** 修改密码方法名*/
	public static final String METHOD_XGMM = "func=xgmm";
	/** 基地大棚方法名*/
	public static final String METHOD_JDDP = "func=jddp";
	/** 基地气象哨方法名*/
	public static final String METHOD_JDQXS = "func=jdqxs";
	/** 监测数据方法名*/
	public static final String METHOD_JCSJ = "func=jcsj";
	/** 曲线初始化方法名*/
	public static final String METHOD_QXCSH = "func=qxcsh";
	/** 曲线数据方法名*/
	public static final String METHOD_QXSJ = "func=qxsj";
	/** 设备控制方法名*/
	public static final String METHOD_SBKZ = "func=sbkz";
	/** 设备控制器数量类型方法名*/
	public static final String METHOD_KZQSL = "func=kzqsl";
	/** 设备控制密码验证*/
	public static final String METHOD_VERIFY_PWD = "func=sbkzpwd";
	/** 修改控制密码*/
	public static final String METHOD_XGKZMM = "func=xgkzmm";
	/** 群棚控制设备列表*/
	public static final String METHOD_QPSBLB = "func=qpsblb";
	/** 群棚控制获得列表方法名*/
	public static final String METHOD_QPKZLB = "func=qpkzlb";
	/** 群棚控制方法名*/
	public static final String METHOD_QPKZ = "func=qpkz";
	/** 报警信息方法名*/
	public static final String METHOD_BJXX = "func=bjxx";
	/** 报警详情方法名*/
	public static final String METHOD_BJXQ = "func=bjxq";
	/** 生育期方法名*/
	public static final String METHOD_SYQ = "func=syq";
	/** 农事活动列表方法名*/
	public static final String METHOD_NSHDLB = "func=nshdlb";
	/** 农事活动项方法名*/
	public static final String METHOD_TERM = "func=nshdxm";
	/** 添加农事活动*/
	public static final String METHOD_ADD = "func=nshd";
	/** 专家信息列表*/
	public static final String METHOD_ZJXX = "func=zjxx";
	/** 在线专家列表*/
	public static final String METHOD_ZXZJ = "func=zxzj";
	/** 专家信息是否变动*/
	public static final String METHOD_ZJXXBD = "func=zjxxbd";
	/** 我的提问 */
	public static final String METHOD_WDTW = "func=wdtw";
	/** 问题列表 */
	public static final String METHOD_WTLB = "func=wtlb";
	/** 问题详细 */
	public static final String METHOD_WTXXLB = "func=wtxxlb";
	/** 农产品列表*/
	public static final String METHOD_NCPLB = "func=ncplb";
	/** 病害列表*/
	public static final String METHOD_BHLB = "func=bhlb";
	/** 病害问题列表*/
	public static final String METHOD_BHWT = "func=bhwt";
	/**病害内容*/
	public static final String METHOD_BHNR = "func=bhnr";
	/** 热点问题列表*/
	public static final String METHOD_RDWT = "func=rdwt";
	/** 热点问题详情*/
	public static final String METHOD_WTXX = "func=wtxx";
	/** 意见反馈*/
	public static final String METHOD_YJFK = "func=yjfk";
	/** 历史数据方法名*/
	public static final String METHOD_LSSJ = "func=lssj";
	/** 版本更新方法名*/
	public static final String METHOD_UPDATE = "func=bbgx";
	/** 视频监控方法名*/
	public static final String METHOD_VIDEO = "func=dpspmk";
	/** 软件更新版本号*/
	public static final String UPDATE_BB = "bb=";
	/** id参数名*/
	public static final String PARS_ID = "id=";
	/** 专家ID*/
	public static final String PARS_EXPERT_ID = "expertid=";
	/** 标题参数名*/
	public static final String PARS_TITLE = "title=";
	/** 内容参数名*/
	public static final String PARS_CONTENT = "content=";
	/** 联系方式*/
	public static final String PARS_CONTACT = "contact=";
	/** 用户名参数名*/
	public static final String PARS_USERNAME = "username=";
	/** 密码参数名*/
	public static final String PARS_PASSWORD = "password=";
	/** 旧密码参数名*/
	public static final String PARS_OLDPASSWORD = "oldpassword=";
	/** session_id参数名*/
	public static final String PARS_SESSIONID = "sessionid=";
	/** imei参数名*/
	public static final String PARS_IMEI = "imei=";
	/** jd_id参数名*/
	public static final String PARS_JDUUID = "jduuid=";
	/** dp_id参数名*/
	public static final String PARS_DPUUID = "dpuuid=";
	/** dy_id参数名*/
	public static final String PARS_DYUUID = "dyuuid=";
	/** 设备id*/
	public static final String PARS_SBSID = "sbsid=";
	/** 设备类型*/
	public static final String PARS_KIND = "kind=";
	/** 状态id*/
	public static final String PARS_STATUSID = "statusid=";
	/** 类型参数名*/
	public static final String PARS_TYPE = "type=";
	/** 备注参数名*/
	public static final String PARS_MARK = "mark=";
	/** 时间参数名*/
	public static final String PARS_DATE = "date=";
	/** 页码参数名*/
	public static final String PARS_PAGENO = "pageNo=";
	/** 每页条数参数名*/
	public static final String PARS_PAGESIZE = "pageSize=";
	/** 设备类型参数名*/
	public static final String PARS_DEVICE_TYPE = "deviceType=";
	/** 设备ID列表*/
	public static final String PARS_DEVICE_ID = "deviceid=";
	/** 开始时间参数名*/
	public static final String PARS_BEGIN = "begin=";
	/** 时间标记参数名*/
	public static final String PARS_TIME_MARK = "timemark=";
	/** 信息id参数名*/
	public static final String PARS_MESG_ID = "messageuuid=";
	/** 命令参数名*/
	public static final String PARS_CMD = "cmd=";
	/** 设备id参数名*/
	public static final String PARS_DEVICEID = "deviceid=";
	/** 时间参数名 */
	public static final String PARS_TIME = "time=";
	/** 设备类型*/
	public static final String PARS_SBTYPE ="sbtype=";
	/** 定时时长参数名*/
	public static final String PARS_TIMING = "timing=";
	/** 旧密码参数名*/
	public static final String PARS_OLD_PWD = "oldpwd=";
	/** 新密码参数名*/
	public static final String PARS_NEW_PWD = "newpwd=";
	/** 时间单位 */
	public static final String PARS_TIME_UNIT = "timingUnit=";

	public static final String RESPONSE_CODE101 = "请求参数无效";
	public static final String RESPONSE_CODE200 = "成功";
	public static final String RESPONSE_CODE201 = "登录超时";
	public static final String RESPONSE_CODE301 = "用户名或密码无效";
	public static final String RESPONSE_CODE302 = "密码错误";
	public static final String RESPONSE_CODE303 = "原始密码错误";
	public static final String RESPONSE_CODE401 = "设备不存在";
	public static final String RESPONSE_CODE402 = "设备离线";
	public static final String RESPONSE_CODE403 = "没有权限";
	public static final String RESPONSE_CODE404 = "控制服务没有运行";
	public static final String RESPONSE_CODE405 = "目前处于自动控制状态，不能进行远程控制";
	public static final String RESPONSE_CODE500 = "服务器发生数据库异常";
	public static final String RESPONSE_CODE505 = "未知异常";
	
	public static final String RESPONSE_CODE00 = "成功";
	public static final String RESPONSE_CODE01 = "服务器异常";
	public static final String RESPONSE_CODE02 = "参数异常";
	public static final String RESPONSE_CODE03 = "登录超时";
	public static final String RESPONSE_CODE04 = "原始密码错误";
	public static final String RESPONSE_CODE05 = "登录信息有误";
	public static final String RESPONSE_CODE06 = "设备不存在";
	public static final String RESPONSE_CODE07 = "设备离线";
	public static final String RESPONSE_CODE08 = "没有权限";
	public static final String RESPONSE_CODE09 = "密码错误";
	public static final String RESPONSE_CODE10 = "控制服务没有运行";
}
