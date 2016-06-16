package com.hxsn.iot.fragment.expert;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;


public class DiseaseSearchFragment extends AbsBaseFgt {
	private View view;
	private ArrayList<HashMap<String,String>> list;
	private LayoutInflater inflater;
	private OnArticleSelectedListener mListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.expert_disease_product_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initData() {
	    ParseDatas parse = DataController.getProductList();
	    if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), getActivity())){
				list = new ArrayList<HashMap<String,String>>();
			} else {
				list = parse.getList();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			list = new ArrayList<HashMap<String,String>>();
		}
	}
	
	private void initView() {
		Button backBtn = (Button) view.findViewById(R.id.expert_search_back_btn);
		TextView title = (TextView) view.findViewById(R.id.expert_disease_product_title);
		TextView navOne = (TextView) view.findViewById(R.id.expert_disease_product_one);
		TextView navTwo = (TextView) view.findViewById(R.id.expert_disease_product_two);
		TextView navThree = (TextView) view.findViewById(R.id.expert_disease_product_three);
		ListView listview = (ListView) view.findViewById(R.id.expert_disease_product_listview);
		
		title.setText("农产品列表");
		navOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.expert_disease_navigation_blue));
		navTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.expert_disease_navigation_gray));
		navThree.setBackgroundDrawable(getResources().getDrawable(R.drawable.expert_disease_navigation_gray));
		
		listview.setAdapter(new ListAdapter());
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putString("id", list.get(position).get("id"));
				mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.DiseaseListFragment", bundle);
			}
		});
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//mListener.onArticleSelectedBack("com.snsoft.aiot.phone.fragment.ExpertFragment", null);
				((AbsFgtActivity)getActivity()).popFragment(0);
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
	
	private class ListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.expert_disease_list_item,
						null);
			}
			
			TextView tv = (TextView) convertView.findViewById(R.id.expert_disease_list_item_tv);
			tv.setText(list.get(position).get("value"));
			
			return convertView;
		}
	}
}
