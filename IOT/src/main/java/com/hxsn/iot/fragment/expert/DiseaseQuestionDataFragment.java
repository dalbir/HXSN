package com.hxsn.iot.fragment.expert;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.HashMap;


public class DiseaseQuestionDataFragment extends AbsBaseFgt {
	private View view;
	private String backId;
	private HashMap<String,String> map;
	private OnArticleSelectedListener mListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.expert_question_data_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initData() {
		backId = getArguments().getString("backId");
		String id = getArguments().getString("id");
		map = DataController.getDiseaseQuestionContent(id);
		if(map != null){
			if(NetworkUtil.isErrorCode(map.get("code"), getActivity())){
				map = new HashMap<String,String>();
			} 
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			map = new HashMap<String,String>();
		}
	}
	
	private void initView() {
		Button back = (Button) view.findViewById(R.id.expert_search_back_btn);
		TextView title = (TextView) view.findViewById(R.id.expert_question_data_title);
		TextView reason = (TextView) view.findViewById(R.id.expert_question_data_reason);
		TextView method = (TextView) view.findViewById(R.id.expert_question_data_method);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Bundle bundle = new Bundle();
				//bundle.putString("id", backId);
				//mListener.onArticleSelectedBack("com.snsoft.aiot.phone.expert.DiseaseQuestionListFragment", bundle);
				((AbsFgtActivity)getActivity()).popFragment(3);
			}
		});
		
		title.setText(map.get("value"));
		reason.setText("    "+map.get("reason"));//加空格首行缩进
		method.setText("    "+map.get("method"));
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
