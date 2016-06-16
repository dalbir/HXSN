package com.hxsn.iot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.iot.R;


public class GroupView extends LinearLayout {

	private int groupPosition;
	
	private OnGroupClickListener listener;
	
	private CheckBox selectGroup;
	
	private TextView groupName;
	
	public GroupView(OnGroupClickListener listener, Context context) {
		this(listener, context, null);
	}
	public GroupView(OnGroupClickListener listener, Context context, AttributeSet attrs) {
		this(listener, context, attrs, 0);
	}
	public GroupView(OnGroupClickListener listener, Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.listener = listener;
		intViews();
	}

	public void intViews(){
		final LayoutInflater mLayoutInflater = LayoutInflater.from(getContext());
		View v = mLayoutInflater.inflate(R.layout.drawer_group, null, false);
		addView(v);
		
		selectGroup = (CheckBox)v.findViewById(R.id.checkbox_select_group);
		groupName = (TextView)v.findViewById(R.id.textview_group_name);
		
		selectGroup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(selectGroup.isChecked()){
					listener.onGroupChecked(groupPosition);
				}else{
					listener.onGroupUnChecked(groupPosition);
				}
			}
		});
	}
	
	public interface OnGroupClickListener{
		public void onGroupChecked(int groupPosition);
		public void onGroupUnChecked(int groupPosition);
	}
	
	public int getGroupPosition() {
		return groupPosition;
	}
	public void setGroupPosition(int groupPosition) {
		this.groupPosition = groupPosition;
	}
	public CheckBox getSelectGroup() {
		return selectGroup;
	}
	public void setSelectGroup(CheckBox selectGroup) {
		this.selectGroup = selectGroup;
	}
	public TextView getGroupName() {
		return groupName;
	}
	public void setGroupName(TextView groupName) {
		this.groupName = groupName;
	}
	
}
