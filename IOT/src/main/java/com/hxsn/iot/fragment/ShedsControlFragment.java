package com.hxsn.iot.fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.activity.AiotActivity;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.ControlDevices;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.JiDi;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.ShedsControlInterface;
import com.hxsn.iot.view.CustomTextView;
import com.hxsn.iot.view.CustomThreeBtn;
import com.hxsn.iot.view.CustomTwoBtn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class ShedsControlFragment extends AbsBaseFgt implements ShedsControlInterface {
	
	private View view;
	private LinearLayout devicesLayout;
	private LinearLayout deviceLayout;
	private TextView validTv;
	private TextView nameTv;
	private CustomTextView image;
	private EditText timeEt;
	private CustomTwoBtn customTwo;
	private CustomThreeBtn customThree;
	private ListView listView;
	private List<HashMap<String,Object>> list;
	private int num = 2;
	private String devicesId;
	private String devicesKind;
	private CustomListAdapter adapter;
	private HashMap<String,String> mapKV;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.sheds_control_layout, container, false);
		initData();
		initView();
		initPwdView(view);
		return view;
	}
	
	private void initData() {
		ControlDevices devices = DataController.getShedsDevicesList();
		if(devices != null){
			if(NetworkUtil.isErrorCode(devices.getCode(), getActivity())){
				list = new ArrayList<HashMap<String,Object>>();
			} else {
				list = devices.getList();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
			list = new ArrayList<HashMap<String,Object>>();
		}
		Contents.getInstance().setFragment(this);
	}
	
	private void initView() {
		devicesLayout = (LinearLayout) view.findViewById(R.id.sheds_control_devices_layout);
		deviceLayout = (LinearLayout) view.findViewById(R.id.sheds_control_device_layout);
		validTv = (TextView) view.findViewById(R.id.sheds_control_valid_dp);
		image = (CustomTextView) view.findViewById(R.id.sheds_control_img);
		nameTv = (TextView) view.findViewById(R.id.sheds_control_name);
		timeEt = (EditText) view.findViewById(R.id.sheds_control_time_et);
		customTwo = (CustomTwoBtn) view.findViewById(R.id.sheds_control_custom_two_btn);
		customThree = (CustomThreeBtn) view.findViewById(R.id.sheds_control_custom_three_btn);
		listView = (ListView) view.findViewById(R.id.sheds_control_listview);
		customTwo.setContext(this);
		customThree.setContext(this);
//		image.setBackgroundDrawable(getDrawable("JL"));
//		image.setText("卷帘");
		deviceLayout.setVisibility(View.INVISIBLE);
		validTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((AiotActivity) getActivity()).openOrCloseMore();
			}
		});
		showDevices();
	}
	
	private void initPwdView(View view){
		SharedPreferences preference = getActivity().getSharedPreferences("data", 0);
		final String pwd = preference.getString("controlPwd", "");
		final EditText et = (EditText) view.findViewById(R.id.control_js_pwd_ed);
		Button btn = (Button) view.findViewById(R.id.control_js_pwd_btn);
		final LinearLayout ll = (LinearLayout) view.findViewById(R.id.control_js_pwd_layout);
		final LinearLayout content = (LinearLayout) view.findViewById(R.id.sheds_control_content_layout);
		if(Contents.getInstance().getIsChecked() || pwd.equals("")){
			content.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
		}
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(et.getText().toString().equals(pwd)){
					content.setVisibility(View.VISIBLE);
					ll.setVisibility(View.GONE);
				} else if(et.getText().toString().equals("")){
					Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "密码不正确", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private void showDevices() {
		for (int i = 0; i < list.size(); i++) {
			CustomTextView imageTv = new CustomTextView(getActivity(),null);
			imageTv.setText(list.get(i).get("deviceName").toString());
			final HashMap<String,Object> map = list.get(i);
			imageTv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					deviceLayout.setVisibility(View.VISIBLE);
					changeImage(map);
					devicesKind = map.get("deviceKind").toString();
					List<JiDi> list = DataController.getShedsData(map.get("deviceKind").toString());
					AbsApplication.getInstance().setJiDiList(list);
//					((AiotActivity) getActivity()).openOrCloseMore();
					((AiotActivity) getActivity()).refreshA();
				}
			});
			
			Drawable drawable = getDrawable(map.get("deviceKind").toString());
			imageTv.setBackgroundDrawable(drawable);
			devicesLayout.addView(imageTv);
		}
	}
	
	private Drawable getDrawable(String type) {
		Drawable draw;
		if(type.equals("BG")){
			draw = getResources().getDrawable(R.drawable.kongzhi_buguan);
		} else if(type.equals("DG")) {
			draw = getResources().getDrawable(R.drawable.kongzhi_diguan);
		} else if(type.equals("JL")) {
			draw = getResources().getDrawable(R.drawable.kongzhi_juanlian);
		} else if(type.equals("KM")) {
			draw = getResources().getDrawable(R.drawable.kongzhi_kaimo);
		} else {
			draw = getResources().getDrawable(R.drawable.kongzhi_default);
		}
		return draw;
	}
	
	private void changeImage(HashMap<String,Object> map) {
		image.setBackgroundDrawable(getDrawable(map.get("deviceKind").toString()));
		ArrayList<HashMap<String,String>> hashMap = (ArrayList<HashMap<String,String>>)(map.get("status"));
		num = hashMap.size();
		image.setText(map.get("deviceName").toString());
		nameTv.setText(map.get("deviceName").toString());
		if(num == 2){
			customTwo.setVisibility(View.VISIBLE);
			customThree.setVisibility(View.GONE);
			customTwo.initalView(hashMap);
		} else {
			customTwo.setVisibility(View.GONE);
			customThree.setVisibility(View.VISIBLE);
			customThree.initalView(hashMap);
		}
	}
	
	public void getMoreSureBtn(ArrayList<Danyuan> list) {
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < list.size(); i++) {
//			sb.append(list.get(i).getId()).append(",");
//		}
//		devicesId = sb.toString();
//		devicesId = devicesId.substring(0,devicesId.length()-1);
		if(list.size()==0){
			validTv.setText("请选择需要控制大棚"); 
		} else {
			validTv.setText("有效大棚("+list.size()+")个"); 
		}
		String[][] str = new String[list.size()][2];
		mapKV = new LinkedHashMap<String,String>();
		for (int j = 0; j < list.size(); j++) {
			for (int m = 0; m < list.get(j).getList().size(); m++) {
				String name = list.get(j).getJidiName()+list.get(j).getDapengName()+list.get(j).getName()+list.get(j).getList().get(m).getName();
				String id = list.get(j).getList().get(m).getId();
				mapKV.put(id, name);
			}
		}
		adapter = new CustomListAdapter();
		adapter.setData(mapKV);
		listView.setAdapter(adapter);
	}
	
	public class CustomListAdapter extends BaseAdapter {
		private HashMap<String,String> map;
		private ArrayList<String> strValue ;
		private ArrayList<String> strKey;
		private ArrayList<String> deviceIdList;
		
		public void setData(HashMap<String,String> map){
			this.map = map;
			Set set = map.keySet();
			Iterator it = set.iterator();
			strValue = new ArrayList<String>();
			strKey = new ArrayList<String>();
			deviceIdList = new ArrayList<String>();
			while(it.hasNext()){
				String key = (String)it.next();
				String value = (String)map.get(key);
				strKey.add(key);
				strValue.add(value);
			}
		}
		
		public ArrayList<String> getDeviceIdList(){
			return deviceIdList;
		}

		@Override
		public int getCount() {
			return strValue.size();
		}

		@Override
		public Object getItem(int arg0) {
			return strValue.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView  == null){
				viewHolder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.sheds_control_listview_item_layout	, null);
				viewHolder.vhTextView = (TextView) convertView.findViewById(R.id.sheds_control_listview_item_tv);
				viewHolder.vhCheckBox = (CheckBox) convertView.findViewById(R.id.sheds_control_listview_item_cb);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.vhTextView.setText(strValue.get(position));
			viewHolder.vhCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						deviceIdList.add(strKey.get(position));
					} else {
						deviceIdList.remove(strKey.get(position));
					}
				}
			});
			
			return convertView;
		}
		
	}
	
	private static class ViewHolder{
		TextView vhTextView;
		CheckBox vhCheckBox;
	}

	@Override
	public String[] getValue() {
		String[] str = new String[3];
		ArrayList<String> list = adapter.getDeviceIdList();
		if(list.size()!=0){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i)+",");
			}
			String ids = sb.toString();
			ids = ids.substring(0,ids.length()-1);
			str[0] = ids;
			str[1] = devicesKind;
			str[2] = timeEt.getText().toString();
		} else {
			str[0] = null;
		}
		return str;
	}
	
	@Override
	public void refreshView(ArrayList<HashMap<String, String>> list) {
//		String[][] str = new String[list.size()][2];
//		for (int i = 0; i < list.size(); i++) {
//			Danyuan dy = DataController.getDanyuanWithId(list.get(i).get("id"));
//			str[i][0] = dy.getJidiName()+dy.getDapengName()+dy.getName();
//			str[i][1] = list.get(i).get("status");
//		}
//		adapter.setData(str);
//		adapter.notifyDataSetChanged();
		RefreshAdapter reAdapter = new RefreshAdapter(list);
		listView.setAdapter(reAdapter);
	}
	
	private class RefreshAdapter extends BaseAdapter{
		private ArrayList<HashMap<String,String>> list;
		public RefreshAdapter(ArrayList<HashMap<String,String>> list){
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.sheds_control_refresh_list_item_layout, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.sheds_control_refresh_listview_item_tv);
			ImageView iv = (ImageView) convertView.findViewById(R.id.sheds_control_refresh_listview_item_iv);
			
			tv.setText(mapKV.get(list.get(position).get("sbid")));
			if(list.get(position).get("status").equals("0")){
				iv.setBackgroundResource(R.drawable.sheds_control_refresh_false);
			} else {
				iv.setBackgroundResource(R.drawable.sheds_control_refresh_true);
			}
			return convertView;
		}
	}
	
	@Override
	public void onResume() {
		((AbsFgtActivity)getActivity()).setLockMode(false);
		super.onResume();
	}
	
	@Override
	public void onStop() {
		((AbsFgtActivity)getActivity()).setLockMode(true);
		super.onStop();
	}

}
