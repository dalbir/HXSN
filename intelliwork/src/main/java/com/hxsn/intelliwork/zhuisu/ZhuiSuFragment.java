package com.hxsn.intelliwork.zhuisu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseFrgament;

public class ZhuiSuFragment extends BaseFrgament {

	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_zhuisu_fragment,
				container, false);
		inintView(view);
		return view;
	}

	private void inintView(View view) {
		// TODO Auto-generated method stub
		
	}
}
