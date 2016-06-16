package com.hxsn.farmage.activity;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.beans.Contactor;
import com.hxsn.farmage.connect.NetJudgeService;
import com.hxsn.farmage.connect.NetworkStateReceiver;
import com.hxsn.farmage.myview.ClearEditText;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.Shared;

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
	String uid="";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		getServices();

		initView();
		startServer();


		if(Shared.getCode().length()>0)
			requtst(Shared.getCode());

		if(Shared.getUserName().length()>0&&!Shared.getPassword().equals(""))
		{
			requtst(Shared.getUserName(), Shared.getPassword());
		}



	}


	public void requtst(String logcode)
	{LoadingDialog.showLoading(this);
		try{
			Shared.setCode(logcode);
			StringRequest stringRequest = new StringRequest(GetURL.LOGIN+"logcode="+logcode,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							JSONObject j;
							try {
								j = new JSONObject(response);
								if (j.getString("code").equals("200")) {
									uid=j.getString("result");
									Shared.setUserID(uid);
									Intent inte = new Intent(LoginActivity.this, HomeActivity.class);
									startActivity(inte);
								} else{
									showToast("登录失败，返回码："+j.getString("code"));
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
		}catch(Exception w)
		{}

	}
	private void getServices() {
		String uid = "";
		if (Shared.getUserID().length() > 0)
		{uid = Shared.getUserID();
			//	Log.i("suid",Shared.getUserID());
		}
		try{
			StringRequest stringRequest = new StringRequest(GetURL.GETSERVERS+uid,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							JSONObject j;
							try {
								//"id":"40288ca151be33c00151be3b68f1000b","onduty":true,"phone":"13333262005","name":"马利强","role":"客服人员","uheadpic":""
								j = new JSONObject(response);
								if (j.getString("code").equals("200")) {
									MyApplication.app.servers.clear();
									JSONArray jsonArray=j.getJSONArray("result");
									for (int i=0;i<jsonArray.length();i++) {
										JSONObject newObject=(JSONObject)jsonArray.get(i);
										String id=newObject.getString("id");
										Log.i("++++++++++++++++++", id);
										boolean onduty=newObject.getBoolean("onduty");
										String phone=newObject.getString("phone");
										String name=newObject.getString("name");
										String role=newObject.getString("role");
										String uheadpic=newObject.getString("uheadpic");
										Contactor contactor=new Contactor(id, uheadpic, name, role, onduty, phone);
										MyApplication.app.servers.add(contactor);
									}
								}else{
									//showToast("请求失败，返回码："+j.getString("code"));
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
		}catch(Exception w){}
	}
	@Override
	protected void onDestroy() {
		stopService(new Intent(this, NetJudgeService.class));
		super.onDestroy();
	}
	/**

	 */
	private void initView() {
		loginBtn = (Button) findViewById(R.id.loginBtn);
		loginPhoneET = (EditText) findViewById(R.id.loginPhoneET);
		loginPwdET = (ClearEditText) findViewById(R.id.loginPwdET);

		String userName = Shared.getUserName();
		if(userName.length()>0){
			loginPhoneET.setText(userName);
			loginPwdET.setText(Shared.getPassword());
		}

		loginScannerBtn=(ImageButton)findViewById(R.id.loginScannerBtn);
		loginScannerBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		loginScannerBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent inte=new Intent(LoginActivity.this,CaptureActivity.class);
				startActivity(inte);
				finish();
			}
		});
		loginBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userName = loginPhoneET.getText().toString();
				String userPwd = loginPwdET.getText().toString();
				requtst(userName, userPwd);
			}
		});
	}

	public void requtst(final String userName,final String userPwd)
	{LoadingDialog.showLoading(this);
		try{
			String url = GetURL.LOGIN+"logname="+userName+"&logpwd="+userPwd;
			Log.i("LoginActivity","url="+url);
			StringRequest stringRequest = new StringRequest(url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Shared.setUserName(userName);
							Shared.setPassword(userPwd);

							try {
								JSONObject jsonObject = new JSONObject(response);
								String result = jsonObject.getString("result");
								Shared.setUserID(result);
							} catch (JSONException e) {
								e.printStackTrace();
							}

							Intent inte = new Intent(LoginActivity.this, HomeActivity.class);
							startActivity(inte);
							finish();
							LoadingDialog.dissmissLoading();
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// Log.e("eeeeeeee", error.getMessage(), error);
				}
			});

			mQueue.add(stringRequest);
		}catch(Exception w){}

	}

	private void startServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean run = true;
				while (run) {
					if (!NetJudgeService.isRun) {
						startService(new Intent(LoginActivity.this, NetJudgeService.class));
						bindBroadcast();
						run = false;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 屏蔽返回键
		switch(keyCode){
//		case KeyEvent.KEYCODE_HOME:return true;
			case KeyEvent.KEYCODE_BACK:return true;
//		case KeyEvent.KEYCODE_CALL:return true;
//		case KeyEvent.KEYCODE_SYM: return true;
//		case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
//		case KeyEvent.KEYCODE_VOLUME_UP: return true;
//		case KeyEvent.KEYCODE_STAR: return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
