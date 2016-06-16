package com.hxsn.town.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

import com.hxsn.town.activity.PayActivity;
import com.hxsn.town.data.IConnPars;

public class MyFragment extends Fragment {
	protected static WebView webView;
	
	public static final int SUCCESS = 1;
	public static final int REQ_CODE = 1;
	public static final int FAIL = -1;
	public static final int WAIT = 0;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode){
			case SUCCESS:
				webView.loadUrl(IConnPars.SUCCESS);
				break;
			case FAIL:
				webView.loadUrl(IConnPars.FAIL);
				break;
			case WAIT:
				webView.loadUrl(IConnPars.WAIT);
				break;
			default:
				break;
		}
	}
	
	protected class JavaScript{
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
    		startActivityForResult(intent, REQ_CODE);
    	}
    }
}
