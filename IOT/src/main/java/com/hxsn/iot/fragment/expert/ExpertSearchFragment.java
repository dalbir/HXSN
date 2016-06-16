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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.DownloadImageTask;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.HashMap;
import java.util.List;


public class ExpertSearchFragment extends AbsBaseFgt {
	private View view;
	private Button backBtn;
	private ListView listview;
	private List<HashMap<String,String>> list;
	private LayoutInflater inflater;
	private OnArticleSelectedListener mListener;  
	private ParseDatas parse;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.expert_search_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initView() {
		backBtn = (Button) view.findViewById(R.id.expert_search_back_btn);
		listview = (ListView) view.findViewById(R.id.expert_search_listview);
		listview.setAdapter(new ListAdapter());
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("parseDatas", parse);
				mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.ExpertDetailFragment", bundle);
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
	
	private void initData() {
		parse = (ParseDatas) getArguments().getSerializable("parseDatas");
		list = parse.getList();
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
				convertView = inflater.inflate(R.layout.expert_search_listview_item,
						null);
			}
			ImageView imageView = (ImageView) convertView.findViewById(R.id.expert_search_item_img);
			DownloadImageTask task = new DownloadImageTask(getActivity(),imageView);
			task.execute(list.get(position).get("img"));
			
			TextView nameTv = (TextView) convertView.findViewById(R.id.expert_search_item_name);
			TextView typeTv = (TextView) convertView.findViewById(R.id.expert_search_item_type);
			nameTv.setText(list.get(position).get("name"));
			typeTv.setText(list.get(position).get("type"));
					
			return convertView;
		}
	}
}
