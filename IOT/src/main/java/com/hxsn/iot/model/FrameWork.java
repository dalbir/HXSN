/**
 * Copyright (c) 1992-2012 廊坊市大华夏神农信息技术有限公司-版权所有
 */
package com.hxsn.iot.model;

/**
 * 农事类
 * @author liuzhiyuan
 *
 */
public class FrameWork {

	private String id;
	private String name;
	
	public FrameWork(String id, String name){
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
