package com.hxsn.iot.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作为数据源传递,单元
 */
public class DevicesModel implements Parcelable{
	
	private String id;
	private String name;
	private String danyuanId;
	private String danyuanName;
	private String dapengName;
	private String dapengId;
	private String jidiName;
	private String jidiId;
	
	public String getDanYuanId(){
		return danyuanId;
	}
	
	public void setDanYuanId(String danyuanId){
		this.danyuanId = danyuanId;
	}
	
	public String getDanYuanName(){
		return danyuanName;
	}
	
	public void setDanYuanName(String danyuanName){
		this.danyuanName = danyuanName;
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
	
	public static final Creator<DevicesModel> CREATOR = new Creator<DevicesModel>(){

		@Override
		public DevicesModel createFromParcel(Parcel parcel)
		{
			DevicesModel danyuan = new DevicesModel();
			danyuan.id = parcel.readString();
			danyuan.dapengId = parcel.readString();
			danyuan.dapengName = parcel.readString();
			danyuan.name = parcel.readString();
			danyuan.jidiName = parcel.readString();
			danyuan.jidiId = parcel.readString();
			return danyuan;
		}

		@Override
		public DevicesModel[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new DevicesModel[size];
		}
		
	};
}
