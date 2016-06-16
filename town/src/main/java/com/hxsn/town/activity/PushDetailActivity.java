package com.hxsn.town.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxsn.town.data.Contents;

//推送详细页
public class PushDetailActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView mWebView = new WebView(this);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.loadUrl(Contents.getInstance().getUrl());
		setContentView(mWebView);
	}
	//为了实现点击链接继续在当前browser中响应
	private class MyWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
