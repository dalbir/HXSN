package com.hxsn.farmage.activity;

import android.annotation.SuppressLint;
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

import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;


/**
 *
 */
@SuppressLint("ShowToast")
public class SuggestionActivity extends BaseActivity {

	ImageButton sugstBackBtn;
	LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	Intent tabIntent;
	WebView suggestionWV;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);
		initView();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tabIntent = new Intent(SuggestionActivity.this, MainActivity.class);

		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		
		suggestionWV=(WebView)findViewById(R.id.suggestionWV);
		WebSettings wSet = suggestionWV.getSettings();
		wSet.setJavaScriptEnabled(true);
    	suggestionWV.loadUrl(GetURL.YJFK);

    	suggestionWV.setWebViewClient(new WebViewClient() {
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
		
		
		sugstBackBtn = (ImageButton) this.findViewById(R.id.sugstBackBtn);
		sugstBackBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		sugstBackBtn.setOnClickListener(new OnClickListener() {
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
