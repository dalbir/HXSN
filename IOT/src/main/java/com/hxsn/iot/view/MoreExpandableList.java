package com.hxsn.iot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.ViewHolder;
import com.hxsn.iot.control.DataHolder;
import com.hxsn.iot.model.ChildData;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.GroupData;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.util.AbsData;
import com.hxsn.iot.activity.AbsFgtActivity;

import java.util.ArrayList;
import java.util.List;


public class MoreExpandableList extends LinearLayout implements GroupView.OnGroupClickListener,
	ChildView.OnChildClickListener{
	
	private DataHolder dataHolder = new DataHolder();
	private ViewHolder viewholder = new ViewHolder();
	
	private View view;
	private Button selectAllBtn;
	private Button makeSureBtn;
	private MoreExpandableListAdapter mListAdapter;
	private AbsFgtActivity mHostActivity;
	private Context mContext;
	
	public MoreExpandableList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MoreExpandableList(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		view = LayoutInflater.from(context).inflate(R.layout.drawer_more_view_expand, null);
	    ExpandableListView mMoreDrawerList = (ExpandableListView) view.findViewById(R.id.more_expand_listview);
	    List<GroupData>  contentData = getContentData();
		dataHolder.setContentData(contentData);
		mListAdapter = new MoreExpandableListAdapter(context);
		mMoreDrawerList.setAdapter(mListAdapter);
		//首次加载全部展开
		for (int i = 0; contentData != null && i < contentData.size(); i++) {
			mMoreDrawerList.expandGroup(i);
		}
  		mMoreDrawerList.setGroupIndicator(null);
  		//点击Group不收缩
  		mMoreDrawerList.setOnGroupClickListener(new OnGroupClickListener() {
  				@Override 
  				public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) { 
  				    return true;
  				}
  		});
  		
  		selectAllBtn = (Button) view.findViewById(R.id.more_checkall_btn);
  		selectAllBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(selectAllBtn.getText().toString().equals("全选")){
					selectAllBtn.setText("全不选");
					dataHolder.setAllGroupAndChildChecked();
					viewholder.setAllGroupAndChildChecked();
				}else{
					selectAllBtn.setText("全选");
					dataHolder.setAllGroupAndChildUnChecked();
					viewholder.setAllGroupAndChildUnChecked();
				}
				mListAdapter.notifyDataSetChanged();
			}
		});
  		makeSureBtn = (Button) view.findViewById(R.id.more_sure_btn);
  		makeSureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//增加对比单元数据
				List<GroupData> list = dataHolder.getCheckedDataList();
				ArrayList<Danyuan> listDy = new ArrayList<Danyuan>();
				listDy.add(AbsApplication.getInstance().getCurDy());
				for(int i = 0; i < list.size(); i++){
					GroupData gd = list.get(i);
					for(int j = 0; j < gd.getItems().size(); j++){
						if(AbsApplication.getInstance().getCurDy().getId().equals(gd.getItems().get(j).getDanYuan().getId())){
							continue;
						}
						listDy.add(gd.getItems().get(j).getDanYuan());
					}
				}
				Contents.getInstance().setList(listDy);//存储数据到中间变量
				if(listDy.size() == 1){
					Toast.makeText(mContext, "请选择数据", Toast.LENGTH_LONG).show();
				} else {
					((AbsFgtActivity) mContext).realizeSureBtn(new AbsData(AbsData.MONITOR_COMPARE,listDy));
					mHostActivity.closeDrawer(1);
				}
			}
		});
  		
        addView(view);
	}
	
	public void refresh() {
		List<GroupData>  contentData = getContentData();
		dataHolder.setContentData(contentData);
		mListAdapter.notifyDataSetChanged();
	}

	public MoreExpandableList(final Context context) {
		super(context);
	}
	
	public void setHostActivity(AbsFgtActivity mainActivity) {
		this.mHostActivity = mainActivity;
	}

	@Override
	public void onChildChecked(int groupPosition, int childPosition) {
		dataHolder.setChildChecked(groupPosition, childPosition);
		viewholder.setChildChecked(groupPosition, childPosition);
	}

	@Override
	public void onChildUnChecked(int groupPosition, int childPosition) {
		dataHolder.setChildUnChecked(groupPosition, childPosition);
		viewholder.setChildUnChecked(groupPosition, childPosition);
	}

	@Override
	public void onGroupChecked(int groupPosition) {
		dataHolder.setGroupChecked(groupPosition);
		viewholder.setGroupChecked(groupPosition);
	}

	@Override
	public void onGroupUnChecked(int groupPosition) {
		dataHolder.setGroupUnChecked(groupPosition);
		viewholder.setGroupUnChecked(groupPosition);
	}

	private class MoreExpandableListAdapter extends BaseExpandableListAdapter {
		
		private Context context;
		
		public MoreExpandableListAdapter(Context context) {
			this.context = context;
		}
		@Override
		public Object getGroup(int groupPosition) {
			return dataHolder.getGroupData(groupPosition);
		}
		@Override
		public int getGroupCount() {
			return dataHolder.getGroupCount();
		}
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return dataHolder.getChildData(groupPosition, childPosition);
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			return dataHolder.getChildCount(groupPosition);
		}
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			
			final GroupView groupView = new GroupView(MoreExpandableList.this, context);
			groupView.setGroupPosition(groupPosition);
			
			GroupData groupData = (GroupData)getGroup(groupPosition);
			groupView.getSelectGroup().setChecked(groupData.isGroupSelected());
			groupView.getGroupName().setText(groupData.getGroupName());
			
			viewholder.setGroupView(groupPosition, groupView);
			
			groupView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if(dataHolder.isGroupSelected(groupPosition)){
						onGroupUnChecked(groupPosition);
					}else{
						onGroupChecked(groupPosition);
					}
					notifyDataSetChanged();
				}
			});
			
			return groupView;
		}
		
		@Override
		public View getChildView(final int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			
			ImageHolder holder;
			if(convertView == null){
				holder = new ImageHolder();
				convertView = new ChildView(MoreExpandableList.this, context);
				holder.childView = (ChildView)convertView;
				convertView.setTag(holder);
			}else{
				holder = (ImageHolder)convertView.getTag();
			}
			
			ChildView childView = holder.childView;
			childView.setGroupPosition(groupPosition);
			childView.setChildPosition(childPosition);
			
			ChildData childData = (ChildData)getChild(groupPosition, childPosition);
			childView.getSelectChild().setChecked(childData.isChildSelected());
			childView.getChildName().setText(childData.getChildName());
			
			viewholder.setChildView(groupPosition, childPosition, childView);
			childView.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if(dataHolder.isChildSelected(groupPosition, childPosition)){
						onChildUnChecked(groupPosition, childPosition);
					}else{
						onChildChecked(groupPosition, childPosition);
					}
					notifyDataSetChanged();
				}
			});
			
			return childView;
		}
		class ImageHolder{
			ChildView childView;
		}
	}
    
    private List<GroupData> getContentData(){
    	//List<JiDi> list = (ArrayList<JiDi>)(DataController.getJidiDapengData().getObject());
    	List<JiDi> list = AbsApplication.getInstance().getJiDiList();
		List<GroupData> groupDatas = new ArrayList<GroupData>();
		for(int i=0;i<list.size();i++){
			ArrayList<Dapeng> listDp = list.get(i).getList();
			for(int j=0;j<listDp.size();j++){
				GroupData groupData = new GroupData();
				groupData.setGroupName(list.get(i).getName()+listDp.get(j).getName());
				groupData.setGroupSelected(false);
				List<ChildData> items = new ArrayList<ChildData>();
				ArrayList<Danyuan> listDy = listDp.get(j).getList();
				for (int k = 0; k < listDy.size(); k++) {
					ChildData childData = new ChildData();
					childData.setChildName(listDy.get(k).getName());
					childData.setChildSelected(false);
					childData.setDanYuan(listDy.get(k));
					items.add(childData);
				}
				groupData.setItems(items);
				groupDatas.add(groupData);
			}
		}
		return groupDatas;
	}
}
