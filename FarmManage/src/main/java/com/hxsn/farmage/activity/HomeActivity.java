package com.hxsn.farmage.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.beans.Contactor;
import com.hxsn.farmage.connect.NetJudgeService;
import com.hxsn.farmage.connect.NetworkStateReceiver;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.SDKCoreHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseActivity {

	private ImageButton jiankongIV, jiaoliuIV, manageIV, wodeIV;
	private Intent inte;
	NetworkStateReceiver myReceiver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigator);

		init();
		initView();
		initListener();
		startServer();
		if (MyApplication.app.servers.size() == 0)
			getServices();

	}

	private void getServices() {
		try {
			StringRequest stringRequest = new StringRequest(GetURL.GETSERVERS,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							JSONObject j;
							try {
								// "id":"40288ca151be33c00151be3b68f1000b","onduty":true,"phone":"13333262005","name":"马利强","role":"客服人员","uheadpic":""
								j = new JSONObject(response);
								if (j.getString("code").equals("200")) {
									MyApplication.app.servers.clear();
									JSONArray jsonArray = j
											.getJSONArray("result");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject newObject = (JSONObject) jsonArray
												.get(i);
										String id = newObject.getString("id");
										boolean onduty = newObject
												.getBoolean("onduty");
										String phone = newObject
												.getString("phone");
										String name = newObject
												.getString("name");
										String role = newObject
												.getString("role");
										String uheadpic = newObject
												.getString("uheadpic");
										Contactor contactor = new Contactor(id,
												uheadpic, name, role, onduty,
												phone);
										MyApplication.app.servers
												.add(contactor);
									}

								} else {
									// showToast("请求失败，返回码："+j.getString("code"));
								}
								LoadingDialog.dissmissLoading();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							// Log.e("eeeeeeee", error.getMessage(), error);
						}
					});

			mQueue.add(stringRequest);

		} catch (Exception w) {

		}
	}

	private void init() {
		// TODO Auto-generated method stub
		try {
			SDKCoreHelper.init(this);
		} catch (Exception e) {
		}
		inte = new Intent(HomeActivity.this, MainActivity.class);
	}

	private void initView() {
		// TODO Auto-generated method stub
		jiankongIV = (ImageButton) findViewById(R.id.jiankongIV);
		jiaoliuIV = (ImageButton) findViewById(R.id.jiaoliuIV);
		manageIV = (ImageButton) findViewById(R.id.manageIV);
		wodeIV = (ImageButton) findViewById(R.id.wodeIV);

	}

	private void initListener() {
		// TODO Auto-generated method stub
		jiankongIV.setOnTouchListener(ImgBtnEffact.btnTL);
		jiaoliuIV.setOnTouchListener(ImgBtnEffact.btnTL);
		manageIV.setOnTouchListener(ImgBtnEffact.btnTL);
		wodeIV.setOnTouchListener(ImgBtnEffact.btnTL);

		jiankongIV.setOnClickListener(new NewIVListener());
		jiaoliuIV.setOnClickListener(new NewIVListener());
		manageIV.setOnClickListener(new NewIVListener());
		wodeIV.setOnClickListener(new NewIVListener());
	}

	private void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean run = true;
				while (run) {
					if (!NetJudgeService.isRun) {
						startService(new Intent(HomeActivity.this,
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

	class NewIVListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub

			switch (view.getId()) {
			case R.id.jiankongIV:
				inte.putExtra("tag", 0);
				startActivity(inte);
				break;
			case R.id.jiaoliuIV:
				inte.putExtra("tag", 1);
				startActivity(inte);
				break;
			case R.id.manageIV:
				inte.putExtra("tag", 2);
				startActivity(inte);
				break;
			case R.id.wodeIV:
				inte.putExtra("tag", 3);
				startActivity(inte);
				break;
			default:
				break;
			}
		}

	}

	private void bindBroadcast() {
		myReceiver = new NetworkStateReceiver("HomeActivity", this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_EDIT);
		this.registerReceiver(myReceiver, filter);
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			SDKCoreHelper.logout();
		} catch (Exception e) {
		}
		stopService(new Intent(this, NetJudgeService.class));
		super.onDestroy();
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
