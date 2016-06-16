package com.hxsn.iot.model;

/**
 * @author zjx
 * 传感器
 */
public class Sensor
{
	private String name;
	private String value;
	/** 单位*/
	private String unit;
	private String type;
	private int strid;
	//是否正常值
	private String normal;
	
	public String getNormal(){
		return normal;
	}
	
	public void setNormal(String normal){
		this.normal = normal;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getUnit()
	{
		return unit;
	}
	/**
	 * @param unit 单位
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}
	/**
	 * @return 检测器类型
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type 检测器类型
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * @return 监测器标题资源string.xml
	 */
	public int getStrid()
	{
		return strid;
	}
	/**
	 * @param strid  监测器标题资源string.xml
	 */
	public void setStrid(int strid)
	{
		this.strid = strid;
	}
}
