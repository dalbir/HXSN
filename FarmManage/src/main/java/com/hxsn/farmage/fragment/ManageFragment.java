package com.hxsn.farmage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.hxsn.farmage.R;
import com.hxsn.farmage.activity.ManageWebActivity;
import com.hxsn.farmage.base.BaseFrgament;

public class ManageFragment  extends BaseFrgament {
	

	TableRow tableRow1,tableRow2,tableRow3,tableRow4;
	Intent intent=null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_guanli_fragment,
				container, false);
        initView(view);
        initListener();
		return view;
	}

	

	private void initView(View view)
	{
		// TODO Auto-generated method stub
	    tableRow1=(TableRow)view.findViewById(R.id.tableRow1);
		tableRow2=(TableRow)view.findViewById(R.id.tableRow2);
		tableRow3=(TableRow)view.findViewById(R.id.tableRow3);
		tableRow4=(TableRow)view.findViewById(R.id.tableRow4);
		intent=new Intent(getActivity(),ManageWebActivity.class);
		
	}

	private void initListener()
	{
		tableRow1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
			    intent.putExtra("title", "农产品状态");
			    intent.putExtra("wtag", 1);
				startActivity(intent);
			}
		});
		
		tableRow2.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent.putExtra("title", "农事作业");
			    intent.putExtra("wtag", 2);
				startActivity(intent);
			}
		});
		
		tableRow3.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				intent.putExtra("title", "门禁监控");
			    intent.putExtra("wtag", 3);
				startActivity(intent);
			}
		});
		
		tableRow4.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				intent.putExtra("title", "安防报警");
			    intent.putExtra("wtag", 4);
				startActivity(intent);
			}
		});
		
	}
	
	
}
