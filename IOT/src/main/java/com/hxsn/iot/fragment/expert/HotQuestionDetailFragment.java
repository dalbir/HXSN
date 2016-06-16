package com.hxsn.iot.fragment.expert;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.model.DetailQuestion;
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.DownloadImageTask;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;
import com.hxsn.iot.view.InnerListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HotQuestionDetailFragment extends AbsBaseFgt {
	private View view;
	private TextView nameTv;
	private TextView contentTv;
	private LinearLayout imgGroup;
	private InnerListView listView;
	private OnArticleSelectedListener mListener; 
	private int position;
	private DetailQuestion detail;
	private List<HashMap<String,String>> list;
	private LayoutInflater inflater;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.expert_question_detail_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initView() {
		Button back = (Button) view.findViewById(R.id.expert_question_back_btn);
		nameTv = (TextView) view.findViewById(R.id.question_detail_name);
		contentTv = (TextView) view.findViewById(R.id.question_detail_content);
		imgGroup = (LinearLayout) view.findViewById(R.id.question_detail_img_layout);
		listView = (InnerListView) view.findViewById(R.id.question_detail_listview);
		ScrollView scrilview = (ScrollView) view.findViewById(R.id.scroll_view);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//mListener.onArticleSelectedBack("com.snsoft.aiot.phone.fragment.ExpertFragment", null);
				((AbsFgtActivity)getActivity()).popFragment(0);
			}
		});
		
		nameTv.setText(detail.getName());
		contentTv.setText(detail.getContent());
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
		if(detail.getImgList()!=null){
			for (int i = 0; i < detail.getImgList().size(); i++) {
				ImageView imageView = new ImageView(getActivity());
				lp.setMargins(10, 10, 10, 10);
				imageView.setLayoutParams(lp);
				DownloadImageTask task = new DownloadImageTask(getActivity(),imageView);
				task.execute(detail.getImgList().get(i).get("url"));
				imgGroup.addView(imageView);
			}
		}
		
		listView.setAdapter(new ListAdapter());
		listView.setParentScrollView(scrilview);
        listView.setMaxHeight(400);
	}
	//获取数据和错误代码判断
	private void initData(){
		position = getArguments().getInt("position");
		ArrayList<HashMap<String,String>> aList = DataController.getHotQuestionList().getList();
		detail = DataController.getHotQuestionData(aList.get(position).get("id"));
		if(detail != null){
			if(NetworkUtil.isErrorCode(detail.getCode(), getActivity())){
				list = new ArrayList<HashMap<String,String>>();
			} else {
				list = detail.getGroupList();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			detail = new DetailQuestion();
			list = new ArrayList<HashMap<String,String>>();
		}
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
				convertView = inflater.inflate(R.layout.expert_question_detail_list_item_layout,
						null);
			}
			
			ImageView img = (ImageView) convertView.findViewById(R.id.question_detail_list_item_img);
			TextView name = (TextView) convertView.findViewById(R.id.question_detail_list_item_name);
			TextView content = (TextView) convertView.findViewById(R.id.question_detail_list_item_content);
			TextView time = (TextView) convertView.findViewById(R.id.question_detail_list_item_time);
			TextView type = (TextView) convertView.findViewById(R.id.question_detail_list_item_type);			
			
			DownloadImageTask task = new DownloadImageTask(getActivity(),img);
			task.execute(list.get(position).get("img"));
			name.setText(list.get(position).get("name"));
			time.setText(list.get(position).get("time"));
			type.setText(list.get(position).get("type"));
			content.setText(list.get(position).get("value"));
					
			return convertView;
		}
	}
}
