package com.hxsn.town.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxsn.town.data.IConnPars;

import java.util.HashMap;
import java.util.Map;


public class HomePageFragment extends MyFragment{
	
	private String CookieStr;
	
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
		webView.loadUrl(IConnPars.HOME_PAGE_URL);
		
		
//		synCookies(getActivity(),URL_INDEX);
		return webView;
	}
	
	
	public void synCookies(Context context, String url) {  
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//移除  
	    cookieManager.setCookie(url, CookieStr);//cookies是在HttpClient中获得的cookie  
	    CookieSyncManager.getInstance().sync();  
	}
	
	
	//解决在当前webview中显示网页，而不是用系统自带浏览器显示
	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Map<String,String> extraHeaders = new HashMap<String, String>();
		    extraHeaders.put("Referer", url);
			view.loadUrl(url,extraHeaders);
			return true;
		}
		  
        public void onPageFinished(WebView view, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);
            Log.e("", "______________Cookies = " + CookieStr);
            super.onPageFinished(view, url);
        }

    }
	
	public static boolean onKeyDown(int keyCode, KeyEvent event){
		if(webView.canGoBack()){
			webView.goBack();
			return false;
		} 
		return true;
	}
	
	public static void goToHome(){
		webView.loadUrl(IConnPars.HOME_PAGE_URL);
		//清除历史记录（前进后退栈）需要在主页完全载入完成后才能生效，要不点击tab之前的页面也会保留
		webView.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
            	webView.clearHistory();
            }
        }, 1000);
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
