package com.hxsn.iot.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.activity.AlarmMsgShowActivity;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.util.DataConnection;
import com.hxsn.iot.util.OnArticleSelectedListener;
import com.hxsn.iot.view.SwipeListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class AlarmMsgDPFragment extends AbsBaseFgt {
	private static final String TAG = "AlarmMsgDPFragment";
	private View view;
	private SwipeListView listAll;
	private SwipeListView listReaded;
	private SwipeListView listToday;
	
	private TextView allEmptyTv;
	private TextView readedEmptyTv;
	private TextView todayEmptyTv;
	
	//消费码
	private static final int LIST_ALL_NEW = 0x1;
	private static final int LIST_ALL_MORE = 0x2;
	private static final int LIST_READED_NEW = 0x3;
	private static final int LIST_READED_MORE = 0x4;
	private static final int LIST_TODAY_NEW = 0x5;
	private static final int LIST_TODAY_MORE = 0x6;
	
	private int listSizeAll = 0;
	private int listSizeReaded = 0;
	private int listSizeToday  = 0;
	
	private ArrayList<HashMap<String, String>> arrayAll;
	private ArrayList<HashMap<String, String>> arrayReaded;
	private ArrayList<HashMap<String, String>> arrayToday;
	
	private MyAdapter allAdapter;
	private MyAdapter readedAdapter;
	private MyAdapter todayAdapter;
	
	private TabHost tabHost;
	
	private OnArticleSelectedListener mListener;
	private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Intent showMsgIntent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		showMsgIntent = new Intent(getActivity(), AlarmMsgShowActivity.class);
		view = inflater.inflate(R.layout.alarm_dp_msg_layout, container, false);
		initView();
		return view;
	}
	
	private void initView() {
        tabHost = (TabHost) view.findViewById(R.id.tabhost);  
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost  
        tabHost.setup();  
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("全部")
        		.setContent(R.id.lv_alarm_list_all_linear));  
  
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("已读")  
                .setContent(R.id.lv_alarm_list_all_already_linear));  
  
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("今日")  
                .setContent(R.id.lv_alarm_list_all_today_linear)); 
        updateTab(tabHost);
        tabHost.setOnTabChangedListener(new OnTabChangedListener());
        listAll = (SwipeListView) view.findViewById(R.id.lv_alarm_list_all);
        listAll.setListner(new SwipeListView.OnloadListner() {
			
			@Override
			public void loadNew() {
				// TODO Auto-generated method stub
				final String dpId = AbsApplication.getInstance().getDapeng().getId();
				new Thread(new Runnable(){
					@Override
					public void run() {
						Looper.prepare();
						// TODO Auto-generated method stub
						ArrayList<HashMap<String, String>> list = DataConnection.conDbDPPhoneAlarmOfType(getActivity(), 0, "all", dpId);
						Message msg = new Message();
						msg.what = LIST_ALL_NEW;
						msg.obj = list;
						handler.sendMessage(msg);
						Looper.loop();
					}}).start();
			}
			
			@Override
			public void loadMore() {
				// TODO Auto-generated method stub
				final String dpId = AbsApplication.getInstance().getDapeng().getId();
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						ArrayList<HashMap<String, String>> list = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),listSizeAll,"all",dpId);
						Message msg = new Message();
						msg.what = LIST_ALL_MORE;
						msg.obj = list;
						handler.sendMessage(msg);
						Looper.loop();
					}}).start();	
			}
		});
        listReaded = (SwipeListView) view.findViewById(R.id.lv_alarm_list_all_already);
        listReaded.setListner(new SwipeListView.OnloadListner() {
			
			@Override
			public void loadNew() {
				// TODO Auto-generated method stub
				final String dpId = AbsApplication.getInstance().getDapeng().getId();
				new Thread(new Runnable(){
					@Override
					public void run() {
						Looper.prepare();
						// TODO Auto-generated method stub
						ArrayList<HashMap<String, String>> list = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),0,"readed",dpId);
						Message msg = new Message();
						msg.what = LIST_READED_NEW;
						msg.obj = list;
						handler.sendMessage(msg);
						Looper.loop();
					}}).start();
			}
			
			@Override
			public void loadMore() {
				// TODO Auto-generated method stub
				final String dpId = AbsApplication.getInstance().getDapeng().getId();
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						ArrayList<HashMap<String, String>> list = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),listSizeReaded,"readed",dpId);
						Message msg = new Message();
						msg.what = LIST_READED_MORE;
						msg.obj = list;
						handler.sendMessage(msg);
						Looper.loop();
					}}).start();	
			}
		});;
        listToday = (SwipeListView) view.findViewById(R.id.lv_alarm_list_all_today);
        listToday.setListner(new SwipeListView.OnloadListner() {
			
        	@Override
			public void loadNew() {
				// TODO Auto-generated method stub
				final String dpId = AbsApplication.getInstance().getDapeng().getId();
				new Thread(new Runnable(){
					@Override
					public void run() {
						Looper.prepare();
						// TODO Auto-generated method stub
						ArrayList<HashMap<String, String>> list = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),0,"today",dpId);
						Message msg = new Message();
						msg.what = LIST_TODAY_NEW;
						msg.obj = list;
						handler.sendMessage(msg);
						Looper.loop();
					}}).start();
			}
			
			@Override
			public void loadMore() {
				// TODO Auto-generated method stub
				final String dpId = AbsApplication.getInstance().getDapeng().getId();
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						ArrayList<HashMap<String, String>> list = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),listSizeToday,"today",dpId);
						Message msg = new Message();
						msg.what = LIST_TODAY_MORE;
						msg.obj = list;
						handler.sendMessage(msg);
						Looper.loop();
					}}).start();	
			}
		});
        
        allEmptyTv = (TextView) view.findViewById(R.id.lv_alarm_list_all_empty_tv);
        readedEmptyTv = (TextView) view.findViewById(R.id.lv_alarm_list_all_already_tv);
        todayEmptyTv = (TextView) view.findViewById(R.id.lv_alarm_list_all_today_tv);
        
        initData();//放在这里主要用到没有数据时的显示
        
	}
	
	class OnTabChangedListener implements OnTabChangeListener {  
		  
		  
        @Override  
        public void onTabChanged(String tabId) {  
            tabHost.setCurrentTabByTag(tabId);  
            updateTab(tabHost);  
        } 
	}
	private void updateTab(final TabHost tabHost) {  
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {  
            View view = tabHost.getTabWidget().getChildAt(i);  
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);  
            tv.setTextSize(16);  
            tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格  
            if (tabHost.getCurrentTab() == i) {//选中  
                //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_current));//选中后的背景  
                tv.setTextColor(this.getResources().getColorStateList(  
                		R.color.tab_textcolor));  
            } else {//不选中  
               // view.setBackgroundDrawable(getResources().getDrawable(R.drawable.category_bg));//非选择的背景  
                tv.setTextColor(this.getResources().getColorStateList(  
                        android.R.color.tab_indicator_text));  
            }  
        }  
    }  
	
	private void isDeleteMethod(){
		if(arrayAll != null){
			arrayAll.clear();
		}
		if(arrayReaded != null){
			arrayReaded.clear();
		}
		if(arrayToday != null){
			arrayToday.clear();
		}
		initData();
		if(allAdapter!=null){
			allAdapter.notifyDataSetChanged();
		}
		if(readedAdapter!=null){
			readedAdapter.notifyDataSetChanged();
		}
		if(todayAdapter!=null){
			todayAdapter.notifyDataSetChanged();
		}
	}
	
	private void initData(){
		Contents.getInstance().setIsSingleAlarm(true);//标签是否进入单棚界面
		String dpId = AbsApplication.getInstance().getDapeng().getId();
		arrayAll = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),0,"all",dpId);
		listSizeAll = arrayAll.size();
        arrayReaded = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),0,"readed",dpId);
        listSizeReaded = arrayReaded.size();
        arrayToday = DataConnection.conDbDPPhoneAlarmOfType(getActivity(),0,"today",dpId);
        listSizeToday = arrayToday.size();
        if(arrayAll == null){
        	listAll.setVisibility(View.GONE);
        	allEmptyTv.setVisibility(View.VISIBLE);
        } else {
        	listAll.setVisibility(View.VISIBLE);
        	allEmptyTv.setVisibility(View.GONE);
        	allAdapter = new MyAdapter(arrayAll,getActivity(),listAll.getRightViewWidth());
        	listAll.setAdapter(allAdapter);
        	
        	listAll.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    					long arg3) {
    				int position = arg2-listAll.getHeaderViewsCount();
    				String id= allAdapter.getMsgid(position);//报警详情msgid
    				//getContentView(id,position);
    				DataConnection.conUpdateReadAlarm(getActivity(), id);
    				HashMap<String, String> item = (HashMap<String, String>) allAdapter.getItem(position);
    				item.put("status", "readed");
    				ViewHolder holder = (ViewHolder) arg1.getTag();
    				holder.readState.setText("已读");
    				holder.readState.setTextColor(Color.BLACK);
    				holder.level.setBackgroundResource(R.drawable.alarm_normal);
    				holder.dp_name.setBackgroundColor(getResources().getColor(R.color.alarm_dpname_bg));
    				showMsgIntent.putExtra("id", id);
    				startActivity(showMsgIntent);
    			}
    		});
        	
        	allAdapter.setOnRightItemClickListener(new onRightItemClickListener() {
            	
                @Override
                public void onRightItemClick(View v, int position) {
                	allAdapter.getMsgid(position);
                	DataConnection.deleteAlarmPhoneData(allAdapter.getMsgid(position), getActivity());
                	initData();
//                	arrayAll.clear();
//                	arrayAll.addAll(DataConnection.conDbPhoneAlarmOfType(getActivity(),listSizeAll,"all"));
//                	isDeleteMethod();
                }
            });
        }
        
        if(arrayReaded == null){
        	listReaded.setVisibility(View.GONE);
        	readedEmptyTv.setVisibility(View.VISIBLE);
        } else {
        	listReaded.setVisibility(View.VISIBLE);
        	readedEmptyTv.setVisibility(View.GONE);
        	readedAdapter = new MyAdapter(arrayReaded,getActivity(),listReaded.getRightViewWidth());
        	listReaded.setAdapter(readedAdapter);
        	
        	listReaded.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    					long arg3) {
    				
    				int position = arg2-listAll.getHeaderViewsCount();
    				String id= allAdapter.getMsgid(position);//报警详情msgid
    				//getContentView(id,position);
    				DataConnection.conUpdateReadAlarm(getActivity(), id);
    				ViewHolder holder = (ViewHolder) arg1.getTag();
    				holder.readState.setText("已读");
    				holder.readState.setTextColor(Color.BLACK);
    				holder.level.setBackgroundResource(R.drawable.alarm_normal);
    				holder.dp_name.setBackgroundColor(getResources().getColor(R.color.alarm_dpname_bg));
    				showMsgIntent.putExtra("id", id);
    				startActivity(showMsgIntent);
    			}
    		});
        	
        	readedAdapter.setOnRightItemClickListener(new onRightItemClickListener() {
            	
                @Override
                public void onRightItemClick(View v, int position) {
                	readedAdapter.getMsgid(position);
                	DataConnection.deleteAlarmPhoneData(readedAdapter.getMsgid(position), getActivity());
                	initData();
                	//isDeleteMethod();
                }
            });
        }
        
        if(arrayToday == null){
        	listToday.setVisibility(View.GONE);
        	todayEmptyTv.setVisibility(View.VISIBLE);
        } else {
        	listToday.setVisibility(View.VISIBLE);
        	todayEmptyTv.setVisibility(View.GONE);
        	todayAdapter = new MyAdapter(arrayToday,getActivity(),listToday.getRightViewWidth());
        	listToday.setAdapter(todayAdapter);
        	
        	listToday.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    					long arg3) {
    				int position = arg2-listAll.getHeaderViewsCount();
    				String id= allAdapter.getMsgid(position);//报警详情msgid
    				//getContentView(id,position);
    				DataConnection.conUpdateReadAlarm(getActivity(), id);
    				HashMap<String, String> item = (HashMap<String, String>) allAdapter.getItem(position);
    				item.put("status", "readed");
    				ViewHolder holder = (ViewHolder) arg1.getTag();
    				holder.readState.setText("已读");
    				holder.readState.setTextColor(Color.BLACK);
    				holder.level.setBackgroundResource(R.drawable.alarm_normal);
    				holder.dp_name.setBackgroundColor(getResources().getColor(R.color.alarm_dpname_bg));
    				showMsgIntent.putExtra("id", id);
    				startActivity(showMsgIntent);
    			}
    		});
        	
        	todayAdapter.setOnRightItemClickListener(new onRightItemClickListener() {
            	
                @Override
                public void onRightItemClick(View v, int position) {
                	todayAdapter.getMsgid(position);
                	DataConnection.deleteAlarmPhoneData(todayAdapter.getMsgid(position), getActivity());
                	initData();
                	//isDeleteMethod();
                }
            });
        }
        
	}
	/*
	// 详情-----------
	private void getContentView(final String msgid, final int position) {
		try {
			DataConnection.conUpdateReadAlarm(getActivity(), msgid);
			DataConnection.conUpdataDbContentByMegId(getActivity(),msgid);//发送请求
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> map = DataConnection.conDbAlarmContent(getActivity(),msgid);
		String title = map.get("title");
		String date1=sDateFormat.format(new Date(Long.parseLong(map.get("alarmtime"))));
		String jdName = map.get("jdname"); 
		String dpName = map.get("dpname");
		String content = map.get("content");
		String level = map.get("level");
		
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		bundle.putString("jdname", jdName);
		bundle.putString("dpname", dpName);
		bundle.putString("level", level);
		bundle.putString("alarmTime", date1);
		bundle.putString("content", content);
		// bundle.putSerializable("dp", dp);
		// bundle.putSerializable("dp",dp);
		// intent.putExtra("bundle", bundle);
		//intent.putExtras(bundle);
		//getActivity().startActivity(intent);
		mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.AlarmMainTop","com.snsoft.aiot.phone.fragment.AlarmContextFragment", bundle);
		
	}*/
	
	
	public class MyAdapter extends BaseAdapter{

		private ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
		private LayoutInflater inflater=null;
		private ArrayList<Boolean> isreads;
		private Context context;
		 private int mRightWidth = 0;
		public MyAdapter(ArrayList<HashMap<String, String>> list,Context context,int rightWidth){
			this.list=list;
			this.context=context;
			this.mRightWidth = rightWidth;
			isreads = new ArrayList<Boolean>();
			for(int i=0;i<list.size();i++){
				isreads.add(false);
			}
		}
		
		@Override
		public int getCount()
		{
			return list.size();
		}

		@Override
		public Object getItem(int position)
		{
			return list.get(position);
		}

		@Override
		public long getItemId(int position){
			return position;
		}
		
		/**
		 * 添加长度
		 * @param size
		 */
		public void setSize(int size){
			if(null== isreads)
				return;
			for(int i=0;i<size;i++){
				isreads.add(false);
			}
		}
		
		public void read(int position){
			isreads.set(position, true);
			notifyDataSetChanged();
		}
		
		public String getMsgid(int position){
			HashMap<String, String> map=list.get(position);
			return map.get("msgid");
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder=new ViewHolder();
			if(convertView == null){
				inflater=LayoutInflater.from(context);
				convertView=inflater.inflate(R.layout.alarm_list_item, null);
				holder.item_left = (RelativeLayout)convertView.findViewById(R.id.layout_alarm_itme01);
	            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
				holder.level = (ImageView) convertView.findViewById(R.id.iv_alarm_itme_level);
				holder.title = (TextView) convertView.findViewById(R.id.tv_alarm_item_title);
				holder.dp_name = (TextView) convertView.findViewById(R.id.tv_alarm_item_dp);
				holder.time = (TextView) convertView.findViewById(R.id.tv_alarm_item_time);
				//holder.state = (ImageView) convertView.findViewById(R.id.iv_alarm_item_readed);
				//holder.content = (TextView) convertView.findViewById(R.id.tv_alarm_item_content);
				holder.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
				holder.readState = (TextView) convertView.findViewById(R.id.tv_alarm_item_state);
				convertView.setTag(holder);
			}else{ 
				holder=(ViewHolder) convertView.getTag();
			}
			 LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
		                LayoutParams.MATCH_PARENT);
	        holder.item_left.setLayoutParams(lp1);
	        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
	        holder.item_right.setLayoutParams(lp2);
			HashMap<String, String> map=list.get(position);
			Log.i("map","map"+map);
	
			//String state = map.get("state");
			Log.i("level","level:"+map.get("level"));
			int level = Integer.parseInt(map.get("level"));
			holder.title.setText(map.get("title"));
			holder.dp_name.setText(map.get("dpname"));
			String date1=sDateFormat.format(new Date(Long.parseLong(map.get("alarmtime"))));
			holder.time.setText(date1);
			//holder.content.setText(map.get("text"));
			//报警级别
			if(level<3){
				holder.level.setBackgroundResource(R.drawable.alarm_warning_m);
			}else if(level<5){
				holder.level.setBackgroundResource(R.drawable.alarm_warning_l);
			}else{
				holder.level.setBackgroundResource(R.drawable.alarm_warning_h);
			}
			
			if(map.get("status").equals("noread")){
				holder.readState.setText("未读");
				holder.readState.setTextColor(Color.RED);
				holder.dp_name.setBackgroundColor(getResources().getColor(R.color.alarm_dpname_warning_bg));
			} else {
				holder.readState.setText("已读");
				holder.readState.setTextColor(Color.BLACK);
				holder.level.setBackgroundResource(R.drawable.alarm_normal);
				holder.dp_name.setBackgroundColor(getResources().getColor(R.color.alarm_dpname_bg));
			}
			
//			if(state.equals("1"))
//				isreads.set(position, true);
//			if(isreads.get(position))
//				holder.state.setBackgroundResource(R.drawable.is_read);
		    holder.item_right.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                if (mListener != null) {
	                    mListener.onRightItemClick(v, position);
	                }
	            }
	        });
			return convertView;
		}
		
		/**
	     * 单击事件监听器
	     */
	    private onRightItemClickListener mListener = null;
	    
	    public void setOnRightItemClickListener(onRightItemClickListener listener){
	    	mListener = listener;
	    }
		
	}
	public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }
  
	@Override  
	public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        try {  
            mListener = (OnArticleSelectedListener) activity;  
         } catch (ClassCastException e) {  
            throw new ClassCastException(activity.toString() + " must implementOnArticleSelectedListener");  
	  }  
	}
	
	private Handler handler = new Handler(){
		ArrayList<HashMap<String, String>> list;
		
		@Override
		public void handleMessage(Message msg) {
			
			// TODO Auto-generated method stub
			switch(msg.what){
				case LIST_ALL_NEW:
					arrayAll.clear();
					arrayAll.addAll((ArrayList<HashMap<String, String>>) msg.obj);
					listSizeAll = arrayAll.size();
					allAdapter.notifyDataSetChanged();
					listAll.onLoadNewCompleted();
					break;
				case LIST_ALL_MORE:
					list= (ArrayList<HashMap<String, String>>) msg.obj;
					arrayAll.addAll(list);
					allAdapter.notifyDataSetChanged();
					listSizeAll = arrayAll.size();
					listAll.onLoadMoreCompleted();
					break;
				case LIST_READED_NEW:
					arrayReaded.clear();
					arrayReaded.addAll((ArrayList<HashMap<String, String>>) msg.obj);
					listSizeReaded = arrayReaded.size();
					readedAdapter.notifyDataSetChanged();
					listReaded.onLoadNewCompleted();
					break;
				case LIST_READED_MORE:
					list = (ArrayList<HashMap<String, String>>) msg.obj;
					arrayReaded.addAll(list);
					readedAdapter.notifyDataSetChanged();
					listSizeReaded = arrayReaded.size();
					listReaded.onLoadMoreCompleted();
					break;
				case LIST_TODAY_NEW:
					arrayToday.clear();
					arrayToday.addAll((ArrayList<HashMap<String, String>>) msg.obj);
					listSizeToday = arrayToday.size();
					todayAdapter.notifyDataSetChanged();
					listToday.onLoadNewCompleted();
					break;
				case LIST_TODAY_MORE:
					list = (ArrayList<HashMap<String, String>>) msg.obj;
					arrayToday.addAll(list);
					todayAdapter.notifyDataSetChanged();
					listSizeToday = arrayReaded.size();
					listToday.onLoadMoreCompleted();
					break;
				default:
					break;
			}
		}
	};
	
	private class ViewHolder{
	   	RelativeLayout item_left;
    	RelativeLayout item_right;
		ImageView level;
		TextView title;
		TextView dp_name;
		TextView time;
		ImageView state;
		//TextView content;
		TextView item_right_txt;
		TextView readState;
	}
}
