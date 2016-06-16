package com.hxsn.iot.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import com.hxsn.iot.AbsApplication;

/** 
 * 
 * 自定义的显示通知的Activity
 *
 */
public class CustomActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		
		setContentView(resource.getIdentifier("custom_activity", "layout", pkgName));

		TextView titleView = (TextView) this.findViewById(resource.getIdentifier("title", "id", pkgName));
		TextView contentView = (TextView) this.findViewById(resource.getIdentifier("content", "id", pkgName));
		
		Intent intent = getIntent();
        String title = AbsApplication.notifyInfo.getTitle();//intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
        String content = AbsApplication.notifyInfo.getDescription();//intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
        
        titleView.setText(title);
        contentView.setText(content);
	}
}
