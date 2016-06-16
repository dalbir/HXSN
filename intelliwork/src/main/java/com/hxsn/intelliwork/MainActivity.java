package com.hxsn.intelliwork;


import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.intelliwork.base.BaseFragmentActivity;
import com.hxsn.intelliwork.comm.ComFragment;
import com.hxsn.intelliwork.connect.NetJudgeService;
import com.hxsn.intelliwork.connect.NetworkStateReceiver;
import com.hxsn.intelliwork.mine.MineFragment;
import com.hxsn.intelliwork.utils.SDKCoreHelper;
import com.hxsn.intelliwork.work.WorkFragment;
import com.hxsn.intelliwork.zhuisu.SuYuanActivity;
import com.hxsn.intelliwork.zhuisu.ZhuiSuFragment;


public class MainActivity extends BaseFragmentActivity {

	FrameLayout frameLayout;
	LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	TextView tab1TV, tab2TV, tab3TV, tab4TV;
	FragmentManager fm;
	ImageView tab1IV, tab2IV, tab3IV, tab4IV;
	private long temptime = 0;

	WorkFragment workFragment;
	ComFragment comFragment;
	ZhuiSuFragment zhuisuFragment;
	MineFragment mineFragment;

	private NetworkStateReceiver myReceiver;

	public static MainActivity mainActivity;

	private boolean isClose = true;
	public boolean islogout = true;
	int tag = 0;
	private Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService();
		try{
		SDKCoreHelper.init(this);
		}catch(Exception e){}
		mainActivity = this;
		intent=new Intent(this, SuYuanActivity.class);
//		fm = getSupportFragmentManager();

		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tab1TV = (TextView) findViewById(R.id.hometltv);
		tab2TV = (TextView) findViewById(R.id.tab2IVltv);
		tab3TV = (TextView) findViewById(R.id.tab3IVltv);
		tab4TV = (TextView) findViewById(R.id.tab4IVltv);
		tab1IV = (ImageView) findViewById(R.id.tab1IV);
		tab2IV = (ImageView) findViewById(R.id.tab2IV);
		tab3IV = (ImageView) findViewById(R.id.tab3IV);
		tab4IV = (ImageView) findViewById(R.id.tab4IV);
		workFragment = new WorkFragment();
        comFragment = new ComFragment();
        zhuisuFragment = new ZhuiSuFragment();
	    mineFragment = new MineFragment();
		Intent inte = getIntent();
		if (inte != null)
			tag = inte.getIntExtra("tag", 0);

	 	setChoiceItem(tag);

		tab1Layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				tab1TV.setTextColor(Color.rgb(139, 193, 17));
				tab1IV.setBackgroundResource(R.drawable.zuo01);
				setChoiceItem(0);
			}
		});
		tab2Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				tab2TV.setTextColor(Color.rgb(139, 193, 17));
				tab2IV.setBackgroundResource(R.drawable.jiao01);
				setChoiceItem(1);
			}
		});
		tab3Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				tab3TV.setTextColor(Color.rgb(139, 193, 17));
				tab3IV.setBackgroundResource(R.drawable.su01);
				setChoiceItem(2);
			}
		});
		tab4Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				tab4TV.setTextColor(Color.rgb(139, 193, 17));
				tab4IV.setBackgroundResource(R.drawable.wo01);
				setChoiceItem(3);
			}
		});
	

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// dbHelper.close();
		 if (islogout) {
			 try {
				 SDKCoreHelper.logout();
			} catch (Exception e) {
				// TODO: handle exception
			}
			 
		 }
		stopService(new Intent(MainActivity.this, NetJudgeService.class));
		super.onDestroy();
	}
	
	
	private void setChoiceItem(int id) {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (id == 0) {
			clear();
			tab1TV.setTextColor(Color.rgb(139, 193, 17));
			tab1IV.setBackgroundResource(R.drawable.zuo01);
			transaction.replace(R.id.tabContent, workFragment);
		}else if(id==1){
			clear();
			tab2TV.setTextColor(Color.rgb(139, 193, 17));
			tab2IV.setBackgroundResource(R.drawable.jiao01);
			transaction.replace(R.id.tabContent, comFragment);
		}else if(id==2){
			clear();
			tab3TV.setTextColor(Color.rgb(139, 193, 17));
			tab3IV.setBackgroundResource(R.drawable.su01);
		    Intent inte =new Intent(MainActivity.this,SuYuanActivity.class);
			startActivity(inte);
		}else if(id==3){
			clear();
			tab4TV.setTextColor(Color.rgb(139, 193, 17));
			tab4IV.setBackgroundResource(R.drawable.wo01);
			transaction.replace(R.id.tabContent, mineFragment);
		}
		
		    transaction.commit();
	}


	private void clear() {
		// TODO Auto-generated method stub
		tab1TV.setTextColor(Color.rgb(91, 91, 99));
		tab2TV.setTextColor(Color.rgb(91, 91, 99));
		tab3TV.setTextColor(Color.rgb(91, 91, 99));
		tab4TV.setTextColor(Color.rgb(91, 91, 99));
	
		tab1IV.setBackgroundResource(R.drawable.zuo);
		tab2IV.setBackgroundResource(R.drawable.jiao);
		tab3IV.setBackgroundResource(R.drawable.su);
		tab4IV.setBackgroundResource(R.drawable.wo);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tab1:
			setChoiceItem(0);
			break;
		case R.id.tab2:
			setChoiceItem(1);
			break;
		case R.id.tab3:
			setChoiceItem(2);
			break;
		case R.id.tab4:
			setChoiceItem(3);
			break;
		default:
			break;
		}
	}



	private void startService() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean run = true;
				while (run) {
					if (!NetJudgeService.isRun) {
						startService(new Intent(MainActivity.this,
								NetJudgeService.class));
						bindBroadcast();
						run = false;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void bindBroadcast() {
		myReceiver = new NetworkStateReceiver("Main", this);
		IntentFilter filter = new IntentFilter();

		filter.addAction(Intent.ACTION_EDIT);
		registerReceiver(myReceiver, filter);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
