package com.hxsn.town.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hxsn.town.data.IConnPars;
import com.hxsn.town.activity.ImgFileListActivity;

import java.util.HashMap;
import java.util.Map;


public class MyPageFragment extends MyFragment{
	
	private String CookieStr;
	private static final int FILECHOOSER_RESULTCODE = 200;
	private static final int RESULT_OK = 200;
	private ValueCallback<Uri> mUploadMessage;
	
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
		webView.loadUrl(IConnPars.MY_PAGE_URL);
		
		return webView;
	}
	
	//解决在当前webview中显示网页，而不是用系统自带浏览器显示
	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url.equals(IConnPars.IMAGE_RETURN_CODE)){
				//选择多张图片上传
				Intent intent = new Intent();
				intent.setClass(getActivity(),ImgFileListActivity.class);
				startActivity(intent);
			} else {
				Map<String,String> extraHeaders = new HashMap<String, String>();
			    extraHeaders.put("Referer", url);
				view.loadUrl(url);
			}
			return true;
		}
		
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }
	
	public static void goToHome(){
		webView.loadUrl(IConnPars.MY_PAGE_URL);
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
		//openFileChooser为使webview支持<input type="file" />，此方法没有出现在api中
		// For Android > 3.0
		public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType ) {   
		    mUploadMessage = uploadMsg;   
		    Intent i = new Intent(Intent.ACTION_GET_CONTENT);   
		    i.addCategory(Intent.CATEGORY_OPENABLE);   
		    i.setType("image/*");   
		    getActivity().startActivityForResult( Intent.createChooser( i, "Filedata" ), FILECHOOSER_RESULTCODE );   
		}
		
		// For Android < 3.0  
		public void openFileChooser( ValueCallback<Uri> uploadMsg ) {  
		    mUploadMessage = uploadMsg;  
		    Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
		    i.addCategory(Intent.CATEGORY_OPENABLE);  
		    i.setType("image/*");  
		    getActivity().startActivityForResult( Intent.createChooser( i, "Filedata" ), FILECHOOSER_RESULTCODE );   
		}  
		
		//For Android 4.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
			mUploadMessage = uploadMsg;
		    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		    i.addCategory(Intent.CATEGORY_OPENABLE);
		    i.setType("image/*");
		    getActivity().startActivityForResult(Intent.createChooser(i, "Filedata"), FILECHOOSER_RESULTCODE);
		}
        
    }
	
	//图片上传发送到javascript传入上传图片返回值
	public static void sendUrlToServer(String url){
		Log.i("MyPageFragment","url="+url);
		webView.loadUrl("javascript:sendUploadImgCode('" + url + "')");
	}
}
