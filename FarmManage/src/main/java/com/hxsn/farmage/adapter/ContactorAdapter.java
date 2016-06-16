package com.hxsn.farmage.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxsn.farmage.R;
import com.hxsn.farmage.base.MyBaseAdapter;
import com.hxsn.farmage.beans.ChatListEntity;
import com.hxsn.farmage.utils.EmojiUtil;
import com.hxsn.farmage.utils.HeadImgUtil;

@SuppressLint("InflateParams")
public class ContactorAdapter extends MyBaseAdapter<ChatListEntity> {

	ViewHolder holder;

	public ContactorAdapter(Context context) {
		super(context);
	}
	
	public void upData() {
		List<ChatListEntity> list = new ArrayList<ChatListEntity>();
		list.addAll(sort(myList));
		myList.clear();
		myList.addAll(list);
		list.clear();
		notifyDataSetChanged();
	}

	@Override
	public View getMyView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_concators, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_date_comm_item.setText(myList.get(position).getDate());
		holder.tv_name_comm_item.setText(myList.get(position).getName());
		holder.tv_type_comm_item.setText(myList.get(position).getRold());
		
		int remind = myList.get(position).getRemind();
		if (remind == 0) {
			holder.item_remind_tv.setVisibility(View.GONE);
		} else {
			if (remind < 100) {
				holder.item_remind_tv.setText(remind + "");
			} else {
				holder.item_remind_tv.setText("99+");
			}
			holder.item_remind_tv.setVisibility(View.VISIBLE);
		}
		
		try {
			EmojiUtil.handlerEmojiText(holder.tv_txt_comm_item, myList.get(position).getTxt(), context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new HeadImgUtil(myList.get(position).getId(), myList.get(position).getIvUrl(), holder.iv_comm_item, context);

		return convertView;
	}

	public class ViewHolder {
		public ImageView iv_comm_item;
		public TextView tv_name_comm_item, tv_date_comm_item,
				tv_type_comm_item, tv_txt_comm_item, item_remind_tv;

		public ViewHolder(View v) {
			item_remind_tv = (TextView) v.findViewById(R.id.item_remind_tv);
			iv_comm_item = (ImageView) v.findViewById(R.id.iv_comm_item);
			tv_name_comm_item = (TextView) v
					.findViewById(R.id.tv_name_comm_item);
			tv_date_comm_item = (TextView) v
					.findViewById(R.id.tv_date_comm_item);
			tv_type_comm_item = (TextView) v
					.findViewById(R.id.tv_type_comm_item);
			tv_txt_comm_item = (TextView) v.findViewById(R.id.tv_txt_comm_item);
		}
	}
	
	private  List<ChatListEntity> sort(List<ChatListEntity> list) {
		if (list != null && list.size() > 0) {
			for(int i = 0; i < list.size(); i++) {
				for(int j = 0; j < list.size() - 1 - i; j++) {
					if (list.get(j).getRemind() < list.get(j + 1).getRemind()) {
						ChatListEntity chatListEntity = list.get(j + 1);
						list.remove(j + 1);
						list.add(j, chatListEntity);
						chatListEntity = null;
					}
				}
			}
		}
		return list;
	}
}
