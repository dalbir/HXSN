package com.hxsn.iot.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.JavaScript;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.data.SensorScreen;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.Sensor;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ControlJSFragment extends AbsBaseFgt implements OnClickListener,OnPageChangeListener{
	private Handler handler = new Handler();  
	private WebView webView;
	private int currentIndex;
    private View view;
    private String[] tabs  ; //名称
    private String[] cropNames;//单元作物名称
	private List<Danyuan> listDy;
	private RelativeLayout layout;
	private LinearLayout ll;
	private ViewPager viewPager;
	private ViewPagerAdapter vpAdapter;
	
	private TextView jdNameTv;
	private TextView dpNameTv;
	private LinearLayout monitorLayout;
	private TextView growthName;
	private LinearLayout linearLayout;
	private Dapeng dp;
	//定义一个ArrayList来存放View
	private ArrayList<View> views;

	private String ctrlPwd;
	
    private ImageView[] points;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences sf = getActivity().getSharedPreferences("data", 0);
		ctrlPwd = sf.getString("controlPwd", null);
		view = inflater.inflate(R.layout.layout_js_control, null);
		dp = AbsApplication.getInstance().getDapeng();
		initView();
		initData();
		initWeatherPost();
		initPwdView(view);
		return view;
	}
	
	private void initWeatherPost() {
		jdNameTv = (TextView) view.findViewById(R.id.weather_post_jdname);
		dpNameTv = (TextView) view.findViewById(R.id.weather_post_dpname);
		growthName = (TextView) view.findViewById(R.id.weather_post_growth_name);
		monitorLayout = (LinearLayout) view.findViewById(R.id.weather_post_monitor);
		
		jdNameTv.setText(" "+dp.getJidiname()+" ");  //加空格纯属为了界面好看
		dpNameTv.setText(" "+dp.getName()+"\\"+tabs[0]+" ");
		
		if(cropNames[0]==null || cropNames[0].equals("")){
			growthName.setVisibility(View.INVISIBLE);
		} else {
			growthName.setText(" "+cropNames[0]+" ");
			growthName.setVisibility(View.VISIBLE);
		}
		
		ParseDatas parse = DataController.getWeatherPost(dp.getJidiid());
		SensorScreen sensorScreen = getErrorCode(parse);
		LayoutParams mParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				  LayoutParams.WRAP_CONTENT);
		if(sensorScreen!=null){
			List<Sensor> sum = sensorScreen.getList();
			for (int i = 0; i < sum.size(); i++) {
				TextView tv = new TextView(getActivity());
				tv.setText(" "+sum.get(i).getName()+sum.get(i).getValue()+sum.get(i).getUnit()+" ");
				tv.setTextSize(20);
				tv.setTextColor(getActivity().getResources().getColor(R.color.weatherpost_blue));
				mParams.gravity = Gravity.CENTER_VERTICAL;
				tv.setLayoutParams(mParams);
				monitorLayout.addView(tv);
			}
		} else {
			TextView tv = new TextView(getActivity());
			tv.setText("气象哨数据为空");
			tv.setTextSize(20);
			tv.setTextColor(getActivity().getResources().getColor(R.color.weatherpost_blue));
			mParams.gravity = Gravity.CENTER_VERTICAL;
			tv.setLayoutParams(mParams);
			monitorLayout.addView(tv);
		}
    }
	
	private SensorScreen getErrorCode(ParseDatas parse){
    	SensorScreen sensorScreen = null;
    	if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), getActivity())){
				sensorScreen = new SensorScreen();
			} else {
				sensorScreen = (SensorScreen)parse.getObject();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			sensorScreen = new SensorScreen();
		}
    	return sensorScreen;
    }
	
	private void initView(){
		//layout = (RelativeLayout) view.findViewById(R.id.control_js_content);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		
		views = new ArrayList<View>();
		vpAdapter = new ViewPagerAdapter(views);
	}
	
	@SuppressLint("JavascriptInterface")
	private void initData(){
		//获得单元列表
		listDy = DataController.getDanyuanOfDapeng(dp.getJidiname(),dp.getName());
		getHasDyCount();
		
        for (int i = 0; i < listDy.size(); i++) {
			String id = listDy.get(i).getId();
			WebView webView = new WebView(getActivity());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl("file:///android_asset/www/control/control.html");
			webView.addJavascriptInterface(new JavaScript(id,getActivity()), "android");
			webView.setWebChromeClient(new MyWebChromeClient()); 
			views.add(webView);
        } 
        
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
        
        //初始化底部小点
        initPoint();
	}
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		linearLayout = (LinearLayout) view.findViewById(R.id.ll);       
		
        points = new ImageView[listDy.size()];
        LayoutParams mParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				  LayoutParams.WRAP_CONTENT);
        mParams.rightMargin = 5;
        //循环取得小点图片
        for (int i = 0; i < listDy.size(); i++) {
        	ImageView img = new ImageView(getActivity());
        	img.setClickable(true);
        	img.setImageResource(R.drawable.point);
        	img.setLayoutParams(mParams);
        	points[i] = img;
        	//默认都设为灰色
        	points[i].setEnabled(true);
        	//给每个小点设置监听
        	points[i].setOnClickListener(this);
        	//设置位置tag，方便取出与当前位置对应
        	points[i].setTag(i);
        	linearLayout.addView(img);
        }
        
        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
	}
	
	/**
	 * 当滑动状态改变时调用
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	
	/**
	 * 当当前页面被滑动时调用
	 */

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	
	/**
	 * 当新的页面被选中时调用
	 */

	@Override
	public void onPageSelected(int position) {
		//设置底部小点选中状态
        setCurDot(position);
        dpNameTv.setText(dp.getName()+"\\"+tabs[position]);
        if(cropNames[position]==null || cropNames[position].equals("")){
			growthName.setVisibility(View.INVISIBLE);
		} else {
			growthName.setText(" "+cropNames[position]+" ");
			growthName.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 通过点击事件来切换当前的页面
	 */
	@Override
	public void onClick(View v) {
		 int position = (Integer)v.getTag();
         setCurView(position);
         setCurDot(position);		
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position){
         if (position < 0 || position >= listDy.size()) {
             return;
         }
         viewPager.setCurrentItem(position);
     }

     /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon){
         if (positon < 0 || positon > listDy.size() - 1 || currentIndex == positon) {
             return;
         }
         points[positon].setEnabled(false);
         points[currentIndex].setEnabled(true);

         currentIndex = positon;
     }
	
	//根据传入的基地和大棚名称获得对应的单元列表
	private void getHasDyCount() {
		tabs = new String[listDy.size()];
		cropNames = new String[listDy.size()];
		for (int i = 0; i < listDy.size(); i++) {
			tabs[i] = listDy.get(i).getName();
			cropNames[i] = listDy.get(i).getCrop();
		}
	}
	
	private void initPwdView(View view){
		SharedPreferences preference = getActivity().getSharedPreferences("data", 0);
		final String pwd = preference.getString("controlPwd", "");
		final EditText et = (EditText) view.findViewById(R.id.control_js_pwd_ed);
		Button btn = (Button) view.findViewById(R.id.control_js_pwd_btn);
		final LinearLayout ll = (LinearLayout) view.findViewById(R.id.control_js_pwd_layout);
		final RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.weather_post);
		if(Contents.getInstance().getIsChecked() || pwd.equals("")){
			viewPager.setVisibility(View.VISIBLE);
			rl.setVisibility(View.VISIBLE);
			linearLayout.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
		}
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(et.getText().toString().equals(pwd)){
					viewPager.setVisibility(View.VISIBLE);
					rl.setVisibility(View.VISIBLE);
					linearLayout.setVisibility(View.VISIBLE);
					ll.setVisibility(View.GONE);
				} else if(et.getText().toString().equals("")){
					Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "密码不正确", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
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
				String defaultValue, final JsPromptResult result) {
			/**
			// TODO Auto-generated method stub
			LayoutInflater infalter = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View contentView = infalter.inflate(R.layout.layout_control_pwd_confirm, null);
			final EditText et = (EditText) contentView.findViewById(R.id.ctrlPwd);
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(contentView).setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences sf = ControlJSFragment.this.getActivity().getSharedPreferences("data", 0);
					String pwd = et.getText().toString();
					if(pwd.equals(ctrlPwd)&&!StringUtils.isNullOrEmpty(ctrlPwd)){
						result.confirm("true");
					}else{
						Toast.makeText(getActivity(), "控制密码错误", Toast.LENGTH_SHORT).show();
						result.confirm("false");
					}
				}
				
			}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					result.cancel();
					
				}
			}).setTitle("控制权限确认").show();
			builder.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if(keyCode==KeyEvent.KEYCODE_BACK){
						result.cancel();
					}
					return true;
				}
			});
			return true;
			*/
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

    }

}
