package com.hxsn.farmage.base;


import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseFragmentActivity extends FragmentActivity {
	
	protected String TAG = "BaseActivity";
	
	protected void setTAG(String TAG) {
		this.TAG = TAG;
	}
	
	protected void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
