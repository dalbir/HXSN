package com.hxsn.iot.fragment;

import android.widget.TextView;


public class AlarmMainTop extends AbsTopFgt {

	@Override
	protected void getTextTitle(TextView textView){
		textView.setText("报警信息");
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
		// TODO Auto-generated method stub
		return false;
	}

}
