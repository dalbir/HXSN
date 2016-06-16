package com.hxsn.iot.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
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

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.fragment.ShedsControlFragment;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.service.UpdateService;
import com.hxsn.iot.util.AbsData;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.HashMap;

public class AiotActivity extends AbsFgtActivity implements OnArticleSelectedListener {
	PopupWindow popup;
	@Override
	public int setBaseLayout(){
		return R.layout.layout_base;
	}
	
	//需要点击出现popupwindow怎修改对应位置为POPWINDOW
	@Override
	public String[] setMiddleFargmentNames(){
		return new String[]{ 
				"com.snsoft.aiot.phone.fragment.ParkMain_WEB", 
				"com.snsoft.aiot.phone.fragment.AlarmMsgFragment",
				"com.snsoft.aiot.phone.fragment.ExpertFragment", 
				POPWINDOW};
	}

	@Override
	public String[] setTopFargmentNames(){
		return new String[]{ 
				"com.snsoft.aiot.phone.fragment.ParkMainTop",
				"com.snsoft.aiot.phone.fragment.AlarmMainTop",
				"com.snsoft.aiot.phone.fragment.ExpertMainTop",
				"com.snsoft.aiot.phone.fragment.ExpertMainTop"};
	}
	
	/**
	 * 生成popwindow菜单
	 */
	@Override
	public void canOpenPoP(){
    	LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View view = layoutInflater.inflate(R.layout.popup_menu, null); 
    	ListView popupListView = (ListView) view.findViewById(R.id.popup_menu_listView);
		popupListView.setAdapter(new PopupAdapter());
		popupListView.setOnItemClickListener(new ItemClickListener());
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		popup = new PopupWindow(view, (int)(metrics.density*140),(int)(metrics.density*120));  
    	popup.setFocusable(true);  
		// popup.setOutsideTouchable(true);	  
	    // 这个是为了点击“返回Back”也能使其消失
	    popup.setBackgroundDrawable(new BitmapDrawable());
	    popup.showAsDropDown(findViewById(R.id.tab01_btn04),(int)(metrics.density*-50),0);
	    
	}
	
	private class PopupAdapter extends BaseAdapter{
		String[] names =getResources().getStringArray(R.array.menu01_text);
		int[] imgs = {R.drawable.popup_control,R.drawable.popup_setting};
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
			switch (arg2) {
			case 0:
				onChangeThisTabViews(getFragment("com.snsoft.aiot.phone.fragment.ControlMainTop"),getFragment("com.snsoft.aiot.phone.fragment.ShedsControlFragment"));
				break;
			case 1:
				onChangeThisTabViews(getFragment("com.snsoft.aiot.phone.fragment.ControlMainTop"),getFragment("com.snsoft.aiot.phone.fragment.SystemSettingFragment"));
				break;
			default:
				break;
			}
			popup.dismiss();
		}
    }
	
	@Override
	public void realizeSureBtn(AbsData absData) {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		((ShedsControlFragment) Contents.getInstance().getFragment()).getMoreSureBtn(absData.getData());
	}
	
	private AlertDialog.Builder alert;
	private AbsApplication myApplication;
	
	public void checkVersion() {
		myApplication = (AbsApplication)getApplication();
		final HashMap<String,String> map = DataController.getUpdateData(myApplication.getLocalVersion());
		//网络错误码判断
		if(map != null){
			if(NetworkUtil.isErrorCode(map.get("code"), this)){
				return;
			}
		} else {
			MessDialog.show1(getResources().getString(R.string.server_unusual_msg), this);
			return;
		}
		
		if (!map.get("code").equals("0")) {
			alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.update_software_title)
				.setMessage(R.string.update_software_message)
				.setPositiveButton(R.string.update_software_positive,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent updateIntent = new Intent(
										AiotActivity.this,
										UpdateService.class);
								updateIntent.putExtra(
										"app_name",
										getResources().getString(
												R.string.app_name));
								updateIntent.putExtra("url", map.get("url"));
								startService(updateIntent);
							}
						})
				.setNegativeButton(R.string.update_software_negative,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
			alert.create().show();
//			Message message = new Message();
//			message.what = 1;
//			handler.sendMessage(message);
		} 
	}
	
	
	//把fragment存入队列，用于返回键的调用
	@Override
	public void onArticleSelected(String topFragment,String fragment, Bundle bundle) {
		Fragment fm = getFragment(fragment);
		fm.setArguments(bundle);
		onChangeThisTabViewsEnBack(getFragment(topFragment),fm);
	}
	//用于返回按钮事件，返回后销毁上一个fragment
	@Override
	public void onArticleSelectedBack(String fragment, Bundle bundle) {
		Fragment fm = getFragment(fragment);
		fm.setArguments(bundle);
		onChangeThisTabViews(getFragment("com.snsoft.aiot.phone.fragment.ExpertMainTop"),fm);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			if(isOpenedDrawer()){
				return false;
			} else {
				if(isBackStackNumZero()) {
					AlertDialog isExit = new AlertDialog.Builder(this).create();  
		            isExit.setTitle("系统提示");  
		            isExit.setMessage("确定要退出吗");  
		            isExit.setButton("确定", listener);   
		            isExit.setButton2("取消", listener);  
		            isExit.show(); 
		            return false;
				}
			}
        }  
        return super.onKeyDown(keyCode, event);  
	}
	
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()  
    {  
        public void onClick(DialogInterface dialog, int which)  
        {  
            switch (which)  
            {  
            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序  
                finish();  
                break;  
            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框  
                break;  
            default:  
                break;  
            }  
        }  
    };
    
    @Override
	protected void onResume() {
    	setLockMode(true);
		super.onResume();
	}
}
