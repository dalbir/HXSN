package com.hxsn.iot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.hxsn.iot.R;

public class AboutUsActivity extends Activity {

	private WebView webContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		webContent  = (WebView) findViewById(R.id.webContent);
		webContent.loadUrl("file:///android_asset/www/about.html");
	}

	
}
