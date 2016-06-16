package com.hxsn.farmage.base;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BaseActivity extends Activity {

	private Toast toast;
	protected static int screenW, screenH;
	protected RequestQueue mQueue;
	
	protected static ArrayList<Activity> allActivity=new ArrayList<Activity>();

	public static BaseActivity baseActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allActivity.add(this);
		baseActivity = this;
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenW = metrics.widthPixels;
		screenH = metrics.heightPixels;
		mQueue = Volley.newRequestQueue(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mQueue.cancelAll(this);
		super.onStop();
		 
	}

	protected void showToast(int resId) {
		showToast(getString(resId));
	}

	protected void showToast(String msg) {
		toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	protected void showLongToast(String msg) {
		toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		toast.show();
	}
	
	protected void showCenterToast(String message) {
		showToast(message);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null, null);
	}

	protected void openActivity(Class<?> pClass, Bundle bundle) {
		openActivity(pClass, bundle, null);
	}

	protected void openActivity(Class<?> pClass, Bundle bundle, Uri uri) {
		Intent intent = new Intent(this, pClass);
		if (bundle != null)
			intent.putExtras(bundle);
		if (uri != null)
			intent.setData(uri);
		startActivity(intent);
		
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_left_out);
	}

	protected void openActivity(String action) {
		openActivity(action, null, null);
	}

	protected void openActivity(String action, Bundle bundle) {
		openActivity(action, bundle, null);
	}

	protected void openActivity(String action, Bundle bundle, Uri uri) {
		Intent intent = new Intent(action);
		if (bundle != null)
			intent.putExtras(bundle);
		if (uri != null)
			intent.setData(uri);
		startActivity(intent);
	}

	/**
	 * 验证email格式
	 * 
	 * @param email
	 * @return
	 */
	protected boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 验证手机
	 * 
	 * @param mobiles
	 * @return
	 */
	protected boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public void clearAllActivity(){
		if(allActivity!=null){
			for (int i = 0; i < allActivity.size(); i++) {
				allActivity.get(i).finish();
			}
		}
	}
}
