package com.hxsn.iot.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrowthCycle {

	private String code;
	private String name;
	private String totalDays;
	private List<HashMap<String,String>> groupList;
	private String currentDay;
	public GrowthCycle() {
		super();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(String totalDays) {
		this.totalDays = totalDays;
	}
	public List<HashMap<String, String>> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<HashMap<String, String>> groupList) {
		this.groupList = groupList;
	}
	public String getCurrentDay() {
		return currentDay;
	}
	public void setCurrentDay(String currentDay) {
		this.currentDay = currentDay;
	}
	
}
