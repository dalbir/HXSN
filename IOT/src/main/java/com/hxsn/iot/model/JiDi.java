package com.hxsn.iot.model;

import java.util.ArrayList;

public class JiDi
{
	private String id;
	private String name;
	
    private ArrayList<Dapeng> list = new ArrayList<Dapeng>();
    
    public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void addDapeng(Dapeng dapeng){
    	list.add(dapeng);
    }
    
    public boolean removeDapeng(Dapeng dapeng){
    	int index =list.indexOf(dapeng);
    	if(-1 == index)
    		return false;
    	list.remove(index);
    	return true;
    }
    
    public ArrayList<Dapeng> getList(){
    	return list;
    }
}