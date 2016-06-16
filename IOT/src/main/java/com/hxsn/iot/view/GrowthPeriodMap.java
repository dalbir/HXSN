package com.hxsn.iot.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.GrowthCycle;
import com.hxsn.iot.util.NetworkUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GrowthPeriodMap extends LinearLayout{
	
	private double[] days ;
	private int[] color = {Color.rgb(153, 108, 51),Color.rgb(253, 205, 53),
			Color.rgb(241, 91, 73),Color.rgb(56, 57, 56),Color.rgb(45, 158, 216),
			Color.rgb(230, 0, 18),Color.rgb(143, 195, 31),Color.rgb(149, 149, 149),
			Color.rgb(19, 181, 177)};
	private double total = 0;
	private double curDay ;
	double[] widArr;
	
	private RelativeLayout growthMap;
	private RelativeLayout growthCur;
	private Context context;
	private double screenWidth;
	private GrowthCycle growthData;
	private LayoutInflater inflater;
	private Timer timer = new Timer();
	private TimerTask task;
	private PopupWindow popup;
	
	public GrowthPeriodMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.growth_paint,this);
		if(initData()){
			initView();
			addGrowthMap();
		}
		
	}
	
	private boolean initData(){
		growthData = DataController.getGrowthCycle(Contents.getInstance().getId());
		
		if(growthData != null){
			if(NetworkUtil.isErrorCode(growthData.getCode(), context)){
				growthData = new GrowthCycle();
				return false;
			} 
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), context);
			growthData = new GrowthCycle();
			return false;
		}
		if(growthData.getCurrentDay()==null||growthData.getGroupList()==null||growthData.getTotalDays()==null){
			return false;
		}
		curDay = Double.valueOf(growthData.getCurrentDay()).doubleValue();
		List<HashMap<String,String>> list = growthData.getGroupList();
		days = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			days[i] = Double.valueOf(list.get(i).get("value")).doubleValue();
		}
		total = Double.valueOf(growthData.getTotalDays()).doubleValue();;
		WindowManager manager = ((Activity)context).getWindowManager();
		Display display = manager.getDefaultDisplay();
		screenWidth = display.getWidth()-50; //全屏宽度减去50
        widArr = new double[days.length];
        for (int i = 0; i < days.length; i++) {
        	widArr[i] = days[i]/total*screenWidth;
		}
        return true;
	}
	//得到当前天数的位置
	private int searchIndex(double currentDayDou){
        double cur = 0;
        double next = 0;
        for (int i = 0; i < days.length; i++) {
        	if(i != 0){
        		cur = cur + days[i-1];
            	next = cur + days[i];
    			if(cur < currentDayDou && currentDayDou <= next){
    				return i;
    			}
        	}
		}
        return 0;
	}
	
	private String getGrowthPeriodName(int position){
		return growthData.getGroupList().get(position).get("name");
	}
	
	private double getStartWidth(double currentDayDou){
		double widthL = 0;
		
		for (int i = 0; i < searchIndex(curDay); i++) {
			widthL += days[i];
		}
		return (currentDayDou-widthL)/total*screenWidth;
	}
	
	private void initView(){
		growthMap = (RelativeLayout) findViewById(R.id.growth_period_map);
		growthCur = (RelativeLayout) findViewById(R.id.growth_period_cur);
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			popup.dismiss();
			super.handleMessage(msg);
		}
	};
	
	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
	
	//主要实现增加进度条显示，原理是先在绘制底层，在在上层绘制当前天数层
	private void addGrowthMap() {
		int index = searchIndex(curDay)+1;
		for (int i = 0; i < widArr.length; i++) {
			TextView colorTv = new TextView(context);
			colorTv.setId(i+1);//100以内的数字为底层ID调用
			colorTv.setBackgroundColor(Color.GRAY);
			final int a = i;
			colorTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					View view = inflater.inflate(R.layout.popup_growth_layout, null); 
					TextView tv = (TextView) view.findViewById(R.id.popup_growth_textview);
					tv.setText(getGrowthPeriodName(a));
					if(popup != null && popup.isShowing()){
						popup.dismiss();
					}
					popup = new PopupWindow(view,80,50);
					popup.showAsDropDown(v,0,-60);
					timer.schedule(new MyTimerTask(), 1000);
					//Toast.makeText(getContext(), getGrowthPeriodName(a), Toast.LENGTH_SHORT).show();
				}
			});
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT); 
			int width = Integer.parseInt(new java.text.DecimalFormat("0").format(widArr[i]));
			params.width = width;
			params.height = 20;
			params.addRule(RelativeLayout.RIGHT_OF, i);
			growthMap.addView(colorTv,params);
			
			//增加第三层分割线
			View view = new View(context);
			view.setBackgroundColor(Color.WHITE);
			RelativeLayout.LayoutParams paramsCut = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT); 
			paramsCut.width = 1;
			paramsCut.height = 20;
			paramsCut.addRule(RelativeLayout.ALIGN_RIGHT,i+1);
			growthMap.addView(view,paramsCut);
			
			//增加标签显示
			TextView labelTv = new TextView(context);
			labelTv.setText((int)days[i]+"天");
			labelTv.setTextSize(10);
			labelTv.setTextColor(Color.WHITE);
			labelTv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
			labelTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.growth_period_label_bg));
			RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			paramsLabel.addRule(RelativeLayout.ALIGN_RIGHT,i+1);
			paramsLabel.addRule(RelativeLayout.BELOW,i+1);
			growthMap.addView(labelTv,paramsLabel);
		}
		for (int i = 0; i < index; i++) {
			TextView colorTv = new TextView(context);
			colorTv.setId(i+100); //id加上100是与底下一层id区分开
			colorTv.setBackgroundColor(color[i]);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT); 
			int width = Integer.parseInt(new java.text.DecimalFormat("0").format(widArr[i]));
			params.width = width;
			params.height = 20;
			if (i == (index-1)) {
				
				int marLeft = Integer.parseInt(new java.text.DecimalFormat("0").format(getStartWidth(curDay)));
				params.width = marLeft;
				params.addRule(RelativeLayout.RIGHT_OF,i+100-1);
				
			} else if(i == 0){
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			} else {
				params.addRule(RelativeLayout.RIGHT_OF,i+100-1);
			}
			growthMap.addView(colorTv,params);
			
		}
		//增加当前天数显示
		TextView label = new TextView(context);
		label.setText("第"+(int)curDay+"天/"+getGrowthPeriodName(searchIndex(curDay)));
		label.setTextSize(10);
		label.setTextColor(Color.WHITE);
		label.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		label.setBackgroundDrawable(getResources().getDrawable(R.drawable.growth_period_label_up_bg));
		RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsLabel.height = 30;
		double widthMar = 0;
		for (int i = 0; i < index-1; i++) {
			widthMar += days[i];
		}
		int marLeft = Integer.parseInt(new java.text.DecimalFormat("0").format(getStartWidth(curDay)+widthMar/total*screenWidth));
		paramsLabel.leftMargin = marLeft;
		growthCur.addView(label,paramsLabel);
	}
	
	private void showPopupWindow(String str,View v){
		
	}
	
	private int randomColor(){
		Random random = new Random();
		int r = random.nextInt(256);
        int g= random.nextInt(256);
        int b = random.nextInt(256);
        int mColor = Color.rgb(r, g, b);
        return mColor;
	}
	
}
