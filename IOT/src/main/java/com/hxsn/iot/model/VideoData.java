package com.hxsn.iot.model;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoData {
	
	private String code;
	private String func;
	private String ip;
	private String port;
	private String user;
	private String password;
	private ArrayList<HashMap<String,String>> list;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<HashMap<String, String>> getList() {
		return list;
	}
	public void setList(ArrayList<HashMap<String, String>> list) {
		this.list = list;
	}
	
}
