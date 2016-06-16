/**android本地的注册的对象*/
var andrFun = window.android;
//测试
function a(){
	andrFun.a();
}

/*根据单元ID获得设备列表，返回以逗号区分的单元名称*/
function getDevices(dyId){
	andrFun.getDevices(dyId);
}

/*
 单个设备控制返回，参数：单元id,控制方式（例上卷帘），设备id,控制时间
 返回：code码
*/
function getDeviceControl(dyId,type,devId,time){
	andrFun.getDeviceControl(dyId,type,devId,time);
}

/*进入控制界面密码验证，参数：输入的密码；返回：code码*/
function verifyPwd(pwd){
	andrFun.verifyPwd(pwd);
}

/*群棚控制根据设备类型返回大棚列表,返回以逗号区分的字符串*/
function getShedsData(type){
	andrFun.getShedsData(type);
}

/*
农事活动：作物根据单元ID返回作物生长周期
返回：作物名称；总天数；当前天数；（name,type,value）（name,type,value）
*/
function getGrowthCycle(dyId){
	andrFun.getGrowthCycle(dyId);
}

/*
根据用户名和单元ID返回农事活动列表
返回：type,name,time,mark;type,name,time,mark;
*/
function getGrowthList(username,dyId){
	andrFun.getGrowthList(username, dyId);
}

/*农事活动项*/
function getGrowthTerm(){
	andrFun.getGrowthTerm();
}

/*
添加农事活动，参数：用户名，单元ID,活动类型,备注
返回：code码
*/
function getAddGrowth(username,dyId,type,mark){
	andrFun.getAddGrowth(username, dyId, type, mark);
}

