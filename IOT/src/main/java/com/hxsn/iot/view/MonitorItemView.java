package com.hxsn.iot.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.activity.AiotBActivity;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.data.SensorScreen;
import com.hxsn.iot.model.Sensor;
import com.hxsn.iot.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;


public class MonitorItemView extends LinearLayout implements OnClickListener{
	
	private Context context;
	private Button mRefreshBtn;
	private Button mAddBtn;
	private TextView mDateTv;
	private LinearLayout mContentLayout;
	private HorizontalScrollView scrollView;
	private Button leftBtn;
	private Button rightBtn;
	private String dyId;
	private Animation animation;
	
	public MonitorItemView(Context context, AttributeSet attrs,String dyId) {
		super(context, attrs);
		this.context = context;
		this.dyId = dyId;
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.monitor_item_view,this);
		initViews();
//		Timer timer = new Timer();
//		timer.schedule(task, 360000, 360000);
	}
	
	private void initViews() {
		mRefreshBtn = (Button)findViewById(R.id.monitor_refresh_btn);
		
		animation = AnimationUtils.loadAnimation(context, R.anim.refresh);
        animation.setRepeatMode(Animation.RESTART);
		
		mAddBtn = (Button) findViewById(R.id.monitor_add_btn);
		mDateTv = (TextView) findViewById(R.id.monitor_date_tv);
		mContentLayout = (LinearLayout) findViewById(R.id.monitor_content_layout);
		scrollView = (HorizontalScrollView) findViewById(R.id.monitor_horizontal_scrollview);
		leftBtn = (Button) findViewById(R.id.monitor_content_left_btn);
		rightBtn = (Button) findViewById(R.id.monitor_content_right_btn);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		mRefreshBtn.setOnClickListener(this);
		mAddBtn.setOnClickListener(this);
		mDateTv.setOnClickListener(this);
		mDateTv.setText(getDateTime());
		setContentValue();
	}
	
	private void setContentValue() {
		if (mContentLayout.getChildCount() != 0) {
			mContentLayout.removeAllViews();
		} 
		for (int i = 0; i < getSensor().size(); i++) {
			MonitorContentItem mItem = new MonitorContentItem(context,null);
			setValue(getSensor().get(i),mItem);
			//mItem.setModuleValue(R.drawable.bottom1_1, getSensor().get(i).getValue());
			mContentLayout.addView(mItem);
		}
	}
	
	private void setValue(Sensor sensor,MonitorContentItem mItem){
		String type =sensor.getType();
		if("QW".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_default, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("QS".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_default, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("GZ".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_gz, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("CO2".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_co2, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("TS".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_default, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("TW".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_trwd, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("FS".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_default, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("FX".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_default, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		} else if("SF".equals(type)){
			mItem.setModuleValue(R.drawable.jiance_img_trsf, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		}else{
			mItem.setModuleValue(R.drawable.jiance_img_default, sensor.getValue()+sensor.getUnit() ,sensor.getNormal(), sensor.getName());
		}
	}
	
	private SensorScreen getErrorCode(ParseDatas parse){
    	SensorScreen sensorScreen = null;
    	if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), context)){
				sensorScreen = new SensorScreen();
			} else {
				sensorScreen = (SensorScreen)parse.getObject();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), context);
			sensorScreen = new SensorScreen();
		}
    	return sensorScreen;
    }
	
	private String getDateTime() {
		ParseDatas parse = DataController.getDapeData(dyId);
		SensorScreen sensorScreen = getErrorCode(parse);
		String date = "";
		if(sensorScreen!=null){
			date = NetworkUtil.convertTimeFormat(sensorScreen.getDate());
		}
		return date;
	}
	
	private List<Sensor> getSensor() {
		ParseDatas parse = DataController.getDapeData(dyId);
		SensorScreen sensorScreen = getErrorCode(parse);
		List<Sensor> sensor = null;
		if(sensorScreen!=null){
			sensor = sensorScreen.getList();
		} else {
			sensor = new ArrayList<Sensor>();
		}
		
		return sensor;
	}
	
	TimerTask task = new TimerTask(){

		@Override
		public void run() {
			Message message = new Message();
			handler.sendMessage(message);
		}
		
	};
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			refreshData();
			super.handleMessage(msg);
		}
	};
	
	private void refreshData(){
		getSensor();
		setContentValue();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.monitor_refresh_btn:
			mRefreshBtn.startAnimation(animation);
			refreshData();
			break;
		case R.id.monitor_add_btn:
			((AiotBActivity) context).openOrCloseMore();
			break;
		case R.id.monitor_date_tv:
			//showCalendar();
			break;
		case R.id.monitor_content_left_btn:
			scrollView.arrowScroll(View.FOCUS_LEFT);
			break;
		case R.id.monitor_content_right_btn:
			scrollView.arrowScroll(View.FOCUS_RIGHT);
			break;
		default:
			break;
		}
	}
	
	private void showCalendar() {
		Calendar c = Calendar.getInstance();
		Dialog dialog = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mDateTv.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			}
		},c.get(Calendar.YEAR), // 传入年份
        c.get(Calendar.MONTH), // 传入月份
        c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
		dialog.show();
	}
	
}
