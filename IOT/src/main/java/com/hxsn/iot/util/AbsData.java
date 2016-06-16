package com.hxsn.iot.util;

import com.hxsn.iot.model.Danyuan;

import java.util.ArrayList;


public class AbsData {
	//监测数据的比较
	public static final int MONITOR_COMPARE = 1;
	public static final int SHEDS_SURE = 2;
	
	private int type ;
	private ArrayList<Danyuan> data;
	public AbsData() {
		super();
	}
	public AbsData(int type, ArrayList<Danyuan> data) {
		super();
		this.type = type;
		this.data = data;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public ArrayList<Danyuan> getData() {
		return data;
	}
	public void setData(ArrayList<Danyuan> data) {
		this.data = data;
	}
	
}
