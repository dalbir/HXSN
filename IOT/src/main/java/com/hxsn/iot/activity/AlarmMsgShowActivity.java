package com.hxsn.iot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hxsn.iot.R;
import com.hxsn.iot.util.DataConnection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AlarmMsgShowActivity extends Activity {

	private static final String TAG = "AlarmMsgShowActivity.";
	private String id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_alarm_content);
		id = getIntent().getStringExtra("id");
		try {
			DataConnection.conUpdataDbContentByMegId(this, id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(TAG + "onCreate.id", id);
		initView();
	}
	
	private void initView(){
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.alarm_content_actionbar);
		Button goBackBtn = (Button)rl.findViewById(R.id.alarm_content_back_btn);
		goBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		TextView tv_tit = (TextView) findViewById(R.id.tv_alarm_content_title);
		TextView tv_jid = (TextView) findViewById(R.id.tv_alarm_content_jidi);
		TextView tv_dap = (TextView) findViewById(R.id.tv_alarm_content_dapen);
		TextView tv_lev = (TextView) findViewById(R.id.tv_alarm_content_level);
		TextView tv_tim = (TextView) findViewById(R.id.tv_alarm_content_time);
		TextView tv_con = (TextView) findViewById(R.id.tv_alarm_content_content);
		
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(id==null||"".equals(id)){
			return ;
		}
		HashMap<String, String> map = DataConnection.conDbAlarmContent(this, id);
		tv_tit.setText(map.get("title"));
		tv_tim.setText(sDateFormat.format(new Date(Long.parseLong(map.get("alarmtime")))));
		tv_jid.setText(map.get("jdname"));
		tv_dap.setText(map.get("dpname"));
		tv_con.setText(map.get("content"));
		tv_lev.setText(map.get("level"));
	}

}
