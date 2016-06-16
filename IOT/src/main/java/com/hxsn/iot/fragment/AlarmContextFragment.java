package com.hxsn.iot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.activity.AbsFgtActivity;

public class AlarmContextFragment extends AbsBaseFgt {
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_alarm_content, container, false);
		initView();
		return view;
	}
	
	private void initView() {
		RelativeLayout ll = (RelativeLayout) view.findViewById(R.id.alarm_content_actionbar);
		Button backBtn = (Button) view.findViewById(R.id.alarm_content_back_btn);
		TextView tv_tit = (TextView) view.findViewById(R.id.tv_alarm_content_title);
		TextView tv_jid = (TextView) view.findViewById(R.id.tv_alarm_content_jidi);
		TextView tv_dap = (TextView) view.findViewById(R.id.tv_alarm_content_dapen);
//		TextView tv_nam = (TextView) view.findViewById(R.id.tv_alarm_content_user);
//		TextView tv_pho = (TextView) view.findViewById(R.id.tv_alarm_content_number);
		TextView tv_lev = (TextView) view.findViewById(R.id.tv_alarm_content_level);
//		TextView tv_sta = (TextView) view.findViewById(R.id.tv_alarm_content_state);
		TextView tv_tim = (TextView) view.findViewById(R.id.tv_alarm_content_time);
		TextView tv_con = (TextView) view.findViewById(R.id.tv_alarm_content_content);
		
		tv_tit.setText(getArguments().getString("title"));
		tv_jid.setText(getArguments().getString("jdname"));
		tv_dap.setText(getArguments().getString("dpname"));
//		tv_nam.setText(getArguments().getString("name"));
//		tv_pho.setText(getArguments().getString("phoneno"));
		tv_lev.setText(getArguments().getString("level"));
//		tv_sta.setText(getArguments().getString("state"));
		tv_tim.setText(getArguments().getString("alarmTime"));
		tv_con.setText(getArguments().getString("content"));
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((AbsFgtActivity)getActivity()).popFragment(0);
			}
		});
		
		if(Contents.getInstance().getIsSingleAlarm()){
			ll.setVisibility(View.GONE);
		} else {
			ll.setVisibility(View.VISIBLE);
		}
		
	}
}
