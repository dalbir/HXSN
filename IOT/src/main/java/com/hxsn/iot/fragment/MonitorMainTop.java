package com.hxsn.iot.fragment;

import android.widget.TextView;


public class MonitorMainTop extends AbsTopFgt {

	@Override
	protected void getTextTitle(TextView textView){
		textView.setText("监测");
	}

	@Override
	protected boolean isATop(){
		return false;
	}

	@Override
	protected void onMenuClick(){
		
	}

	@Override
	protected boolean hideTop() {
		return false;
	}

}
