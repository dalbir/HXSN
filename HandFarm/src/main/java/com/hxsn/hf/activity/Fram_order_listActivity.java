package com.hxsn.hf.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hxsn.hf.R;
import com.hxsn.hf.base.BaseActivity;

public class Fram_order_listActivity extends BaseActivity{
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fram_order_list);
		init();
		initOnClick();
		
	}
	
	private void init(){
		back =(ImageView) findViewById(R.id.back);
	}
	
	private void initOnClick(){
		
		//-------------------����¼��߼�-----------------------------
		
		 OnClickListener onClick=new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.back:
					finish();
					break;

				default:
					break;
				}
			}
		};
		//----------------------�ؼ���Ӽ���---------------------
		back.setOnClickListener(onClick);
		
	}
	
	
}