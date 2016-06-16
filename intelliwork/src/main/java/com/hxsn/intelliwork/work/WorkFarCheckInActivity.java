package com.hxsn.intelliwork.work;


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

import com.hxsn.intelliwork.MainActivity;
import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseActivity;
import com.hxsn.intelliwork.myviews.LoadingDialog;
import com.hxsn.intelliwork.utils.GetURL;
import com.hxsn.intelliwork.utils.Shared;


public class WorkFarCheckInActivity extends BaseActivity {

	private WebView webView;
//	private String url ;
//	private ImageView iv;
//	private Bitmap bit;
	LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	Intent tabIntent;
	ImageButton ycczBackBtn;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_check_in);
		webView = (WebView) findViewById(R.id.check_webview);
		ycczBackBtn= (ImageButton) findViewById(R.id.ycczBackBtn);
//		iv=(ImageView) findViewById(R.id.check_iv);
//		try {
//			bit = BitmapFactory.decodeStream(this.getAssets().open("404.png"));
//			iv.setImageBitmap(bit);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (NetUtils.isConnected(this)){
//			url = "http://www.baidu.com";
//			webView.loadUrl(url);
//		}else{
//			webView.setVisibility(View.GONE);
//			iv.setVisibility(View.VISIBLE);
//		}
		
		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		
		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tabIntent = new Intent(WorkFarCheckInActivity.this, MainActivity.class);

		
		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
//		webView.addJavascriptInterface(new JavaScriptObject(this), "wst");
//		String url = "file:///android_asset/lsll.html";
		webView.loadUrl(GetURL.CAIZHAIDJ+Shared.getUserID());
		webView.setWebViewClient(new MyWebViewClient());
		
		ycczBackBtn.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
				finish();	
			}
		});
		
		
	}
	private class MyWebViewClient extends WebViewClient {
		public MyWebViewClient(){

		}
		
		public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面开始加载的时候
	    LoadingDialog.showLoading(WorkFarCheckInActivity.this);
	    webView.setEnabled(false);// 当加载网页的时候将网页进行隐藏
		super.onPageStarted(view, url, favicon);
		}

		public void onPageFinished(WebView view, String url) {// 网页加载结束的时候
			LoadingDialog.dissmissLoading();
			webView.setEnabled(true);
		}
		

	}
	
	
	class NewIVListener implements OnClickListener {
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.tab1:
					tabIntent.putExtra("tag", 0);
					startActivity(tabIntent);
					finish();
					break;
				case R.id.tab2:
					tabIntent.putExtra("tag", 1);
					startActivity(tabIntent);
					finish();
					break;
				case R.id.tab3:
					tabIntent.putExtra("tag", 2);
					startActivity(tabIntent);
					finish();
					break;
				case R.id.tab4:
					tabIntent.putExtra("tag", 3);
					startActivity(tabIntent);
					finish();
					break;
				default:
					break;
				}
			}

		}

}
