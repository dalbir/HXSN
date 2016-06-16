package com.hxsn.iot.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.fragment.MonitorDataFragment;
import com.hxsn.iot.util.AbsData;
import com.hxsn.iot.util.OnArticleSelectedListener;


public class AiotBActivity extends AbsFgtActivity implements OnArticleSelectedListener {

	public PopupWindow popup;
	
	@Override
	public int setBaseLayout(){
		return R.layout.layout_baseb;
	}

	@Override
	public String[] setMiddleFargmentNames(){
		return new String[]{
				"com.snsoft.aiot.phone.fragment.MonitorDataFragment",
				"com.snsoft.aiot.phone.fragment.ControlJSFragment",
				"com.snsoft.aiot.phone.fragment.FarmworkFragment",
				POPWINDOW
		};
	}

	@Override
	public String[] setTopFargmentNames(){
		return new String[]{ 
				"com.snsoft.aiot.phone.fragment.MonitorMainTop",
				"com.snsoft.aiot.phone.fragment.ControlMainTop",
				"com.snsoft.aiot.phone.fragment.FarmMainTop",
				"com.snsoft.aiot.phone.fragment.FarmMainTop"
		};
	}
	
	@Override
	public void canOpenPoP(){
    	LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View view = layoutInflater.inflate(R.layout.popup_menu, null); 
    	ListView popupListView = (ListView) view.findViewById(R.id.popup_menu_listView);
		popupListView.setAdapter(new PopupAdapter());
		popupListView.setOnItemClickListener(new ItemClickListener());
		popupListView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		popup = new PopupWindow(view, (int)( 140 * metrics.density),(int)(180 * metrics.density));  
    	popup.setFocusable(true);  
	    //popup.setOutsideTouchable(true);  	  
	    // 这个是为了点击“返回Back”也能使其消失
	    popup.setBackgroundDrawable(new BitmapDrawable());
	    popup.showAsDropDown(findViewById(R.id.tab02_btn04),(int)(metrics.density*-50),0);
	}
	
	private class PopupAdapter extends BaseAdapter{
		String[] names = getResources().getStringArray(R.array.menu02_text);
		int[] imgs = {R.drawable.popup_line,R.drawable.popup_alarm,R.drawable.popup_video};
		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return names[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getLayoutInflater().inflate(R.layout.popup_item_layout, null);
			}
			ImageView image = (ImageView) convertView.findViewById(R.id.popup_window_item_img);
			TextView tv = (TextView) convertView.findViewById(R.id.popup_window_item_tv);
			
			image.setBackgroundResource(imgs[position]);
			tv.setText(names[position]);
			return convertView;
		}
		
	}
	
	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int arg2,
				long arg3) {
			String itemName = ((TextView)view.findViewById(R.id.popup_window_item_tv)).getText().toString();
			
		    if("报警信息".equals(itemName)){
		    	onChangeThisTabViews(getFragment("com.snsoft.aiot.phone.fragment.AlarmMainTop"),getFragment("com.snsoft.aiot.phone.fragment.AlarmMsgDPFragment"));
		    } else if("今日曲线".equals(itemName)){
		    	Intent intent = new Intent(getApplication(),CurveActivity.class);
		    	startActivity(intent);
		    }else if("视频监控".equals(itemName)){
		    	onChangeThisTabViews(getFragment("com.snsoft.aiot.phone.fragment.VideoSurveillanceTop"),getFragment("com.snsoft.aiot.phone.fragment.VideoSurveillanceFragment"));
		    }
		    popup.dismiss();
		}
    }
	//多选确定按钮事件
	@Override
	public void realizeSureBtn(AbsData absData) {
		switch (absData.getType()) {
		case AbsData.MONITOR_COMPARE:
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			((MonitorDataFragment)getFragment("com.snsoft.aiot.phone.fragment.MonitorDataFragment")).showMonitorCompare(layoutInflater,this,absData.getData());
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		setLockMode(false);
		super.onResume();
	}

	@Override
	public void onArticleSelected(String fragmentTop,String fragment, Bundle bundle) {
		Fragment fm = getFragment(fragment);
		fm.setArguments(bundle);
		onChangeThisTabViewsEnBack(getFragment(fragmentTop),fm);
	}

	@Override
	public void onArticleSelectedBack(String fragment, Bundle bundle) {
		// TODO Auto-generated method stub
		
	}
}
