package com.hxsn.farmage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hxsn.farmage.R;
import com.hxsn.farmage.activity.DistinctListActivity;
import com.hxsn.farmage.activity.ProductDetailActivity;
import com.hxsn.farmage.activity.YouYuanActivity;
import com.hxsn.farmage.base.BaseFrgament;
import com.hxsn.farmage.js.JavaScriptObject;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.Shared;



public class JianKongFragment extends BaseFrgament  {

	ImageButton headlBtn, navIV;
	TextView youyuanTV, yypdTV;
	WebView youyuanWV;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_jiankong_fragment, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		yypdTV = (TextView) view.findViewById(R.id.yypdTV);

		headlBtn = (ImageButton) view.findViewById(R.id.headlBtn);
		navIV = (ImageButton) view.findViewById(R.id.navIV);
		youyuanWV = (WebView) view.findViewById(R.id.youyuanWV);
		WebSettings wSet = youyuanWV.getSettings();
		wSet.setJavaScriptEnabled(true);

		youyuanWV.addJavascriptInterface(new JavaScriptObject(getActivity()), "wst");
		String url = GetURL.YOUYUAN+Shared.getUserID();
		Log.i("JiankongFragment","Url="+url);
		youyuanWV.loadUrl(url);
		youyuanWV.setWebViewClient(new MyWebViewClient());

		headlBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		headlBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent inte = new Intent(getActivity(),
						DistinctListActivity.class);
				startActivity(inte);
			}
		});

		navIV.setOnTouchListener(ImgBtnEffact.btnTL);
		navIV.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent inte = new Intent(getActivity(), YouYuanActivity.class);
				startActivity(inte);
			}
		});


		yypdTV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent inte = new Intent(getActivity(), ProductDetailActivity.class);
				startActivity(inte);
			}
		});
	}


	




private class MyWebViewClient extends WebViewClient {
	public MyWebViewClient(){

	}
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面
    LoadingDialog.showLoading(getActivity());
	youyuanWV.setEnabled(false);// 当加载网页的将网页进行隐藏
	super.onPageStarted(view, url, favicon);
	}


	@Override
	public void onPageFinished(WebView view, String url) {// 网页加载结束的时
		LoadingDialog.dissmissLoading();
		youyuanWV.setEnabled(true);
	}
	

}

}
