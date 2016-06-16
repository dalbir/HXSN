package com.hxsn.farmage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.R;
import com.hxsn.farmage.adapter.ContactorAdapter;
import com.hxsn.farmage.base.BaseFrgament;
import com.hxsn.farmage.beans.ChatListEntity;
import com.hxsn.farmage.beans.ChatMsgEntity;
import com.hxsn.farmage.beans.Contactor;
import com.hxsn.farmage.activity.OnLineActivity;

import java.util.ArrayList;
import java.util.List;

public class ComcationFragment extends BaseFrgament {

	private EditText et_work;
	private ListView lv_work_com;
	private ContactorAdapter adapter;
	// 全部列表信息
	private ArrayList<ChatListEntity> allList;
	// 查询结果列表
	private ArrayList<ChatListEntity> selectList;
	private String selStr;
	
	public static ComcationFragment comcationFragment = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_jiaoliu_fragment, container,
				false);
		initView(view);
		initListener();
		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		comcationFragment = this;
		// 从服务器获取列表到allList-----------------------------------------------------------
		initData();
	}
	
	private void initData() {
		allList.clear();
		List<Contactor> data = MyApplication.app.servers;
		for (int i = 0; i < data.size(); i++) {
			ChatListEntity chatListEntity = new ChatListEntity();
			chatListEntity.setId(data.get(i).getId());
			chatListEntity.setName(data.get(i).getName());
			chatListEntity.setPhone(data.get(i).getPhone());
			chatListEntity.setIvUrl(data.get(i).getUheadpic());
			chatListEntity.setRold(data.get(i).getRole());
			chatListEntity.setRemind(MyApplication.app.dbForQQ.queryRemind(chatListEntity.getId()));
			ChatMsgEntity chatMsgEntity = MyApplication.app.dbForQQ.queyOneChat(chatListEntity.getId());
			if (chatMsgEntity != null) {
				chatListEntity.setDate(chatMsgEntity.getDate());
				String text = "";
//				if (chatMsgEntity.getMsgType()) {
//					text += chatListEntity.getName() + "：";
//				} else {
//					text += Shared.getUserName() + "：";
//				}
				int type = chatMsgEntity.getType();
				if (type == 0) {
					text += chatMsgEntity.getText();
				} else if (type == 1) {
					text += "[语音]";
				} else if (type == 2) {
					text += "[图片]";
				} else {
					text += "[其他]";
				}
				chatListEntity.setTxt(text);
			} else {
				chatListEntity.setTxt("暂无聊天信息");
				chatListEntity.setDate("");
			}
			allList.add(chatListEntity);
		}
		
		adapter.appendData(allList, true);
		adapter.upData();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		comcationFragment = null;
		super.onStop();
	}

	private void initView(View view) {
		et_work = (EditText) view.findViewById(R.id.et_work_com);
		lv_work_com = (ListView) view.findViewById(R.id.lv_work_com);
		allList = new ArrayList<ChatListEntity>();
		

		selectList = new ArrayList<ChatListEntity>();
		adapter = new ContactorAdapter(getActivity());
		lv_work_com.setAdapter(adapter);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		et_work.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				check();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		lv_work_com.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), OnLineActivity.class);
				intent.putExtra("otherID", adapter.getAdapterData().get(arg2).getId());
				intent.putExtra("otherName", adapter.getAdapterData().get(arg2).getName());
				intent.putExtra("titleName", adapter.getAdapterData().get(arg2).getName());
				intent.putExtra("headPic", adapter.getAdapterData().get(arg2).getIvUrl());
				intent.putExtra("phone", adapter.getAdapterData().get(arg2).getPhone());
				intent.putExtra("dkid", adapter.getAdapterData().get(arg2).getRold());
				intent.putExtra("num", adapter.getAdapterData().get(arg2).getRemind());
				startActivity(intent);
			}
		});
	}
	
	private void check() {
		selStr = et_work.getText().toString();
		if("".equals(selStr)){
			adapter.appendData(allList, true);
		}else{
			selectList.clear();
			for(int i=0;i<allList.size();i++){
				if(allList.get(i).getName().contains(selStr))
					selectList.add(allList.get(i));
			}
			adapter.appendData(selectList, true);
		}
		adapter.upData();
	}
	
	public void upDataRemind() {
		// TODO Auto-generated method stub
		initData();
		check();
	}
}
