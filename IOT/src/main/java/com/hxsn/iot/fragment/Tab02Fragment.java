package com.hxsn.iot.fragment;


import com.hxsn.iot.R;

public class Tab02Fragment extends AbsBottomFgt {
	
	@Override
	protected int[] getRadioButtonIds(){
		return new int[]{ R.id.tab02_btn01, R.id.tab02_btn02, R.id.tab02_btn03, R.id.tab02_btn04 };
	}

	@Override
	protected int setCreateView(){
		return R.layout.layout_tabs02;
	}

	@Override
	protected boolean isMainBottom() {
		// TODO Auto-generated method stub
		return false;
	}

}
