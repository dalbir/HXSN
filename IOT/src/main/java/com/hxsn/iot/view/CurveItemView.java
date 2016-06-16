package com.hxsn.iot.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hxsn.iot.R;
import com.hxsn.iot.control.LoginController;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.model.QxEntity;
import com.hxsn.iot.util.NetworkUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CurveItemView extends LinearLayout{

	private Map<String,Object> map;
	private String id;
	private Context context;
	private WebView wv;
	private LinearLayout layout;
	private int[] series;
	
	public CurveItemView(Context context, AttributeSet attrs, String id) {
		super(context, attrs);
		this.context = context;
		this.id = id;
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_linechart,this);
		initView();
	}
	
	private void initView(){
		LoginController lc = new LoginController();
		map = lc.getQxData(id);
		
		if(map != null){
			if(NetworkUtil.isErrorCode((String) (map.get("code")), context)){
				map = new HashMap<String,Object>();
				return;
			} 
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), context);
			map = new HashMap<String,Object>();
			return;
		}
		
//		TextView dpName = (TextView) findViewById(R.id.line_textview_name);
    	TextView growthName = (TextView) findViewById(R.id.line_growth_name);
    	TextView time = (TextView) findViewById(R.id.line_current_time);
    	
//    	dpName.setText(AbsApplication.getInstance().getDapeng().getName());
    	growthName.setText(map.get("growth").toString());
    	time.setText(NetworkUtil.convertTimeFormat(map.get("time").toString()));
    	
    	layout = (LinearLayout) findViewById(R.id.line_checkbox_layout);
    	addCheckBoxOfLayout();
    	
    	wv=(WebView)findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);//可用JS 
        wv.addJavascriptInterface(new JavaScript2(), "android");
        wv.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
        wv.loadUrl("file:///android_asset/www/index.html");
        wv.setWebChromeClient(new MyWebChromeClient());  
	}
	
	private void addCheckBoxOfLayout(){
    	List<QxEntity> list = (List<QxEntity>) map.get("list");
    	//这里分为测试状态和实际状态，不知道为什么本地xml解析出来会与服务器不同（原因：解析方法有误、xml文件不同），代码没有精简
    	if(DataController.isTest){
    		series = new int[list.size()/3];
    		//参数从5开始是因为解析执行15此，前五次为空，具体可打log验证（结果分别为5,7,9,11,13....）
        	for (int i = 5; i < list.size(); i+=2) {
    			CheckBox cb = new CheckBox(context);
    			cb.setText(list.get(i).getYdesc());
    			cb.setTextColor(Color.BLACK);
    			cb.setChecked(true);
    			final int tag = i;
    			series[(i-5)/2]=1;
    			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    				
    				@Override
    				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    						if(isChecked){
    							series[(tag-5)/2]=1;
    						} else {
    							series[(tag-5)/2]=0;
    						}
    						refreshLineView();
    				}   
    			});
    			if(list.get(i).getYdesc()!=null){
    				layout.addView(cb);
    			}
    		}
    	} else {
    		series = new int[list.size()-2];
    		for (int i = 2; i < list.size(); i++) {
    			CheckBox cb = new CheckBox(context);
    			cb.setText(list.get(i).getYdesc());
    			cb.setTextColor(Color.BLACK);
    			cb.setChecked(true);
    			final int tag = i;
    			series[i-2]=1;
    			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    				
    				@Override
    				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    						if(isChecked){
    							series[tag-2]=1;
    						} else {
    							series[tag-2]=0;
    						}
    						refreshLineView();
    				}   
    			});
    			if(list.get(i).getYdesc()!=null){
    				layout.addView(cb);
    			}
    		}
    	}
    	
    }
    
    private void refreshLineView(){
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < series.length; i++) {
    		sb.append(series[i]).append(",");
		}
    	String str = sb.toString();
    	wv.loadUrl("javascript:changeLine('"+str+"')");  
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
    
     class JavaScript2{
    	
		//获取曲线信息
		
		public String getQxData(){
			
			List<QxEntity> list = new ArrayList<QxEntity>();
			list = (List<QxEntity>) map.get("list");
			Gson gson = new Gson();
			String str = gson.toJson(list);
			Log.i("Qx","曲线数据:"+str);
			return str;
		}
    	 public void goBack(){
// 			Intent intent = new Intent(LineChartFragment.this,AiotBActivity.class);
// 			startActivity(intent);
// 			LineChartFragment.this.finish();
 		}
    }

}
