package com.hxsn.iot.fragment.expert;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.DownloadImageTask;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.util.HashMap;
import java.util.List;


public class ExpertDetailFragment extends AbsBaseFgt {
	private View view;
	private List<HashMap<String,String>> list;
	private int position;
	private OnArticleSelectedListener mListener;  
	private ParseDatas data;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.expert_detail_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initView() {
		ImageView imageView = (ImageView) view.findViewById(R.id.expert_detail_img);
		TextView nameTv = (TextView) view.findViewById(R.id.expert_detail_name);
		TextView typeTv = (TextView) view.findViewById(R.id.expert_detail_type);
		TextView detail = (TextView) view.findViewById(R.id.expert_detail_textview);
		Button questionBtn = (Button) view.findViewById(R.id.expert_detail_question);
		Button backBtn = (Button) view.findViewById(R.id.expert_search_back_btn);
		
		DownloadImageTask task = new DownloadImageTask(getActivity(),imageView);
		task.execute(list.get(position).get("img"));
		
		nameTv.setText(list.get(position).get("name"));
		typeTv.setText(list.get(position).get("type"));
		detail.setText(list.get(position).get("value"));
		
		questionBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("parseDatas", data);
				bundle.putInt("position", position);
				mListener.onArticleSelected("com.snsoft.aiot.phone.fragment.ExpertMainTop","com.snsoft.aiot.phone.expert.AskQuestionFragment", bundle);
			}
		});
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Bundle bundle = new Bundle();
				//bundle.putSerializable("parseDatas", data);
				//mListener.onArticleSelectedBack("com.snsoft.aiot.phone.expert.ExpertSearchFragment", bundle);
				((AbsFgtActivity)getActivity()).popFragment(1);
			}
		});
	}
	
	private void initData() {
		position = getArguments().getInt("position");
		data = (ParseDatas) getArguments().getSerializable("parseDatas");
		list = data.getList();
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
}
