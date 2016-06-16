package com.hxsn.iot.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.R;
import com.hxsn.iot.activity.AboutUsActivity;
import com.hxsn.iot.activity.LoginActivity;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.util.DataConnection;
import com.hxsn.iot.util.OnArticleSelectedListener;


public class SystemSettingFragment extends AbsBaseFgt implements OnClickListener{
	private View view;
	private TextView alarmEmpty;
	private TextView reControlPwd;
	private CheckBox reControlPwdCb;
	private TextView modControlPwd;
	private TextView modUserPwd;
	private TextView changeUser;
	private TextView about;
	private TextView update;
	private TextView feedback;
	private TextView exit;
	
	private OnArticleSelectedListener mListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.settings_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initData() {
		SharedPreferences preference = getActivity().getSharedPreferences("data", 0);
		if(preference.getString("controlPwd", "").equals("")){
			Contents.getInstance().setEmpty(true);
		} else {
			Contents.getInstance().setEmpty(false);
		}
	}
	
	private void initView() {
		alarmEmpty = (TextView) view.findViewById(R.id.settings_alarm_empty);
		reControlPwd = (TextView) view.findViewById(R.id.settings_remember_control_pwd);
		reControlPwdCb = (CheckBox) view.findViewById(R.id.settings_remember_control_pwd_cb);
		modControlPwd = (TextView) view.findViewById(R.id.settings_modify_control_pwd);
		modUserPwd = (TextView) view.findViewById(R.id.settings_modify_user_pwd);
		changeUser = (TextView) view.findViewById(R.id.settings_change_user);
		about = (TextView) view.findViewById(R.id.settings_about);
		update = (TextView) view.findViewById(R.id.settings_monitor_update);
		feedback = (TextView) view.findViewById(R.id.settings_feedback);
		exit = (TextView) view.findViewById(R.id.settings_exit);
		
//		if(Contents.getInstance().getEmpty()){
//			reControlPwdCb.setEnabled(false);
//			modControlPwd.setText("  创建控制密码");
//		} else {
//			reControlPwdCb.setEnabled(true);
//			modControlPwd.setText("  修改控制密码");
//		}
		
		alarmEmpty.setOnClickListener(this);
		modControlPwd.setOnClickListener(this);
		modUserPwd.setOnClickListener(this);
		changeUser.setOnClickListener(this);
		about.setOnClickListener(this);
		update.setOnClickListener(this);
		feedback.setOnClickListener(this);
		exit.setOnClickListener(this);
		reControlPwdCb.setChecked(Contents.getInstance().getIsChecked());
		reControlPwdCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Contents.getInstance().setIsChecked(isChecked);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settings_alarm_empty:
			deleteAllDialog();
			break;
		case R.id.settings_modify_control_pwd:
			mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.setting.ControlPwdModifyFragment", null);
			break;
		case R.id.settings_change_user:
			logoutDialog();
			break;
		case R.id.settings_about:
			Intent intent = new Intent(getActivity(), AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.settings_monitor_update:
//			((AiotActivity)getActivity()).checkVersion();
			Toast.makeText(getActivity(), "当前为最新版本", Toast.LENGTH_LONG).show();
			break;
		case R.id.settings_modify_user_pwd:
			mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.fragment.ModifyPassword", null);
			break;
		case R.id.settings_feedback:
			mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.setting.FeedbackFragment", null);
			break;
		case R.id.settings_exit:
			getActivity().finish();
			break;
			
		default:
			break;
		}
	}
	
	private void deleteAllDialog() {
		
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
    	builder.setTitle(R.string.deleteall).
    		setMessage("删除全部报警信息？").
    		setPositiveButton("确定", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialoginterface, int i){
					deleteAll();
				}
			}).
			setNegativeButton("取消", null).show();
	}
	
	private void deleteAll(){
		
		DataConnection dc = new DataConnection();
		dc.deleteAlarmTable(getActivity());
		
	}
	
	private void logoutDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
    	builder.setTitle(R.string.app_name).
    		setMessage("是否注销用户").
    		setPositiveButton("确定", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialoginterface, int i){
					logout();
				}
			}).
			setNegativeButton("取消", null).show();
	}
	
	private void logout(){
		SharedPreferences sharedata = getActivity().getSharedPreferences("data", 0);//获得SharedPreferences对象  
		String name = sharedata.getString("username", null);
		String passward = sharedata.getString("pwd", null);
		if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(passward)){
	     
			
			Editor esharedata = getActivity().getSharedPreferences("data", 0).edit();
			esharedata.putString("username", null);
			esharedata.putString("pwd", null);
			esharedata.putString("sessionid", null);
			esharedata.putString("controlPwd", null);
			esharedata.commit();
		}
		getActivity().finish();
		Intent it=new Intent(getActivity(), LoginActivity.class);
		startActivity(it);
		
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
}
