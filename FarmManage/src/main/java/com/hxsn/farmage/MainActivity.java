package com.hxsn.farmage;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.farmage.base.BaseFragmentActivity;
import com.hxsn.farmage.fragment.ComcationFragment;
import com.hxsn.farmage.connect.NetJudgeService;
import com.hxsn.farmage.connect.NetworkStateReceiver;
import com.hxsn.farmage.fragment.JianKongFragment;
import com.hxsn.farmage.fragment.ManageFragment;
import com.hxsn.farmage.fragment.MineFragment;

public class MainActivity extends BaseFragmentActivity {

	FrameLayout frameLayout;
	LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	TextView tab1TV, tab2TV, tab3TV, tab4TV;
	android.support.v4.app.FragmentManager fm;
	ImageView tab1IV, tab2IV, tab3IV, tab4IV;

	JianKongFragment jiankongFragment;
	ManageFragment manageFragment;
	ComcationFragment comuFragment;
	MineFragment mineFragment;

	private NetworkStateReceiver myReceiver;

	public static MainActivity mainActivity;

	private boolean isClose = true;
	public boolean islogout = true;
	int tag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService();
		mainActivity = this;
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

		jiankongFragment = new JianKongFragment();
		manageFragment = new ManageFragment();
		comuFragment = new ComcationFragment();
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
				tab1IV.setBackgroundResource(R.drawable.jian01);
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
				tab3IV.setBackgroundResource(R.drawable.guan01);
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
		stopService(new Intent(MainActivity.this, NetJudgeService.class));
		super.onDestroy();
	}

	private void setChoiceItem(int id) {
		fm = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
		if (id == 0) {
			clear();
			tab1TV.setTextColor(Color.rgb(139, 193, 17));
			tab1IV.setBackgroundResource(R.drawable.jian01);
			transaction.replace(R.id.tabContent, jiankongFragment);
		} else if (id == 1) {
			clear();
			tab2TV.setTextColor(Color.rgb(139, 193, 17));
			tab2IV.setBackgroundResource(R.drawable.jiao01);
			transaction.replace(R.id.tabContent, comuFragment);

		} else if (id == 2) {
			clear();
			tab3TV.setTextColor(Color.rgb(139, 193, 17));
			tab3IV.setBackgroundResource(R.drawable.guan01);
			transaction.replace(R.id.tabContent, manageFragment);
		} else if (id == 3) {
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

		tab1IV.setBackgroundResource(R.drawable.jian);
		tab2IV.setBackgroundResource(R.drawable.jiao);
		tab3IV.setBackgroundResource(R.drawable.guan);
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
}
