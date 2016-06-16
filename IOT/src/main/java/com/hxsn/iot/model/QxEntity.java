package com.hxsn.iot.model;

public class QxEntity {

	private String type;
	private int ymax;
	private int ymin;
	private int alarmMax;
	private int alarmMin;
	private int interval;
	private String ydesc;
	private float[] line;
	private String colours;
	private String unit;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getYmax() {
		return ymax;
	}
	public void setYmax(int ymax) {
		this.ymax = ymax;
	}
	public int getYmin() {
		return ymin;
	}
	public void setYmin(int ymin) {
		this.ymin = ymin;
	}
	public int getAlarmMax() {
		return alarmMax;
	}
	public void setAlarmMax(int alarmMax) {
		this.alarmMax = alarmMax;
	}
	public int getAlarmMin() {
		return alarmMin;
	}
	public void setAlarmMin(int alarmMin) {
		this.alarmMin = alarmMin;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public String getYdesc() {
		return ydesc;
	}
	public void setYdesc(String ydesc) {
		this.ydesc = ydesc;
	}
	public float[] getLine() {
		return line;
	}
	public void setLine(float[] line) {
		this.line = line;
	}
	public String getColours() {
		return colours;
	}
	public void setColours(String colours) {
		this.colours = colours;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	

	
	
	
}
