package com.hxsn.iot.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作为数据源传递,单元
 */
public class Danyuan implements Parcelable{

	private String id;
	private String name;
	private String devicesId;
	private String image;
	private String crop;
	private String dapengName;
	private String dapengId;
	private String jidiName;
	private String jidiId;
	private ArrayList<DevicesModel> list = new ArrayList<DevicesModel>();
	
	public ArrayList<DevicesModel> getList(){
		return list;
	}
	
	public void addDeviceModel(DevicesModel device){
    	list.add(device);
    }
    
    public boolean removeDeviceModel(DevicesModel device){
    	int index =list.indexOf(device);
    	if(-1 == index)
    		return false;
    	list.remove(index);
    	return true;
    }
	
	public String getDevicesId() {
		return devicesId;
	}
	public void setDevicesId(String devicesId) {
		this.devicesId = devicesId;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCrop() {
		return crop;
	}

	public void setCrop(String crop) {
		this.crop = crop;
	}
	
	public String getJidiName() {
		return jidiName;
	}
	public void setJidiName(String jidiName) {
		this.jidiName = jidiName;
	}
	public String getJidiId() {
		return jidiId;
	}
	public void setJidiId(String jidiId) {
		this.jidiId = jidiId;
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
	public String getDapengName()
	{
		return dapengName;
	}
	public void setDapengName(String dapengName)
	{
		this.dapengName = dapengName;
	}
	public String getDapengId()
	{
		return dapengId;
	}
	public void setDapengId(String dapengId)
	{
		this.dapengId = dapengId;
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
		parcel.writeString(dapengId);
		parcel.writeString(dapengName);
		parcel.writeString(name);
		parcel.writeString(jidiName);
		parcel.writeString(jidiId);
	}
	
	public static final Creator<Danyuan> CREATOR = new Creator<Danyuan>(){

		@Override
		public Danyuan createFromParcel(Parcel parcel)
		{
			Danyuan danyuan = new Danyuan();
			danyuan.id = parcel.readString();
			danyuan.dapengId = parcel.readString();
			danyuan.dapengName = parcel.readString();
			danyuan.name = parcel.readString();
			danyuan.jidiName = parcel.readString();
			danyuan.jidiId = parcel.readString();
			return danyuan;
		}

		@Override
		public Danyuan[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new Danyuan[size];
		}
		
	};
}
