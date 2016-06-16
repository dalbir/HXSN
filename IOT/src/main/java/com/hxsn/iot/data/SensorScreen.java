package com.hxsn.iot.data;


import com.hxsn.iot.model.Sensor;

import java.util.ArrayList;

/**
 * @author zjx
 * 监测屏
 */
public class SensorScreen {
	
	/**
	 * 传感器集
	 */
	private ArrayList<Sensor> list = new ArrayList<Sensor>();
	private String date;
	private String growth;
	
	public void setGrowth(String growth){
		this.growth = growth;
	}
	
	public String getGrowth(){
		return growth;
	}

	public void addSensor(Sensor sensor){
		list.add(sensor);
	}
	
	public boolean removeSensor(Sensor sensor){
		int index = list.indexOf(sensor);
		if(-1 == index)
			return false;
		list.remove(index);
		return true;
	}

	public void removeAll(){
		if(list.size()==0)
			return;
		list.clear();
	}
	
	/**
	 * @return 传感器集 判断size是否为0
	 */
	public ArrayList<Sensor> getList(){
		return list;
	}
	
	public Sensor getSensorbyName(String name){
		if(0 == list.size())
			return null;
		for(int i=0;i<list.size();i++){
			Sensor sensor =list.get(i);
			if(name.endsWith(sensor.getName()))
					return sensor;
		}
		return null;
	}
	
	/**
	 * 设置日期
	 * @param date
	 */
	public void setDate(String date){
		this.date = date;
	}
	
	public String getDate(){
		return date;
	}
	
}