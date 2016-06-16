package com.hxsn.farmage.connect;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.activity.LoginActivity;
import com.hxsn.farmage.activity.HomeActivity;
import com.hxsn.farmage.utils.ImgBtnEffact;





/**
 *
 */
public class NetworkStateReceiver extends BroadcastReceiver {

	private Boolean isRun = false;
	private String who = "";
	Dialog myDialog = null;
	

	public NetworkStateReceiver(String who, Context context) {
	
		this.who = who;
	}

	@Override
	public void onReceive(final Context ct, final Intent inte) {
		if (inte.getBooleanExtra("isHave", true)) {
			if (isRun) {
				if (myDialog != null) {
					myDialog.dismiss();
					isRun = false;
				}
			}
		} else {
			if (isRun) {
				return;
			}
			isRun = true;
			if (who.equals("Main")) {
				BaseActivity.baseActivity.clearAllActivity();
			}
			showWifiDown(ct);
		}

		ct.unregisterReceiver(this);
	}
	
	private void showWifiDown(final Context ct) {
		myDialog = new Dialog(ct);
//		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.show();
		myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		myDialog.setCanceledOnTouchOutside(false);
		Window win = myDialog.getWindow(); 
		win.setContentView(R.layout.dialog_wifi);
		
		Button setting = (Button) win.findViewById(R.id.wifi_setBtn);
		Button close = (Button) win.findViewById(R.id.wifi_cancleBtn);
		
		setting.setOnTouchListener(ImgBtnEffact.btnTL);
		close.setOnTouchListener(ImgBtnEffact.btnTL);
		
		setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRun = false;
				Intent intent = null;
				if (android.os.Build.VERSION.SDK_INT > 0) {
					intent = new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				ct.startActivity(intent);
				myDialog.dismiss();
			}
		});
		
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (who.equals("Main")) {
					MainActivity Main = (MainActivity) ct;
					Main.finish();
				} else if (who.equals("Login")) {
					LoginActivity login = (LoginActivity) ct;
					login.finish();
				}
				else if(who.equals("HomeActivity"))
				{
					HomeActivity homeActivity =(HomeActivity)ct;
					homeActivity.finish();
				}
				myDialog.dismiss();
			}
		});
		
		myDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				myDialog = null;
				isRun = false;
			}
		});
	}
}
