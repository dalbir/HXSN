package com.hxsn.farmage.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.hxsn.farmage.R;
import com.hxsn.farmage.adapter.FirstAdapter;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.utils.Shared;

import java.util.ArrayList;



@SuppressLint("InflateParams")
public class FristWelcomeActivity extends BaseActivity implements OnPageChangeListener{
	

	private ImageView page_3_img;
	private ViewPager viewPager;
	private FirstAdapter viewPagerAdapter;
	
	private ArrayList<View> viewList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_welcome);
		
		Intent inte=new Intent();
		
		if (Shared.getIsFirstRun()) {
			init();
			setchangePager(0);
			initListener();
		} else {
			if(Shared.getUserID().length()>0)
			{
				inte.setClass(FristWelcomeActivity.this, HomeActivity.class);
				startActivity(inte);
			}
			else
			{
				inte.setClass(FristWelcomeActivity.this, LoginActivity.class);
				startActivity(inte);
				overridePendingTransition(R.anim.infrom_right, R.anim.outto_left);
			}
			finish();
		}
		
		
	}
	
	private void init(){
		viewList = new ArrayList<View>();
		viewList.add(getLayoutInflater().inflate(R.layout.page_1, null));
		viewList.add(getLayoutInflater().inflate(R.layout.page_2, null));
		viewList.add(getLayoutInflater().inflate(R.layout.page_3, null));
		
		viewPager = (ViewPager) findViewById(R.id.frist_welcome_vp);
		page_3_img = (ImageView) viewList.get(2).findViewById(R.id.page_3_img);
		
		viewPagerAdapter = new FirstAdapter(viewList);
		viewPager.setAdapter(viewPagerAdapter);
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		viewPager.setOnPageChangeListener(this);
		
		page_3_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Shared.setIsFirstRun(false);
				Intent intent = new Intent(FristWelcomeActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setchangePager(arg0);
	}
	
	private void setchangePager(int id){

	}

}
