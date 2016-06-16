package com.hxsn.town.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxsn.town.MainActivity;
import com.hxsn.town.activity.PayActivity;
import com.hxsn.town.data.IConnPars;

import java.util.HashMap;
import java.util.Map;

public class ShopCartFragment extends MyFragment{
	
	private static final String LOGCAT = "com.snsoft.phone.fragment.ShopCartFragment";
	
	public String CookieStr;
	
	@SuppressLint("JavascriptInterface")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		webView = new WebView(getActivity());
		webView.setWebChromeClient(new MyWebChromeClient()); 
		webView.setWebViewClient(new MyWebViewClient());
		
		WebSettings setting = webView.getSettings();     
		setting.setJavaScriptCanOpenWindowsAutomatically(true);   
		setting.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JavaScript(),"droid");
		setting.setRenderPriority(RenderPriority.HIGH);//提高渲染的优先级
		webView.loadUrl(IConnPars.SHOP_CART_URL);
		
		return webView;
	}
	
	class JavaScript{
    	public void clickBack() { 
        	if(webView.canGoBack()){
    			webView.goBack();
    		} 
        } 
    	
    	public void callAlipay(String title,String content, String orderNum, String price){
    		Bundle bundle = new Bundle();
    		bundle.putString("title", title);
    		bundle.putString("content", content);
    		bundle.putString("orderNum", orderNum);
    		bundle.putString("price", price);
    		Intent intent = new Intent();
    		intent.putExtra("bundle", bundle);
    		intent.setClass(getActivity(), PayActivity.class);
    		//startActivity(intent);
    		startActivityForResult(intent, REQ_CODE);
    	}
    }
	
	//解决在当前webview中显示网页，而不是用系统自带浏览器显示
	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url.contains("index.shtml")){
				((MainActivity)getActivity()).setCurrentTab(0);//当点击去逛逛按钮后跳转到首页标签
			}
			Map<String,String> extraHeaders = new HashMap<String, String>();
		    extraHeaders.put("Referer", url);
			view.loadUrl(url);
			return true;
		}
		
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }
	
	public static void goToHome(){
		webView.loadUrl(IConnPars.SHOP_CART_URL);
	}
	
	public static boolean onKeyDown(int keyCode, KeyEvent event){
		if(webView.canGoBack()){
			webView.goBack();
			return false;
		} 
		return true;
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
