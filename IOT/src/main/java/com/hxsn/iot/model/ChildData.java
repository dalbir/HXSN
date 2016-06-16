package com.hxsn.iot.model;


public class ChildData {
	
	private String childName;
	
	private boolean childSelected;
	
	private int childImage;
	
	private Danyuan danYuan;
	
	public Danyuan getDanYuan() {
		return danYuan;
	}
	
	public void setDanYuan(Danyuan danYuan){
		this.danYuan = danYuan;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public boolean isChildSelected() {
		return childSelected;
	}

	public void setChildSelected(boolean childSelected) {
		this.childSelected = childSelected;
	}

	public int getChildImage() {
		return childImage;
	}

	public void setChildImage(int childImage) {
		this.childImage = childImage;
	}
	
}