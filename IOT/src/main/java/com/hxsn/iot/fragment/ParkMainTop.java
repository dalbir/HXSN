package com.hxsn.iot.fragment;

import android.widget.TextView;


public class ParkMainTop extends AbsTopFgt {

	@Override
	protected void getTextTitle(TextView textView){
		textView.setText("园区");
	}

	@Override
	protected boolean isATop(){
		return true;
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
