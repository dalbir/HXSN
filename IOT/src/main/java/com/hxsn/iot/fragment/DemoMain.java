package com.hxsn.iot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.hxsn.iot.R;
import com.hxsn.iot.activity.AiotBActivity;


public class DemoMain extends AbsBaseFgt {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.layout_democnt, container, false);
		view.findViewById(R.id.btn_gob).setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				startInAbsFgtActivity(AiotBActivity.class,new Bundle());
			}
		});
		return view;
	}

}
