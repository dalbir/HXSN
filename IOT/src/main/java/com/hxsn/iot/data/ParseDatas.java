package com.hxsn.iot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseDatas implements Serializable
{
	private String code;
	private String func;
	private String data;
	private Object object;
	
	private HashMap<String, String> map = new HashMap<String, String>();
//	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String, String>> list =new ArrayList<HashMap<String, String>>();
	
	public String getCode()
	{
		return code;
	}
	
	public void setCode(String code)
	{
		this.code = code;
	}
	
	public String getFunc()
	{
		return func;
	}
	
	public void setFunc(String func)
	{
		this.func = func;
	}
	
	public String getData()
	{
		return data;
	}
	
	public void setData(String data)
	{
		this.data = data;
	}
	
	public Object getObject(){
		return object;
	}
	
	public void setObject(Object object){
		this.object = object;
	}
	
	public HashMap<String, String> getMap()
	{
		return map;
	}
	
	public void setMap(HashMap<String, String> map)
	{
		this.map = map;
	}
	
	public ArrayList<HashMap<String, String>> getList()
	{
		return list;
	}
	
	public void setList(ArrayList<HashMap<String, String>> list)
	{
		this.list = list;
	}
	
}
