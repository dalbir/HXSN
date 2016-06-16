package com.hxsn.iot.model;

public class User{
	private String uid;
	private String uname;
	private String status;
	private String controlPwd;
	
	public String getControlPwd(){
		return controlPwd;
	}
	
	public void setControlPwd(String controlPwd){
		this.controlPwd = controlPwd;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String sessionid;
	
	public String getSessionid()
	{
		return sessionid;
	}
	public void setSessionid(String sessionid)
	{
		this.sessionid = sessionid;
	}
	public String getUid()
	{
		return uid;
	}
	public void setUid(String uid)
	{
		this.uid = uid;
	}
	public String getUname()
	{
		return uname;
	}
	public void setUname(String uname)
	{
		this.uname = uname;
	}
}
