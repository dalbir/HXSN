package com.hxsn.iot.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.data.SensorScreen;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.Sensor;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.view.MonitorItemView;
import com.hxsn.iot.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MonitorDataFragment extends AbsBaseFgt implements OnClickListener,OnPageChangeListener{
	private ViewPager viewPager;
	
	private ViewPagerAdapter vpAdapter;
	
	//定义一个ArrayList来存放View
	private ArrayList<View> views;

    private ImageView[] points;
    
    //记录当前选中位置
    private int currentIndex;
    private View view;
    String[] tabs  ; //单元名称
    String[] cropNames;//单元作物名称
	private List<Danyuan> listDy;
	private Dapeng dp;
	
	private TextView jdNameTv;
	private TextView dpNameTv;
	private LinearLayout monitorLayout;
	private TextView growthName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	view = inflater.inflate(R.layout.monitor_main, null);
    	dp = AbsApplication.getInstance().getDapeng();
    	initView();
		initData();
		initWeatherPost();
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
			} else {
				sensorScreen = (SensorScreen)parse.getObject();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
		}
    	return sensorScreen;
    }

	/**
	 * 初始化组件
	 */
	private void initView(){
		
		views = new ArrayList<View>();
		
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		
		vpAdapter = new ViewPagerAdapter(views);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		//获得单元列表
		listDy = DataController.getDanyuanOfDapeng(dp.getJidiname(),dp.getName());
		getHasDyCount();
		
        for (int i = 0; i < listDy.size(); i++) {
			String id = listDy.get(i).getId();
			MonitorItemView item = new MonitorItemView(getActivity(),null,id);
			views.add(item);
        } 
        
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
        
        //初始化底部小点
        initPoint();
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
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll);       
		
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
        AbsApplication.getInstance().setCurDy(dp.getList().get(currentIndex));
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
        Danyuan dy = dp.getList().get(position);
        AbsApplication.getInstance().setCurDy(dy);
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
    
  //多个监测数据比较，从跟activit得到调用
  	public void showMonitorCompare(LayoutInflater layoutInflater,Activity context,ArrayList<Danyuan> data) {
  		String[][] names = new String[data.size()][20];//数组定死了20个，监测数据类型如超过20个在这里更改
  		String[][] normals = new String[data.size()][20];//监测数据是否正常
  		String[] title = null;
  		int p = 0;
  		List<Sensor> protoSensors = null;
  		for (Danyuan dy : data) {
  			ParseDatas parse = DataController.getDapeData(dy.getId());
  			SensorScreen sensorScreen = getErrorCode(parse);
  			if(sensorScreen==null){
  				if(p==0){
  					Toast.makeText(context, "当前单元没有数据,无法进行数据对比", Toast.LENGTH_SHORT).show();
  					return ;
  				}
  				names[p][0] = dy.getName();
		  		normals[p][0] = "1";
		  		for (int j = 1; j <= protoSensors.size(); j++) {
		  			names[p][j] = "0"+protoSensors.get(j-1).getUnit();
		  			normals[p][j] = "0";
		  		}
  			}else{
		  		List<Sensor> sensor = sensorScreen.getList();
		  		if(p == 0){
		  			protoSensors = sensor;
		  		//得到title数据
		  			title = new String[sensor.size()+1];
		  			title[0] = "名称";
		  			for (int j = 1; j <= sensor.size(); j++) {
		  				title[j] = sensor.get(j-1).getName();
		  			}
		  		}
		  		names[p][0] = dy.getName();
		  		normals[p][0] = "1";
		  		for (int j = 1; j <= sensor.size(); j++) {
		  			names[p][j] = sensor.get(j-1).getValue()+sensor.get(j-1).getUnit();
		  			normals[p][j] = sensor.get(j-1).getNormal();
		  		}
  			}
	  		p++;
  		}
      	View mView = layoutInflater.inflate(R.layout.monitor_compare_view, null); 
      	
      	LinearLayout titleLayout = (LinearLayout) mView.findViewById(R.id.monitor_value_tital);
      	LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
      	lp.width = 110;//title宽度
      	if(title!=null){
      		for (int i = 0; i < title.length; i++) {
      			TextView tv = new TextView(context);
      			tv.setText(title[i]);
      			tv.setTextColor(context.getResources().getColor(R.color.monitor_compare_title));
      			tv.setTextSize(12);
      			tv.setLayoutParams(lp);
      			//tv.setSingleLine(true);
      			titleLayout.addView(tv);
      		}
      	}
      	
      	ListView listView = (ListView) mView.findViewById(R.id.monitor_value_listview);
      	listView.setAdapter(new MonitorListAdapter(names,normals,context));
      	final AlertDialog builder = new AlertDialog.Builder(context).create();
      	builder.setView(mView);
      	builder.show();
      	Button sureBtn = (Button) mView.findViewById(R.id.monitor_value_btn_sure);
      	sureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
      	/*WindowManager manager = context.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
		builder.getWindow().setLayout(width, height/2);*/  
  	}
  	
  	private class MonitorListAdapter extends BaseAdapter {
  		private String[][] names;
  		private String[][] normals;
  		private LayoutInflater layoutInflater;
  		private Context context;
  		public MonitorListAdapter(String[][] names,String[][] normals,Context context) {
  			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  			this.names = names;
  			this.normals = normals;
  			this.context = context;
  		}
  		@Override
  		public int getCount() {
  			return names.length;
  		}

  		@Override
  		public Object getItem(int arg0) {
  			return names[arg0];
  		}

  		@Override
  		public long getItemId(int arg0) {
  			return arg0;
  		}

  		@Override
  		public View getView(int arg0, View convertView, ViewGroup arg2) {
  			if (convertView == null) {
  				convertView = layoutInflater.inflate(R.layout.monitor_compare_listview_item,
  						null);
  			}
  			LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.monitor_compare_listview_item_ll);
  			if(ll.getChildCount() != 0){
  				ll.removeAllViews();
  			}
  			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
  	    	lp.width = 110;//title宽度
  			for (int i = 0; i < names[arg0].length; i++) {
  				if(names[arg0][i] != null){
  					TextView tv = new TextView(context);
  					tv.setText(names[arg0][i]);
  					tv.setLayoutParams(lp);
  					tv.setTextSize(12);
  					if(normals[arg0][i].equals("0")){
  						tv.setTextColor(Color.RED);
  					}
  					ll.addView(tv);
  				}
  			}
  			
  			return convertView;
  		}
  	}

}
