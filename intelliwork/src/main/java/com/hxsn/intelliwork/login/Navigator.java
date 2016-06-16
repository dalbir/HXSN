package com.hxsn.intelliwork.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseActivity;



public class Navigator extends BaseActivity {


    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigator);
		
		init();
		initView();
		initListener();

	}

	private void init() {
		// TODO Auto-generated method stub
	}

	private void initView() {
		// TODO Auto-generated method stub
	
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
	
	}

	class NewIVListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			
			switch (view.getId()) {
			
			default:
				break;
			}
		}

	}

}
