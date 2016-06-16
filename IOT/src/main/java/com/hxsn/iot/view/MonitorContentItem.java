package com.hxsn.iot.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.iot.R;


public class MonitorContentItem extends LinearLayout{
	private ImageView mImageView;
	private TextView mTextView;
	private TextView mTvName;
	
	public MonitorContentItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.monitor_content_item,this);
		initViews();
	}
	
	private void initViews() {
		mTvName = (TextView) findViewById(R.id.monitor_content_item_name);
		mImageView = (ImageView) findViewById(R.id.monitor_content_item_imageview);
		mTextView = (TextView) findViewById(R.id.monitor_content_item_tv);
	}
	
	public void setModuleValue(int id, String str ,String normal, String name) {
		mImageView.setImageResource(id);
		
		mTextView.setText(str);
		TextPaint tp = mTextView.getPaint();//字体加粗
		tp.setFakeBoldText(true); 
		
		mTvName.setText(name);
		if(normal.equals("0")){
			mTextView.setTextColor(Color.RED);
		} else {
			mTextView.setTextColor(Color.GREEN);
		}
	}

}
