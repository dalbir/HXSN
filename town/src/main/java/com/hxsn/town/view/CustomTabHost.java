package com.hxsn.town.view;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

public class CustomTabHost extends FragmentTabHost{

	public CustomTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	/*
	 * 重写onTouchModeChanged方法主要是为了解决与网页输入框共存时不能输入英文和数字的bug
	 * 在非Touch模式（即 在使用硬件盘），如果没有View获取焦点，则将焦点传给当前Tab页。
	 */
	@Override
	public void onTouchModeChanged(boolean isInTouchMode) {
		// TODO Auto-generated method stub
		//super.onTouchModeChanged(isInTouchMode);
	}

}
