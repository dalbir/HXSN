package com.hxsn.hf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxsn.hf.R;
import com.hxsn.hf.base.BaseActivity;

public class FramClassListActivity   extends BaseActivity{
	private RelativeLayout framClass_btn;
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_framclass_list);
		init();
		initOnClick();
		
	}
	
	private void init(){
		framClass_btn=(RelativeLayout) findViewById(R.id.framClass_btn);
		back =(ImageView) findViewById(R.id.back);
	}
	
	private void initOnClick(){
		
		//-------------------����¼��߼�-----------------------------
		
		 OnClickListener onClick=new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.framClass_btn :
					 Intent intent =new Intent(FramClassListActivity.this,FramListActivity.class);
					startActivity(intent);
					break;
				case R.id.back:
					finish();
					break;
					
				default:
					break;
				}
			}
		};
		//----------------------�ؼ���Ӽ���---------------------
		framClass_btn.setOnClickListener(onClick);
		back.setOnClickListener(onClick);
	}
	
	
}