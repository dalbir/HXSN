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
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;


public class QuestionListFragment extends AbsBaseFgt {
	private View view;
	private Button backBtn;
	private ListView listview;
	private LayoutInflater inflater;
	private OnArticleSelectedListener mListener;  
	private ArrayList<HashMap<String,String>> list;
	private ArrayList al;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.expert_question_list_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initView() {
		backBtn = (Button) view.findViewById(R.id.expert_question_back_btn);
		listview = (ListView) view.findViewById(R.id.expert_question_listview);
		listview.setAdapter(new ListAdapter());
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putParcelableArrayList("questionList", al);
				mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.QuestionDetailFragment", bundle);
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
		al = getArguments().getParcelableArrayList("questionList");
		list = (ArrayList<HashMap<String,String>>)al.get(0);
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
				convertView = inflater.inflate(R.layout.expert_question_list_item_layout,
						null);
			}
			
			TextView name = (TextView) convertView.findViewById(R.id.expert_question_item_name);
			TextView content = (TextView) convertView.findViewById(R.id.expert_question_item_content);
			TextView time = (TextView) convertView.findViewById(R.id.expert_question_item_time);
			TextView answer = (TextView) convertView.findViewById(R.id.expert_question_item_answer);
			
			name.setText(list.get(position).get("name"));
			content.setText(list.get(position).get("value"));
			time.setText(list.get(position).get("time"));
			answer.setText(list.get(position).get("num")+"人回答");
					
			return convertView;
		}
	}
	
}
