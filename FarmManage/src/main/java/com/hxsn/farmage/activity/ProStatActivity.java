package com.hxsn.farmage.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.Shared;



@SuppressLint("SetJavaScriptEnabled")
public class ProStatActivity extends BaseActivity {

	private ImageButton su_pbackBtn;
    private WebView suProuStates_WV;
    private String code="";
    
    LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	Intent tabIntent;
	
	ImageView tab3IV,tab1IV;
	TextView  tab3IVltv,hometltv;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_su_prostat);
		
	   Intent ing=getIntent();
	   if(ing!=null)
		code=ing.getStringExtra("code");
		
		initView();
		initListener();
		
	}


	private void initView() {
		// TODO Auto-generated method stub
		su_pbackBtn=(ImageButton)findViewById(R.id.su_pbackBtn);
		suProuStates_WV=(WebView) findViewById(R.id.suProuStates_WV);
		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tab3IV=(ImageView)findViewById(R.id.tab3IV);
		tab3IV.setImageResource(R.drawable.guan01);
		tab3IVltv=(TextView)findViewById(R.id.tab3IVltv);
		tab3IVltv.setTextColor(getResources().getColor(R.color.font_color));
		
		tab1IV=(ImageView)findViewById(R.id.tab1IV);
		tab1IV.setBackgroundResource(R.drawable.jian);
		hometltv=(TextView)findViewById(R.id.hometltv);
		hometltv.setTextColor(Color.rgb(91, 91, 99));
		
		tabIntent = new Intent(ProStatActivity.this, MainActivity.class);
		
		WebSettings wSet = suProuStates_WV.getSettings();
		wSet.setJavaScriptEnabled(true);
		
		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		
//		suProuStates_WV.addJavascriptInterface(new JavaScriptObject(this), "wst");
//		String url = "file:///android_asset/lsll.html";
		suProuStates_WV.loadUrl(GetURL.ZHUISU+Shared.getUserID()+"&code="+code);

		suProuStates_WV.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});
		
		
	}
	
	private void initListener() {
		// TODO Auto-generated method stub
		su_pbackBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		su_pbackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent inte=new Intent(ProStatActivity.this,CaptureActivity.class);
				inte.putExtra("flag", "su");
				startActivity(inte);
				finish();
			}
		});
		
		// 模块跳转
				tab1Layout.setOnClickListener(new NewIVListener());
				tab2Layout.setOnClickListener(new NewIVListener());
				tab3Layout.setOnClickListener(new NewIVListener());
				tab4Layout.setOnClickListener(new NewIVListener());
	}

	

	class NewIVListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub

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
