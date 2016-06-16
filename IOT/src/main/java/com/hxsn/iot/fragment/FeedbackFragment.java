package com.hxsn.iot.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.HashMap;


public class FeedbackFragment extends AbsBaseFgt {
	private View view;
	private EditText contentEt;
	private EditText contactEt;
	private Button submit;
	private Button back;
	private OnArticleSelectedListener mListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.settings_feedback_layout, container, false);
		initView();
		return view;
	}
	
	private void initView() {
		back = (Button) view.findViewById(R.id.settings_back_btn);
		contentEt = (EditText) view.findViewById(R.id.settings_feedback_content);
		contactEt = (EditText) view.findViewById(R.id.settings_feedback_contact);
		submit = (Button) view.findViewById(R.id.settings_feedback_btn);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.SystemSettingFragment", null);
				((AbsFgtActivity)getActivity()).popFragment(0);
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				String content = contentEt.getText().toString();
				String contact = contactEt.getText().toString();
				if(content.equals("")){
					Toast.makeText(getActivity(), "请输入意见再提交", Toast.LENGTH_SHORT).show();
					return;
				}
				if(contact.equals("")){
					Toast.makeText(getActivity(), "请输入联系方式再提交", Toast.LENGTH_SHORT).show();
					return;
				}
				HashMap<String,String> map = DataController.getFeedback(content, contact);
				if(map != null){
					if(!NetworkUtil.isErrorCode(map.get("code"), getActivity())){
						Toast.makeText(getActivity(), "提交成功", Toast.LENGTH_SHORT).show();
					} 
				} else {
					MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
				}
			}
		});
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
