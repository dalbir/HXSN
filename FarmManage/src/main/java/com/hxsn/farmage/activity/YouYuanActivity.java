package com.hxsn.farmage.activity;

import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.Shared;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;



public class YouYuanActivity extends BaseActivity {

	WebView yyWebView;
	ImageButton yywbackBtn;
	LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	Intent tabIntent;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youyuan);
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tabIntent = new Intent(YouYuanActivity.this, MainActivity.class);
		yywbackBtn = (ImageButton) findViewById(R.id.yywbackBtn);
		yyWebView = (WebView) findViewById(R.id.yyWebView);
		WebSettings wSet = yyWebView.getSettings();
		wSet.setJavaScriptEnabled(true);

		String url = GetURL.DZDAOHANG+Shared.getUserID();
		yyWebView.loadUrl(url);

		yyWebView.setWebViewClient(new MyWebViewClient());
		
		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		
		yywbackBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		yywbackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		// 模块跳转
		tab1Layout.setOnClickListener(new NewIVListener());
		tab2Layout.setOnClickListener(new NewIVListener());
		tab3Layout.setOnClickListener(new NewIVListener());
		tab4Layout.setOnClickListener(new NewIVListener());

	}
	
    
	private class MyWebViewClient extends WebViewClient {
		public MyWebViewClient(){

		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    LoadingDialog.showLoading(YouYuanActivity.this);
	    yyWebView.setEnabled(false);
		super.onPageStarted(view, url, favicon);
		}


		@Override
		public void onPageFinished(WebView view, String url) {
			LoadingDialog.dissmissLoading();
			yyWebView.setEnabled(true);
		}
		

	}
  
    
	class NewIVListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub

			switch (view.getId()) {
			case R.id.tab1:
				tabIntent.putExtra("tag", 0);
				startActivity(tabIntent);
				break;
			case R.id.tab2:
				tabIntent.putExtra("tag", 1);
				startActivity(tabIntent);
				break;
			case R.id.tab3:
				tabIntent.putExtra("tag", 2);
				startActivity(tabIntent);
				break;
			case R.id.tab4:
				tabIntent.putExtra("tag", 3);
				startActivity(tabIntent);
				break;
			default:
				break;
			}
		}

	}
}
