package com.hxsn.iot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;


public class SingleExpandableList extends LinearLayout{
	
	private View view;
	private SingleExpandableListAdapter mListAdapter;
	private AbsFgtActivity mHostActivity;
	private Object[][] storeDp ;
	private List<JiDi> list;
	private Context context;
	
	public SingleExpandableList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SingleExpandableList(final Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initData();
		view = LayoutInflater.from(context).inflate(R.layout.drawer_single_view_expand, null);
	    ExpandableListView mSingleDrawerList = (ExpandableListView)view;
	    mSingleDrawerList.setGroupIndicator(null);
		mListAdapter = new SingleExpandableListAdapter(context);
		mSingleDrawerList.setAdapter(mListAdapter);
		mSingleDrawerList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long arg4) {
				AbsApplication.getInstance().setDapeng((Dapeng)storeDp[arg2][arg3]);
				Toast.makeText(context, ((Dapeng)storeDp[arg2][arg3]).getName(), Toast.LENGTH_LONG	).show();
				mHostActivity.closeDrawer(0);
				mHostActivity.refresh();
				return false;
			}
		});
        addView(view);
	}

	public SingleExpandableList(final Context context) {
		super(context);
	}
	//网络错误码判断
	private void initData(){
		ParseDatas parse = DataController.getJidiDapengData();
		if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), context)){
				list = new ArrayList<JiDi>();
				return;
			} else {
				list = (List<JiDi>) (parse.getObject());
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), context);
			list = new ArrayList<JiDi>();
			return;
		}
	}
	
	public void setHostActivity(AbsFgtActivity mainActivity) {
		this.mHostActivity = mainActivity;
	}
	//获得第一级基地目录数据
	private String[] getGroupData() {
		String[] str = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			str[i] = list.get(i).getName();
		}
		return str;
	}
	//获得第二级大棚目录
	private String[][] getChildData() {
		String[][] str = new String[list.size()][];
		storeDp = new Object[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			str[i] = new String[list.get(i).getList().size()];
			storeDp[i] = new Object[list.get(i).getList().size()];
			for (int j = 0; j < list.get(i).getList().size(); j++) {
				str[i][j] = list.get(i).getList().get(j).getName();
				storeDp[i][j] = list.get(i).getList().get(j);
			}
		}
		return str;
	}
	
	private class SingleExpandableListAdapter extends BaseExpandableListAdapter {
		private String[] groupValue = getGroupData();
    	private String[][] childValue = getChildData(); 
    	
    	private LayoutInflater inflater;

		public SingleExpandableListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}
    	
		@Override
		public Object getChild(int arg0, int arg1) {
			return childValue[arg0][arg1];
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			return arg1;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean isExpanded, View convertView,
				ViewGroup arg4) {
			ChildHolder childHolder = null;
			if (convertView == null) {
				childHolder = new ChildHolder();
				convertView = inflater.inflate(R.layout.drawer_single_child_item, null);

				childHolder.textView = (TextView) convertView
						.findViewById(R.id.single_child_tv);

				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildHolder) convertView.getTag();
			}
			childHolder.textView.setText(getChild(arg0,arg1).toString());

			return convertView;
		}

		@Override
		public int getChildrenCount(int arg0) {
			return childValue[arg0].length;
		}

		@Override
		public Object getGroup(int arg0) {
			return groupValue[arg0];
		}

		@Override
		public int getGroupCount() {
			return groupValue.length;
		}

		@Override
		public long getGroupId(int arg0) {
			return arg0;
		}

		@Override
		public View getGroupView(int arg0, boolean isExpanded, View convertView,
				ViewGroup arg3) {
			
			GroupHolder groupHolder = null;
			if (convertView == null) {
				groupHolder = new GroupHolder();
				convertView = inflater.inflate(R.layout.drawer_single_group_item, null);
				groupHolder.textView = (TextView) convertView
						.findViewById(R.id.single_group_tv);
				groupHolder.textView.setTextSize(15);
				groupHolder.imageView = (ImageView) convertView.findViewById(R.id.single_group_img);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}

			groupHolder.textView.setText(getGroup(arg0).toString());
			
			if(isExpanded){
				groupHolder.imageView.setBackgroundResource(R.drawable.single_blow);
            }else{
            	groupHolder.imageView.setBackgroundResource(R.drawable.single_default);
            } 
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}
	};
	
	private class GroupHolder {
		TextView textView;
		ImageView imageView;
	}
    
    private class ChildHolder {
    	TextView textView;
    }
}
