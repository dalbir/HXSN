package com.hxsn.farmage.base;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	protected Context context;
	protected LayoutInflater inflater;
	protected ArrayList<T> myList=new ArrayList<T>();
	
	public MyBaseAdapter(Context context){
		this.context=context;
		this.inflater=LayoutInflater.from(context);
	}
	
	public ArrayList<T>  getAdapterData(){
		return myList;
	}
	
	//锟斤拷装锟斤拷锟斤拷莸姆锟斤拷锟�--1锟斤拷  锟斤拷锟斤拷     
	public void appendData(T t,boolean isClearOld){
		if(t==null)
			return;
		if(isClearOld)
			myList.clear();
		myList.add(t);
	}
	public void appendData(ArrayList<T> data,boolean isClearOld){
		if(data==null)
			return;
		if(isClearOld)
			myList.clear();
		myList.addAll(data);
	}
	//锟斤拷装锟斤拷锟斤拷莸姆锟斤拷锟�--1锟斤拷  锟斤拷锟斤拷    锟节讹拷锟斤拷锟斤拷锟�
	public void appendDataTop(T t,boolean isClearOld){
		if(t==null)
			return;
		if(isClearOld)
			myList.clear();
		myList.add(0, t);
	}
	public void appendDataTop(ArrayList<T> data,boolean isClearOld){
		if(data==null)
			return;
		if(isClearOld)
			myList.clear();
		myList.addAll(0, data);
	}
	
	public void update(){
		this.notifyDataSetChanged();
	}
	
	public void clear(){
		myList.clear();
	}
	

	public int getCount() {
		if(myList==null)
			return 0;
		return myList.size();
	}

	public T getItem(int position) {
		if(myList==null)
			return null;
		if(position>myList.size()-1)
			return null;
		return myList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return getMyView(position, convertView, parent);
	}
	public abstract View getMyView(int position, View convertView, ViewGroup parent);
	
}
