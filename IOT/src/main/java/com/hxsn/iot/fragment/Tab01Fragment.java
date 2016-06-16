package com.hxsn.iot.fragment;


import com.hxsn.iot.R;

public class Tab01Fragment extends AbsBottomFgt {

	@Override
	protected int[] getRadioButtonIds(){
		return new int[]{ R.id.tab01_btn01, R.id.tab01_btn02, R.id.tab01_btn03, R.id.tab01_btn04 };
	}

	@Override
	protected int setCreateView(){
		return R.layout.layout_tabs01;
	}

	@Override
	protected boolean isMainBottom() {
		// TODO Auto-generated method stub
		return true;
	}

}
