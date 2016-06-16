package com.hxsn.farmage.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.connect.MyReceiver;
import com.hxsn.farmage.js.JavaScriptObject;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.Shared;


@SuppressLint("SetJavaScriptEnabled")
public class DistinctListActivity extends BaseActivity {

	
	ImageButton dqnavBtn,dqbackBtn;
	TextView dqfbTV;
	WebView diatinctWV;
	MyReceiver myReceiver;
	IntentFilter intentFilter;
	LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	Intent tabIntent;
    String title="";
    String code="";
    Intent inte=null;
    public String firstTitle="";
    public boolean flag=true;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_distinct);
		initView();
	
	}

	public TextView getDqfbTV() {
		return dqfbTV;
	}

	public void setDqfbTV(TextView dqfbTV) {
		this.dqfbTV = dqfbTV;
	}

	private void initView() {
		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tabIntent=new Intent(DistinctListActivity.this,MainActivity.class);
		
		
		MyApplication myApplication = (MyApplication) this.getApplicationContext();
	    myApplication.dkActivity = this;
		
		dqnavBtn=(ImageButton)findViewById(R.id.dqnavBtn);
		dqbackBtn=(ImageButton)findViewById(R.id.dqbackBtn);
		dqfbTV=(TextView)findViewById(R.id.dqfbTV);
	
		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		
		diatinctWV=(WebView)findViewById(R.id.diatinctWV);
		WebSettings wSet = diatinctWV.getSettings();
		wSet.setJavaScriptEnabled(true);
		
		diatinctWV.setVisibility(View.VISIBLE); 
		diatinctWV.addJavascriptInterface(new JavaScriptObject(this), "wst");
	    Log.i("urll", GetURL.DKFQ+Shared.getUserID());
        Handler mHandler = new Handler();

        mHandler.post(new Runnable() { 
			@Override
			public void run() {
				diatinctWV.loadUrl(GetURL.DKFQ+Shared.getUserID().toString()); 
			} 
        });
    
        
		diatinctWV.setWebViewClient(new MyWebViewClient());
		dqnavBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		dqnavBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent inte=new Intent(DistinctListActivity.this,YouYuanActivity.class);
				startActivity(inte);
			}
		});
		
		
		dqbackBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		dqbackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				if(firstTitle.equals(dqfbTV.getText().toString()))
					finish();
				else
				{
					diatinctWV.goBack();   //后
				} 
			}
		});
		 myReceiver = new MyReceiver();
		 intentFilter = new IntentFilter("com.hxsn.action.jiankong.broadcast");
		//模块跳转
				tab1Layout.setOnClickListener(new NewIVListener());
				tab2Layout.setOnClickListener(new NewIVListener());
				tab3Layout.setOnClickListener(new NewIVListener());
				tab4Layout.setOnClickListener(new NewIVListener());

	}
	
	
	private class MyWebViewClient extends WebViewClient {
		public MyWebViewClient(){

		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面�?始加载的时�??
	    LoadingDialog.showLoading(DistinctListActivity.this);
	    diatinctWV.setEnabled(false);// 当加载网页的时�?�将网页进行隐藏
		super.onPageStarted(view, url, favicon);
		}


		@Override
		public void onPageFinished(WebView view, String url) {// 网页加载结束的时�?
			LoadingDialog.dissmissLoading();
			diatinctWV.setEnabled(true);
		}
		

	}
	
	
	
	@Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
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
