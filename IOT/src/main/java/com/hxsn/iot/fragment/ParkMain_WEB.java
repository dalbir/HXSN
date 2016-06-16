package com.hxsn.iot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.activity.AiotBActivity;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;


public class ParkMain_WEB extends AbsBaseFgt {
	ParseDatas pard;
	List<JiDi> listArray;
	private WebView wv;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = inflater.inflate(R.layout.layout_jdyq, container, false);
		initData();
		init();
	    return view;
	}
	//网络错误码判断
	private void initData(){
		pard = DataController.getJidiDapengData();
		
		if(pard != null){
			if(NetworkUtil.isErrorCode(pard.getCode(), getActivity())){
				listArray = new ArrayList<JiDi>();
				return;
			} else {
				listArray = (List<JiDi>)(pard.getObject());
			}
		} else {
			MessDialog.show1(getResources().getString(R.string.server_unusual_msg), getActivity());
			listArray = new ArrayList<JiDi>();
			return;
		}
	}
    @SuppressLint("JavascriptInterface")
	private void init(){//初始化
    	wv=(WebView)view.findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);//可用JS
        wv.addJavascriptInterface(new JavaScript_1(), "android");
        wv.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        wv.loadUrl("file:///android_asset/www/park/jdPark.html");
        wv.setWebChromeClient(new MyWebChromeClient());  
    }
    class JavaScript_1{
    	
    	/**
    	 * 获取基地园区页数据
    	 * **/
    	public String getAllDapengData(){
    		Gson gson = new Gson();
    		String str = gson.toJson(listArray);
    		return str;
    	}
    	
    	/*
    	 * 获取服务器路径
    	 */
    	public String getServerUrl(){
    		String url = Contents.getInstance().getServerUrl();
    		String[] result = url.split("/");
    		String str = result[0]+"//"+result[2];
    		return str;
    	}
    	
    	/**
    	 * 根据大棚id传入下一层级
    	 * **/
    	public void getDapengDataBydpId(String dpid){
    		
    		Dapeng dp =null;
    		for(int i=0;i<listArray.size();i++){
    			JiDi jdList = listArray.get(i);
    			for(int k=0;k<jdList.getList().size();k++){
    				if(dpid.equals(jdList.getList().get(k).getId())){
    					
    					dp=jdList.getList().get(k);
    				}
    			}
    		}
    		
    		AbsApplication.getInstance().setDapeng(dp);//大棚id存入全局变量
    		startInAbsFgtActivity(AiotBActivity.class, new Bundle());
    	}
    }
    class MyWebChromeClient extends WebChromeClient {    

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