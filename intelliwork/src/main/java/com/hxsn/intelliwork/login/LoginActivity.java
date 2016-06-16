package com.hxsn.intelliwork.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hxsn.intelliwork.MainActivity;
import com.hxsn.intelliwork.MyApplication;
import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseActivity;
import com.hxsn.intelliwork.beans.Contactor;
import com.hxsn.intelliwork.connect.NetJudgeService;
import com.hxsn.intelliwork.connect.NetworkStateReceiver;
import com.hxsn.intelliwork.myviews.ClearEditText;
import com.hxsn.intelliwork.myviews.LoadingDialog;
import com.hxsn.intelliwork.utils.GetURL;
import com.hxsn.intelliwork.utils.ImgBtnEffact;
import com.hxsn.intelliwork.utils.LogUtil;
import com.hxsn.intelliwork.utils.NetUtils;
import com.hxsn.intelliwork.utils.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ShowToast")
public class LoginActivity extends BaseActivity {

	Button loginBtn;
	EditText loginPhoneET;
	ClearEditText loginPwdET;
	ImageButton loginScannerBtn;
	NetworkStateReceiver myReceiver;
	String uid = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getServices();

		initView();
		startServer();

		if (Shared.getCode().length() > 0)
			requtst(Shared.getCode());

		if (Shared.getUserName().length() > 0
				&& !Shared.getPassword().equals("")) {
			loginPhoneET.setText(Shared.getUserName());
			loginPwdET.setText(Shared.getPassword());
			requtst(Shared.getUserName(), Shared.getPassword());
		}

	}

	public void requtst(String logcode) {
		LoadingDialog.showLoading(this);
		try {
			Shared.setCode(logcode);
			StringRequest stringRequest = new StringRequest(GetURL.LOGIN
					+ "logcode=" + logcode,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							JSONObject j;
							try {
								j = new JSONObject(response);
								if (j.getString("code").equals("200")) {
									uid = j.getString("result");
									Shared.setUserID(uid);
									Intent inte = new Intent(
											LoginActivity.this, MainActivity.class);
									startActivity(inte);
								} else {
									showToast("登录失败，返回码：" + j.getString("code"));
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

	private void getServices() {
		// TODO Auto-generated method stub
		// GETSERVERS
		String uid = "";
		if (Shared.getUserID().length() > 0)
			{uid = Shared.getUserID();
			Log.i("suid",Shared.getUserID());
			}
			
		try {
			StringRequest stringRequest = new StringRequest(GetURL.GETSERVERS+uid,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							JSONObject j;
							try {
								// "id":"40288ca151be33c00151be3b68f1000b","onduty":true,"phone":"13333262005","name":"马利强","role":"客服人员","uheadpic":""
								j = new JSONObject(response);
								if (j.getString("code").equals("200")) {
									MyApplication.servers.clear();
									JSONArray jsonArray = j.getJSONArray("result");
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject newObject = (JSONObject) jsonArray.get(i);
										String id = newObject.getString("id");
										boolean onduty = newObject.getBoolean("onduty");
										String phone = newObject.getString("phone");
										String name = newObject.getString("name");
										String role = newObject.getString("role");
										String uheadpic = newObject.getString("uheadpic");
										Contactor contactor = new Contactor(id, uheadpic, name, role, onduty, phone);
										MyApplication.servers.add(contactor);
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

	protected void onDestroy() {
		stopService(new Intent(this, NetJudgeService.class));
		super.onDestroy();
	}

	/**
	 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginPhoneET = (EditText) findViewById(R.id.loginPhoneET);
		loginPwdET = (ClearEditText) findViewById(R.id.loginPwdET);
		loginScannerBtn = (ImageButton) findViewById(R.id.loginScannerBtn);
		loginScannerBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		loginScannerBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent inte = new Intent(LoginActivity.this,
						CaptureActivity.class);
				startActivity(inte);
				finish();
			}
		});
		loginBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userName = loginPhoneET.getText().toString();
				String userPwd = loginPwdET.getText().toString();
				if (userName.equals("") || userPwd.equals("")) {
					showToast("请将账号信息填写完善");
					return;
				} else {
					requtst(userName, userPwd);
					// Intent inte = new Intent(LoginActivity.this,
					// MainActivity.class);
					// startActivity(inte);

				}
			}
		});
		if (!NetUtils.isConnected(getApplication()))
			showToast("请检查网络连接！！");
	}

	public void requtst(final String userName, final String userPwd) {
		if (NetUtils.isConnected(getApplication())) {
			LoadingDialog.showLoading(this);
			try {
				StringRequest stringRequest = new StringRequest(GetURL.LOGIN
						+ "logname=" + userName + "&logpwd=" + userPwd, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						LogUtil.showLog("login", response);
						JSONObject j;
						try {
							j = new JSONObject(response);
							if (j.getString("code").equals("200")) {
								uid = j.getString("result");
								Shared.setUserID(uid);
								Shared.setUserName(userName);
								Shared.setPassword(userPwd);
								Intent inte = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(inte);
								finish();
							} else {
								showToast("登录失败，返回码：" + j.getString("code"));
							}
							LoadingDialog.dissmissLoading();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						NetworkResponse response = error.networkResponse;
						if (response != null)
							switch (response.statusCode) {
							case 408:// 超时

								break;

							case 404://
								break;
							}
					}
				});
				mQueue.add(stringRequest);
			} catch (Exception w) {

			}
		} else {
			showToast("请检查网络连接！！");
		}
	}
	


	private void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean run = true;
				while (run) {
					if (!NetJudgeService.isRun) {
						startService(new Intent(LoginActivity.this,
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
		myReceiver = new NetworkStateReceiver("Login", this);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_EDIT);
		this.registerReceiver(myReceiver, filter);
	}

	private long temptime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (event.getAction() == KeyEvent.ACTION_DOWN)) { // 2s内再次选择back键有效
			if (System.currentTimeMillis() - temptime > 2000) {
				System.out.println(Toast.LENGTH_LONG);
				Toast.makeText(this, "请在按一次退出", Toast.LENGTH_LONG).show();
				temptime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0); // 凡是非零都表示异常退出!0表示正常退出!
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
}
