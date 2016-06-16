package com.hxsn.town.data;

public class Contents {
	private static Contents instance;
	private String url;
	
	private Contents(){}
	public static Contents getInstance(){
		if(instance == null){
			instance = new Contents();
		}
		return instance;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
