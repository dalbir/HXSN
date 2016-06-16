package com.hxsn.iot.fragment;

import android.widget.TextView;


public class ExpertMainTop extends AbsTopFgt {

	@Override
	protected void getTextTitle(TextView textView){
		textView.setText("专家");
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
