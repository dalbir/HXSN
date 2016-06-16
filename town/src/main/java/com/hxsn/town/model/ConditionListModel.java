package com.hxsn.town.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ConditionListModel {
	private String code;
	private String func;
	private ArrayList<String> categoryList;
	private ArrayList<ArrayList<HashMap<String,String>>> eventList;
	
	public ConditionListModel() {
		super();
	}

	public ConditionListModel(String code, String func,
			ArrayList<String> categoryList,
			ArrayList<ArrayList<HashMap<String, String>>> eventList) {
		super();
		this.code = code;
		this.func = func;
		this.categoryList = categoryList;
		this.eventList = eventList;
	}

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

	public ArrayList<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(ArrayList<String> categoryList) {
		this.categoryList = categoryList;
	}

	public ArrayList<ArrayList<HashMap<String, String>>> getEventList() {
		return eventList;
	}

	public void setEventList(ArrayList<ArrayList<HashMap<String, String>>> eventList) {
		this.eventList = eventList;
	}
	
}
