package com.hxsn.iot.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.data.SensorScreen;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.FrameWork;
import com.hxsn.iot.model.Sensor;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FarmworkFragment extends AbsBaseFgt implements OnClickListener,OnPageChangeListener{
	
	private View view;
	private Dapeng dp;
	private String[] tabs  ; //名称
	private String[] cropNames;//单元作物名称
	private List<Danyuan> listDy;
	private RelativeLayout layout;
	private LinearLayout ll;
	private ViewPager viewPager;
	private ViewPagerAdapter vpAdapter;
	private LinearLayout monitorLayout;
	private LinearLayout linearLayout;
	private int currentIndex;
	private TextView jdNameTv;
	private TextView dpNameTv;
	private TextView growthName;
	//定义一个ArrayList来存放View
	private ArrayList<View> views;
	private LayoutInflater inflater;
    private ImageView[] points;
    private List<FrameWork> frameWorkList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.farmwork_layout, null);
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
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT);
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
	
	private void initData(){
		//获得单元列表
		listDy = DataController.getDanyuanOfDapeng(dp.getJidiname(),dp.getName());
		getHasDyCount();
		
		SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);
		// 获取存在本地的username
		String name = sharedata.getString("username", null);
		
        for (int i = 0; i < listDy.size(); i++) {
			final String id = listDy.get(i).getId();
			Contents.getInstance().setId(id);
			View view = inflater.inflate(R.layout.growth_period_content, null);
			ArrayList<HashMap<String,String>> list = null;
			ParseDatas parse = DataController.getGrowthList(name,id);
			
			if(parse != null){
				if(NetworkUtil.isErrorCode(parse.getCode(), getActivity())){
					list = new ArrayList<HashMap<String,String>>();
				} else {
					list = parse.getList();
				}
			} else {
				MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
				list = new ArrayList<HashMap<String,String>>();
			}
			
			ListView listview = (ListView) view.findViewById(R.id.growth_content_listview);
			listview.setAdapter(new GrowthAdapter(list));
			ImageButton addBtn = (ImageButton) view.findViewById(R.id.growth_content_addbtn);
			addBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addFarmwork(id);
				}
			});
			views.add(view);
        } 
        
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
        
        //初始化底部小点
        initPoint();
	}
	
	//添加农事活动
	private void addFarmwork(final String id) {
		//ArrayList<String> list = DataController.getGrowthTerm();
		frameWorkList = DataController.getGrowthTermObjectList();
		System.out.println(frameWorkList);
//		if(list == null){
//			list = new ArrayList<FrameWork>();
//		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final AlertDialog dialog = builder.create();
		View addView = inflater.inflate(R.layout.layout_add_farmwork,
				null);
		final Spinner spinner = (Spinner) addView.findViewById(R.id.growth_add_type);
		Button btn = (Button) addView.findViewById(R.id.growth_add_btn);
		final EditText et = (EditText) addView.findViewById(R.id.growth_add_mark);
		//ArrayAdapter<String> spinnerAdapter = null;// new ArrayAdapter<String>(getActivity(),R.layout.spinner_view_layout,R.id.spinner_textview,list);
		//spinner.setAdapter(spinnerAdapter);
		spinner.setAdapter(new MyAdapter(frameWorkList, getActivity()));
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(et.getText().toString().equals("") || et.getText().toString() == null){
					Toast.makeText(getActivity(), "备注不能为空", Toast.LENGTH_LONG).show();
					return;
				}
				String username = getActivity().getSharedPreferences("data", 0).getString("username", "");
				HashMap<String, String> map = DataController.getAddGrowth(username, id, spinner.getSelectedItem().toString(), et.getText().toString());
				
				if(map != null){
					if(NetworkUtil.isErrorCode(map.get("code"), getActivity())){
						
					} else {
						Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_LONG).show();
						dialog.dismiss();
					}
				} else {
					MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
				}
				
			}
		});
		dialog.setView(addView);
		dialog.show();
		WindowManager manager = getActivity().getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
		dialog.getWindow().setLayout(width, height/2);    
	}
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		linearLayout = (LinearLayout) view.findViewById(R.id.ll);       
		
        points = new ImageView[listDy.size()];
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT);
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
	
	//农事列表适配器
	private class GrowthAdapter extends BaseAdapter{
		
		private ArrayList<HashMap<String,String>> list;
		public GrowthAdapter(ArrayList<HashMap<String,String>> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			if (convertView == null) {
				// 把布局文件实例化为View对象
				convertView = getActivity().getLayoutInflater().inflate(R.layout.growth_content_listview_item,
						null);
//			}
			final ScrollView scrollview = (ScrollView) convertView.findViewById(R.id.growth_list_scrollview);
			TextView typeTv = (TextView) convertView.findViewById(R.id.growth_list_type);
			TextView nameTv = (TextView) convertView.findViewById(R.id.growth_list_name);
			TextView noteTv = (TextView) convertView.findViewById(R.id.growth_list_note);
			TextView timeTv = (TextView) convertView.findViewById(R.id.growth_list_time);
			typeTv.setText(list.get(position).get("type"));
			nameTv.setText(list.get(position).get("name"));
			noteTv.setText("备注："+list.get(position).get("mark"));
			timeTv.setText(list.get(position).get("time"));
			
			typeTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(scrollview.getVisibility() == View.GONE){ 
						scrollview.setVisibility(View.VISIBLE); 
					}else{ 
						scrollview.setVisibility(View.GONE); 
					} 
				}
			});
			nameTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(scrollview.getVisibility() == View.GONE){ 
						scrollview.setVisibility(View.VISIBLE); 
					}else{ 
						scrollview.setVisibility(View.GONE); 
					} 
				}
			});
			timeTv.setOnClickListener(new OnClickListener() {
	
				@Override
				public void onClick(View v) {
					if(scrollview.getVisibility() == View.GONE){ 
						scrollview.setVisibility(View.VISIBLE); 
					}else{ 
						scrollview.setVisibility(View.GONE); 
					} 
				}
			});
			return convertView;
		}
		
	}
	
	private class MyAdapter extends BaseAdapter{
		private List<FrameWork> list;
		private Context context;
		
		public MyAdapter(List<FrameWork> list, Context context){
			this.list = list;
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(list==null){
				return 0;
			}
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0).getId();
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = LayoutInflater.from(context);
			arg1 = inflater.inflate(R.layout.spinner_view_layout, null);
			if(arg1!=null)
			{
				TextView tv = (TextView) arg1.findViewById(R.id.spinner_textview);
				tv.setText(list.get(arg0).getName());
			}
			return arg1;
		}
		
	}
}
