package com.hxsn.iot.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.iot.R;


public class CustomTextView extends LinearLayout{
	private ImageView imageview;
	private TextView tv;
	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sheds_custom_tv_layout,this);
		initView();
	}
	
	private void initView() {
		imageview = (ImageView)findViewById(R.id.imageView1);
		tv = (TextView) findViewById(R.id.textView1);
	}
	
	public void setText(String text) {
		tv.setText(text);
	}
	
	public void setBackgroundDrawable(Drawable background) {
		imageview.setBackgroundDrawable(background);
	}
	
	public void setOnClickListener(OnClickListener listener) {
		imageview.setOnClickListener(listener);
	}

}
