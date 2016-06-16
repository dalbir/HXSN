package com.hxsn.iot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.db.DbHelper;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.User;
import com.hxsn.iot.service.AlarmService;
import com.hxsn.iot.util.DbUtil;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.util.NetConnUtil;
import com.hxsn.iot.util.NetworkUtil;


public class LoginActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	//private ProgressBar mProgressBar;
	private EditText edtUser;
	private EditText edtPwd;
	private ImageView ImgUserER;		// 用户对错图标
	private ImageView ImgPswER;			//  密码对错图标
	private TextView txtPrompt;			//  提示文本 提示密码为空
	private TextView  txtRightIcon;		//用户名右边的三角
	private String name;
	private String passward;
	private String sessionId;
	private String serverUrl;

	//private PopupWindow popupWindow;
	private SharedPreferences sharedata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		Contents.getInstance().setActivity(this);
		checkNetwork();
		sharedata = getSharedPreferences("data", 0);
		// 获取存在本地的username，pwd
		name = sharedata.getString("userName", null);
		passward = sharedata.getString("pwd", null);
		sessionId = sharedata.getString("sessionid", null);
		serverUrl = sharedata.getString("ServerUri", null);
		setServerUrl(sharedata);

		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passward) || TextUtils.isEmpty(sessionId)) {
			setContentView(R.layout.main);
			addView();
			Button b = (Button) findViewById(R.id.button1);
			b.setOnClickListener(this);
			//initPopupWindow(sharedata);
		} else {
			Log.i("百度云推送", "百度云推送");
			cloundStartWork();
			// 本地的username，pwd进行相关操作
			toNextActivity();
			this.finish();
		}
	}

	private void addView() {
		ImgUserER = (ImageView)findViewById(R.id.img_error_or_right_user);
		ImgPswER = (ImageView)findViewById(R.id.img_error_or_right_psw);
		txtPrompt = (TextView)findViewById(R.id.txt_prompt);
		txtRightIcon = (TextView)findViewById(R.id.txt_right_icon);
		edtUser = (EditText) findViewById(R.id.etUserName);
		edtPwd = (EditText) findViewById(R.id.etPWD);
	}

	private void initPopupWindow(final SharedPreferences sp){
		LayoutInflater inflater = LayoutInflater.from(this);
		View popView = inflater.inflate(R.layout.login_setting_popup_layout, null);
		PopupWindow popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		popupWindow.setOutsideTouchable(true);
		TextView serverUrlTv = (TextView) popView.findViewById(R.id.login_popup_server_url);
		serverUrlTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditText et = new EditText(LoginActivity.this);
				serverUrl = sp.getString("ServerUri", null);
				et.setText(serverUrl);
				new AlertDialog.Builder(LoginActivity.this).setTitle("请输入服务器地址")
						.setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String serverUrl = et.getText().toString().trim();
						if(serverUrl.isEmpty()||serverUrl.equals("")){
							Toast.makeText(LoginActivity.this, "地址为空，请重新输入", Toast.LENGTH_LONG).show();
						} else {
							sp.edit().putString("ServerUri", serverUrl).commit();
						}
					}
				}).show();
			}
		});
	}

	//判断aiot文件中是否有地址，没有需要用户输入地址保存xml中。读地址时首先读文件，返回空时读xml
	private void setServerUrl(final SharedPreferences sp){
//		String localUrl = NetworkUtil.getLocalUri();
		String spUrl = sp.getString("ServerUri", null);
		if(TextUtils.isEmpty(spUrl)){
			final EditText et = new EditText(this);
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setTitle("请输入服务器地址")
					.setView(et)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialoginterface, int i){
							sp.edit().putString("ServerUri", et.getText().toString().trim()).commit();
							Log.i("", "++++++++++++" + sp.getString("ServerUri", null));
						}
					}).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.button1:
				User user = DataController.getLoginData(edtUser.getText().toString(),edtPwd.getText().toString(),getPhoneID().toString().trim());
				//网络错误码判断
				if(user != null){
					if(NetworkUtil.isErrorCode(user.getStatus(), this)){
						return;
					}
				} else {
					MessDialog.show(getResources().getString(R.string.server_unusual_msg), this);
					return;
				}

				String sessionid = user.getSessionid();
				String controlPwd = user.getControlPwd();
				int code = Integer.parseInt(user.getStatus());
				Log.i("sessionid", "sessionid=" + sessionid);
				Log.i("etName", "etName=" + edtUser.getText().toString().trim());
				// 用户名的验证
				txtRightIcon.setVisibility(View.GONE);
				ImgUserER.setVisibility(View.VISIBLE);
				if (TextUtils.isEmpty(edtUser.getText().toString().trim())) {
					ImgUserER.getDrawable().setLevel(9);
					txtPrompt.setText("用户名不能为空");
					return;
				} else {
					ImgUserER.getDrawable().setLevel(39);

				}
				// 密码验证
				if (TextUtils.isEmpty(edtPwd.getText().toString().trim())) {
					ImgPswER.getDrawable().setLevel(9);
					ImgPswER.setVisibility(View.VISIBLE);
					txtPrompt.setText("密码不能为空");
					return;
				} else {

					if (200 == code) {
						AbsApplication.getInstance().setSessionid(sessionid);// sessionId存入全局变量
						txtPrompt.setVisibility(View.GONE);
						ImgPswER.getDrawable().setLevel(39);

						if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passward)) {
							Editor esharedata = getSharedPreferences("data", 0).edit();
							esharedata.putString("userName", edtUser.getText().toString());
							esharedata.putString("pwd", edtPwd.getText().toString());
							esharedata.putString("IMEI", getPhoneID().toString().trim());
							esharedata.putString("sessionid", sessionid);
							esharedata.putString("controlPwd", controlPwd);
							esharedata.commit();
						}
						/**
						 * 查看当前的用户名在本地数据库中存不存在 1）存在启动cloundStartWork
						 * 2）不存在，新建一个服务发送一个携带本地当前时间的请求，返回报警信息，关闭当前服务。启动cloundStartWork
						 * */
						SQLiteDatabase db = DbUtil.getReadDb(this);
						Cursor cursor = db.rawQuery("select username from user_table where username=?", new String[]{edtUser.getText()
								.toString()});
						try {
							if (cursor.getCount() == 0) {
								Log.i("用户第一次登陆","用户第一次登陆启动服务");
								String sql = "insert into user_table(id,username) values('" +DbHelper.getUuid()+"','"+ edtUser.getText().toString() + "')";
								db.execSQL(sql);
								Intent intent = new Intent(this, AlarmService.class);
								startService(intent);
								stopService(intent);
								Log.i("用户第一次登陆","用户第一次登陆停止服务");
							}
							Log.i("百度云推送","百度云推送");
							cloundStartWork();

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							cursor.close();
							DbUtil.closeDb(db);
						}

						toNextActivity();
						this.finish();
					} else if (NetConnUtil.connTimeOut(this, code)) {
						Toast.makeText(this, "连接超时", Toast.LENGTH_LONG).show();
					} else {
						txtPrompt.setText("用户名或密码错误");
						ImgPswER.getDrawable().setLevel(9);
						return;
					}
				}
				break;
		}

	}

	public void setBtnClick(View view){
//		if(pw.isShowing()){
//			pw.dismiss();
//		} else {
//			pw.showAsDropDown(view);
//		}
		final EditText et = new EditText(LoginActivity.this);
		serverUrl = sharedata.getString("ServerUri", null);
		et.setText(serverUrl);
		new AlertDialog.Builder(LoginActivity.this).setTitle("请输入服务器地址")
				.setView(et).setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String serverUrl = et.getText().toString().trim();
				if(serverUrl.isEmpty()||serverUrl.equals("")){
					Toast.makeText(LoginActivity.this, "地址为空，请重新输入", Toast.LENGTH_LONG).show();
				} else {
					sharedata.edit().putString("ServerUri", serverUrl).commit();
				}
			}
		}).show();
	}

	public void toNextActivity() {

		Intent intent = new Intent(LoginActivity.this, AiotActivity.class);
		startActivity(intent);
	}

	public String getPhoneID() {
		// 获取手机唯一标识，存到数据库
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String szImei = TelephonyMgr.getDeviceId();
		AbsApplication.getInstance().setImei(szImei);
		return szImei;
	}

	/**
	 * 检测网络是否可用
	 *
	 * @return
	 */
	public boolean checkNetwork() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		// 网络不可用
		if (null == networkInfo || !networkInfo.isAvailable()) {
			new AlertDialog.Builder(this).setCancelable(false)
					.setMessage(R.string.nonetwork)
					.setPositiveButton(R.string.confirm,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
													int which) {
									Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
									startActivity(intent);
									finish();
								}
							}).show();
			return false;
		}
		return true;
	}

	public void cloundStartWork() {
//		PushManager.startWork(this.getApplicationContext(),
//				PushConstants.LOGIN_TYPE_API_KEY,
//				Utils.getMetaValue(this, "api_key"));

		/**
		 * 1.此api_key是用郑巨鑫的百度账号创建的工程，可以这样写，也可以用上种方式读取写在清单文件中的api_key
		 * 2.在此设置通知的样式，不设置为默认
		 * **/
		PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, "4v6XqFOHgEg1Arv60l8EBMAn");

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}