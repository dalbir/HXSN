package com.hxsn.iot.model;

import java.util.HashMap;
import java.util.List;

public class ControlDevices {

	private String code;
	private List<HashMap<String,Object>> list;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<HashMap<String, Object>> getList() {
		return list;
	}
	public void setList(List<HashMap<String, Object>> list) {
		this.list = list;
	}
	
	
}
