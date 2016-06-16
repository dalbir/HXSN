package com.hxsn.iot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.GrowthCycle;


public class GrowthPeriodView extends LinearLayout{
	
	private TextView growthName;

	public GrowthPeriodView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.growth_period_layout,this);
		initView();
	}
	
	private void initView(){
		growthName = (TextView) findViewById(R.id.growth_name);
		GrowthCycle data = DataController.getGrowthCycle(Contents.getInstance().getId());
		growthName.setText(data.getName());
	}
}
