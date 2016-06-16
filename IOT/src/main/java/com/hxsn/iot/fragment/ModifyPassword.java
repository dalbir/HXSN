package com.hxsn.iot.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.HashMap;


public class ModifyPassword extends AbsBaseFgt implements OnClickListener {
	private static final String TAG = "ModifyPassword";
	private View view;
	private TextView oldTv;
	private EditText oldEt;
	private EditText newEt;
	private EditText TNewEt;
	private Button okBtn;
	private Button backBtn;
	
	private boolean empty = false;
	private SharedPreferences settings;
	private Editor editor;
	
	private OnArticleSelectedListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.settings_modify_user_pwd, container, false);
		initData();
		initView();		
		return view;
	}
	
	private void initView() {
		oldTv = (TextView) view.findViewById(R.id.settings_modify_control_text);
		oldEt = (EditText) view.findViewById(R.id.settings_modify_control_pwd_old);
		newEt = (EditText) view.findViewById(R.id.settings_modify_control_pwd_new);
		TNewEt = (EditText) view.findViewById(R.id.settings_modify_control_pwd_new_too);
		okBtn = (Button) view.findViewById(R.id.settings_modify_control_pwd_btn);
		backBtn = (Button) view.findViewById(R.id.settings_back_btn);
		
		if(empty) {
			oldEt.setVisibility(View.INVISIBLE);
			oldTv.setVisibility(View.INVISIBLE);
		}
		
		backBtn.setOnClickListener(this);
		okBtn.setOnClickListener(this);
		
	}
	
	private void initData() {
		settings = getActivity().getSharedPreferences("data", 0);
		editor = settings.edit();
	}
	
	private void sureBtnMethod() {
		String oldStr = oldEt.getText().toString();
		String newStr = newEt.getText().toString();
		String TNewStr = TNewEt.getText().toString();
		if(!empty){
			if(oldStr.equals("")||oldStr==null){
				Toast.makeText(getActivity(), "初始密码为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!oldStr.equals(settings.getString("pwd", null))){
				Toast.makeText(getActivity(), "初始密码不正确", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if(newStr.equals("")||newStr==null){
			Toast.makeText(getActivity(), "新密码为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(!TNewStr.equals(newStr)){
			Toast.makeText(getActivity(), "两次密码不同", Toast.LENGTH_SHORT).show();
			return;
		}
		HashMap<String, String> map = DataController.getResetPwd(settings.getString("username", null), settings.getString("pwd", null), TNewStr, settings.getString("IMEI", null));
		if(map != null){
			if(!NetworkUtil.isErrorCode(map.get("code"), getActivity())){
				editor.putString("pwd", TNewStr);
				editor.commit();
				Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
			} 
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
		}
		oldEt.setText("");
		newEt.setText("");
		TNewEt.setText("");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settings_back_btn:
			//mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.SystemSettingFragment", null);
			((AbsFgtActivity)getActivity()).popFragment(0);
			break;
		case R.id.settings_modify_control_pwd_btn:
			sureBtnMethod();
			break;
		default:
			break;
		}
	}

	public String getPhoneID(){
		//获取手机唯一标识，存到数据库
		TelephonyManager TelephonyMgr = (TelephonyManager)getActivity().getSystemService(getActivity().TELEPHONY_SERVICE); 
		String szImei = TelephonyMgr.getDeviceId(); 
		//Log.i("IMEI","The IMEI:"+szImei);
		return szImei;
	}

}
