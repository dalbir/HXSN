package com.hxsn.iot.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作为数据源传递，大棚
 */
public class Dapeng implements Parcelable{

	private String id;
	private String name;
	private String jidiname;
	private String jidiid;
	private String img;
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

	private ArrayList<Danyuan> list = new ArrayList<Danyuan>();
	
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
	public String getJidiname()
	{
		return jidiname;
	}
	public void setJidiname(String jidiname)
	{
		this.jidiname = jidiname;
	}
	public String getJidiid()
	{
		return jidiid;
	}
	public void setJidiid(String jidiid)
	{
		this.jidiid = jidiid;
	}
	
	public void addDanyuan(Danyuan danYuan){
    	list.add(danYuan);
    }
    
    public boolean removeDapeng(Danyuan danYuan){
    	int index =list.indexOf(danYuan);
    	if(-1 == index)
    		return false;
    	list.remove(index);
    	return true;
    }
    
    public ArrayList<Danyuan> getList(){
    	return list;
    }
	
	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeString(id);
		parcel.writeString(jidiid);
		parcel.writeString(jidiname);
		parcel.writeString(name);
	}
	
	public static final Creator<Dapeng> CREATOR = new Creator<Dapeng>(){

		@Override
		public Dapeng createFromParcel(Parcel parcel)
		{
			Dapeng dapeng = new Dapeng();
			dapeng.id = parcel.readString();
			dapeng.jidiid = parcel.readString();
			dapeng.jidiname = parcel.readString();
			dapeng.name = parcel.readString();
			return dapeng;
		}

		@Override
		public Dapeng[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new Dapeng[size];
		}
		
	};
}
