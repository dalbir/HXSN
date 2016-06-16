package com.hxsn.farmage.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxsn.farmage.R;
import com.hxsn.farmage.beans.PopListInfo;

@SuppressLint("InflateParams")
public class PopListAdapter extends BaseAdapter {

	private ArrayList<PopListInfo> list = null;
	private LayoutInflater inflater;
	
	public PopListAdapter(ArrayList<PopListInfo> list, Context context) {
		// TODO Auto-generated constructor stub
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	public ArrayList<PopListInfo> getList() {
		return list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HoldView hv;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.pop_list_item, null);
			hv = new HoldView(convertView);
			convertView.setTag(hv);
		} else {
			hv = (HoldView) convertView.getTag();
		}
		if(list.get(position).getVideos().getEqtype().equals("1"))
			{
			hv.sxtlistIV.setImageResource(R.drawable.sx);
			}
		else
		{
			hv.sxtlistIV.setImageResource(R.drawable.sxq);
		}
		if (list.get(position).isCheck()) {
			hv.pop_list_text.setTextColor(Color.parseColor("#f5be23"));
		} else {
			hv.pop_list_text.setTextColor(Color.BLACK);
		}
		hv.pop_list_text.setText(list.get(position).getVideos().getName());
		
		return convertView;
	}

	private class HoldView {
		
		public TextView pop_list_text;
		public ImageView sxtlistIV;
		
		public HoldView(View view) {
			// TODO Auto-generated constructor stub
			pop_list_text = (TextView) view.findViewById(R.id.pop_list_text);
			sxtlistIV= (ImageView) view.findViewById(R.id.sxtlistIV);
		}
	}
}
