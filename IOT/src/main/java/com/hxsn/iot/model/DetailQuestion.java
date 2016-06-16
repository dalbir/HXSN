package com.hxsn.iot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailQuestion {
	private String code;
	private String name;
	private String content;
	private List<HashMap<String,String>> imgList;
	private ArrayList<HashMap<String,String>> groupList;
	public DetailQuestion() {
		super();
		// TODO Auto-generated constructor stub
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<HashMap<String, String>> getImgList() {
		return imgList;
	}
	public void setImgList(List<HashMap<String, String>> imgList) {
		this.imgList = imgList;
	}
	public ArrayList<HashMap<String, String>> getGroupList() {
		return groupList;
	}
	public void setGroupList(ArrayList<HashMap<String, String>> groupList) {
		this.groupList = groupList;
	}
	
}
