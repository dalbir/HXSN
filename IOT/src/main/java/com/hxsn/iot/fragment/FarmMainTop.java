package com.hxsn.iot.fragment;

import android.widget.TextView;


public class FarmMainTop extends AbsTopFgt {

	@Override
	protected void getTextTitle(TextView textView){
		textView.setText("农事管理");
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
