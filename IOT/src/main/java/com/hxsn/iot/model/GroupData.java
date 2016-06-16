package com.hxsn.iot.model;

import java.util.List;


public class GroupData {
	
	private String groupName;
	
	private boolean groupSelected;
	
	private List<ChildData> items;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isGroupSelected() {
		return groupSelected;
	}

	public void setGroupSelected(boolean groupSelected) {
		this.groupSelected = groupSelected;
	}

	public List<ChildData> getItems() {
		return items;
	}

	public void setItems(List<ChildData> items) {
		this.items = items;
	}
	
	
}
