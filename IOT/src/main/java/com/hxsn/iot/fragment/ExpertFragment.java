package com.hxsn.iot.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpertFragment extends AbsBaseFgt implements OnClickListener{
	private View view;
	private OnArticleSelectedListener mListener; 
	private ArrayList<HashMap<String,String>> list;
	private LayoutInflater inflater;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.expert_main_layout, container, false);
		initData();
		initView();
		return view;
	}
	//热点信息错误判断
	private void initData() {
		ParseDatas parse = DataController.getHotQuestionList();
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
	//专家查询错误判断
	private ParseDatas initExpertList(){
		ParseDatas data = DataController.getExpertList();
		if(data != null){
			if(NetworkUtil.isErrorCode(data.getCode(), getActivity())){
				data = new ParseDatas();
			} 
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			data = new ParseDatas();
		}
		return data;
	}
	//问题列表错误判断
	private ArrayList<HashMap<String,String>> initQuestionList(){
		ParseDatas questionParse = DataController.getQuestionList();
		ArrayList<HashMap<String,String>> list = null;
		if(questionParse != null){
			if(NetworkUtil.isErrorCode(questionParse.getCode(), getActivity())){
				list = new ArrayList<HashMap<String,String>>();
			} else {
				list = questionParse.getList();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			list = new ArrayList<HashMap<String,String>>();
		}
		return list;
	}
	//在线专家错误判断
	private ParseDatas initOnlineList(){
		ParseDatas dataOnline = DataController.getExpertListOnLine();
		if(dataOnline != null){
			if(NetworkUtil.isErrorCode(dataOnline.getCode(), getActivity())){
				dataOnline = new ParseDatas();
			} 
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			dataOnline = new ParseDatas();
		}
		return dataOnline;
	}
	
	private void initView() {
		ImageView disSearch = (ImageView) view.findViewById(R.id.expert_main_disease_search);
		ImageView queryExpert = (ImageView) view.findViewById(R.id.expert_main_query_expert);
		ImageView question = (ImageView) view.findViewById(R.id.expert_main_question);
		ImageView onlineExpert = (ImageView) view.findViewById(R.id.expert_main_online_expert);
		ListView listview = (ListView) view.findViewById(R.id.expert_main_listview);
		disSearch.setOnClickListener(this);
		queryExpert.setOnClickListener(this);
		question.setOnClickListener(this);
		onlineExpert.setOnClickListener(this);
		
		listview.setAdapter(new ListAdapter());
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.HotQuestionDetailFragment", bundle);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.expert_main_disease_search:
			mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.DiseaseSearchFragment", null);
			break;
		case R.id.expert_main_query_expert:
			ParseDatas data = initExpertList();
			Bundle bundle = new Bundle();
			bundle.putSerializable("parseDatas", data);
			mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.ExpertSearchFragment", bundle);
			break;
		case R.id.expert_main_question:
			ArrayList<HashMap<String,String>> questionList = initQuestionList();
			ArrayList al = new ArrayList();
			al.add(questionList);
			Bundle questionBundle = new Bundle();
			questionBundle.putParcelableArrayList("questionList", al);
			mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.QuestionListFragment", questionBundle);
			break;
		case R.id.expert_main_online_expert:
			ParseDatas dataOnline = initOnlineList();
			Bundle bd = new Bundle();
			bd.putSerializable("parseDatas", dataOnline);
			mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.ExpertSearchFragment", bd);
			break;
		default:
			break;
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
 