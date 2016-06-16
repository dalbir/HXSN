package com.hxsn.town.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxsn.town.data.IConnPars;

import java.util.HashMap;
import java.util.Map;

public class CircumDetailActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView mWebView = new WebView(this);
		mWebView.setWebChromeClient(new MyWebChromeClient()); 
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.loadUrl(IConnPars.URL+getIntent().getExtras().get("url").toString());
		setContentView(mWebView);
	} 
	
	//为了实现点击链接继续在当前browser中响应
	private class MyWebViewClient extends WebViewClient{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Map<String,String> extraHeaders = new HashMap<String, String>();
		    extraHeaders.put("Referer", url);
			view.loadUrl(url);
			return true;
		}
	}
	
	private class MyWebChromeClient extends WebChromeClient {    

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			
			return super.onJsAlert(view, url, message, result);
		}
		
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {

			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {

			return super.onJsPrompt(view, url, message, defaultValue, result);
		}
        
    }
}
