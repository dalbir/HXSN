package com.hxsn.town.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.hxsn.town.R;
import com.hxsn.town.activity.CircumDetailActivity;
import com.hxsn.town.data.DataController;
import com.hxsn.town.data.ImageService;
import com.hxsn.town.model.ConditionListModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;




/*
 * 周边页，主要是地图定位及检索周边。利用百度地图SDK V3.0.0,定位SDK V4.2
 */
public class CircumFragment extends MyFragment{
	
	private LocationClient mLocationClient = null;
	private PoiSearch mPoiSearch = null;
	private BDLocationListener myListener = new MyLocationListenner();
	private BaiduMapOptions options;
	private PopupWindow mPopupWindow;
	
	private View view;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private GeoCoder mCoder;
	
	private DrawerLayout mDrawerLayout;
	private Button mSearchBtn;
	private LinearLayout mLeftDrawer;
	private ListView mCatrgoryLv;
	private ListView mDetailLv;
	
	boolean isFirstLoc = true;// 是否首次定位
	private String currentCity;
	private LatLng latLng;
	private String searchValue;//检索信息
	private boolean isOwn = false;//条件搜索，是否是百度地图自带搜索
	private ArrayList<ArrayList<String>> mListStr;//条件搜索菜单name
	private ArrayList<ArrayList<String>> mListStrId;//条件搜索菜单id
	
	private int mPosition = 0; //条件搜索一级菜单位置
	
	private ArrayList<HashMap<String, String>> mRimParkList;//初始周边园区信息列表
	
	int[] imgs = {R.drawable.icon_marka,R.drawable.icon_markb,R.drawable.icon_markc,R.drawable.icon_markd,R.drawable.icon_marke,
			R.drawable.icon_markf,R.drawable.icon_markg,R.drawable.icon_markh,R.drawable.icon_marki,R.drawable.icon_markj};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.main_circum_layout, null);
		initView();
		initData();
		initDrawer();
		return view;
	}
	//地图上附加条件筛选侧边栏
	private void initDrawer(){
	
		ConditionListModel model = DataController.getConditionListData();
		ArrayList<String> list = model.getCategoryList();
		ArrayList<ArrayList<HashMap<String,String>>> mList = model.getEventList();
		mListStr = new ArrayList<ArrayList<String>>();
		mListStrId = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < mList.size(); i++) {
			ArrayList<String> list1 = new ArrayList<String>();
			ArrayList<String> list2 = new ArrayList<String>();
			for (int j = 0; j < mList.get(i).size(); j++) {
				list1.add(mList.get(i).get(j).get("name"));
				list2.add(mList.get(i).get(j).get("id"));
			}
			mListStrId.add(list2);
			mListStr.add(list1);
		}
		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		mLeftDrawer = (LinearLayout) view.findViewById(R.id.left_drawer);
		mSearchBtn = (Button) view.findViewById(R.id.circum_left_drawer_button);
		mCatrgoryLv = (ListView) view.findViewById(R.id.circum_drawer_catrgory);
		mDetailLv = (ListView) view.findViewById(R.id.circum_drawer_detail);
		
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(),R.layout.listview_item_textview,list);
		mCatrgoryLv.setAdapter(categoryAdapter);
		
		mDetailLv.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listview_item_textview,mListStr.get(3)));
		isOwn = true;//
		mCatrgoryLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mDetailLv.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listview_item_textview,mListStr.get(arg2)));
				if(arg2 == mListStr.size()-1){
					isOwn = true;
				}
				mPosition = arg2;
			}
		});
		
		mDetailLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (isOwn) {
					mPoiSearch.searchNearby((new PoiNearbySearchOption())
							.location(latLng)
							.keyword(((TextView)arg1).getText().toString())
							.pageCapacity(20)
							.radius(3000));//3000米以内搜索结果
					searchValue = ((TextView)arg1).getText().toString();
				} else {
					initSearchPoiOverlay(arg2);
				}
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
		});
		
		
		mSearchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mDrawerLayout.isDrawerOpen(mLeftDrawer)){
					mDrawerLayout.closeDrawer(Gravity.LEFT);
				} else {
					mDrawerLayout.openDrawer(Gravity.LEFT);
				}
			}
		});
		
	}
	
	private void initData(){
		mLocationClient = new LocationClient(getActivity().getApplicationContext());     
	    mLocationClient.registerLocationListener( myListener );  
	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
	    option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    mLocationClient.setLocOption(option);
	    mLocationClient.start();
	}
	
	private void initView(){
		//之所以new mapview是为了以后用到去标尺，缩放控件等用到的BaiduMapOptions
		FrameLayout fl = (FrameLayout) view.findViewById(R.id.content_frame);
		mMapView = new MapView(getActivity());
		fl.addView(mMapView);
		mMapView.removeViewAt(2);//去掉縮放控件，不建议这样做，建议用BaiduMapOptions
		mMapView.removeViewAt(1);//去掉百度地图logo,不建议这样做
		
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		
		mCoder = GeoCoder.newInstance();
		mCoder.setOnGetGeoCodeResultListener(listener);
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				if (null != mPopupWindow) { 
		            mPopupWindow.dismiss(); 
		        }
			}
		});
		mPoiSearch = PoiSearch.newInstance();//自带周边搜索实例
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
	}
	//地址编码和反编码
	private OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {

		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	            //没有找到检索结果  
	        }  
			String city = result.getAddressDetail().city;
//			currentCity = result.getAddressDetail().district;
			LatLng ll = result.getLocation();
			latLng = result.getLocation();
//			Toast.makeText(getActivity(), city,
//					Toast.LENGTH_LONG).show();
			initPoiOverlay(ll);
		}
	};
	
	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){  
		@Override
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
		//获取Place详情页检索结果
			if (arg0.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "成功，查看详情页面", Toast.LENGTH_SHORT)
						.show();
			}
		}
		@Override
		public void onGetPoiResult(PoiResult result) {
		//获取POI检索结果  
			if (result == null
					|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				mBaiduMap.clear();
				PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);

				overlay.setData(result);
				overlay.addToMap();
				overlay.zoomToSpan();
				return;
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

				// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
				String strInfo = "在";
				for (CityInfo cityInfo : result.getSuggestCityList()) {
					strInfo += cityInfo.city;
					strInfo += ",";
				}
				strInfo += "找到结果";
				Toast.makeText(getActivity(), strInfo, Toast.LENGTH_LONG)
						.show();
			}
		}  
	};
	
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			}
			return true;
		}
	}
	
	//条件搜索标注
	private void initSearchPoiOverlay(int position){
		double[] db= getRange(latLng.longitude,latLng.latitude,15000);
		mRimParkList = DataController.getRimParkData(db[1], db[3], db[0], db[2],mListStrId.get(mPosition).get(position));
		if(mRimParkList == null || mRimParkList.size()==0){
			Toast.makeText(getActivity(), "当前周边没有大棚", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		for (int i = 0; i < mRimParkList.size(); i++) {
			BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(imgs[i]);
			LatLng mLl = new LatLng(Double.parseDouble(mRimParkList.get(i).get("lat")),Double.parseDouble(mRimParkList.get(i).get("lon")));
			OverlayOptions oo = new MarkerOptions().position(mLl).icon(bd).zIndex(i);
			mBaiduMap.addOverlay(oo);
		}
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				getPopupWindowInstance(marker.getZIndex());
				mPopupWindow.showAtLocation(view.findViewById(R.id.circum_main_layout), Gravity.BOTTOM, 0, 62);
				return true;
			}
		});
	}
	
	//初始化poi周边标记
	private void initPoiOverlay(LatLng ll){
		double[] db= getRange(ll.longitude,ll.latitude,15000);
		mRimParkList = DataController.getRimParkData(db[1], db[3], db[0], db[2],"null");
		if(mRimParkList == null || mRimParkList.size()==0){
			Toast.makeText(getActivity(), "当前周边没有园区", Toast.LENGTH_LONG).show();
			return;
		}
		for (int i = 0; i < mRimParkList.size(); i++) {
			BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(imgs[i]);
			LatLng mLl = new LatLng(Double.parseDouble(mRimParkList.get(i).get("lat")),Double.parseDouble(mRimParkList.get(i).get("lon")));
			OverlayOptions oo = new MarkerOptions().position(mLl).icon(bd).zIndex(i);
			mBaiduMap.addOverlay(oo);
		}
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				getPopupWindowInstance(marker.getZIndex());
				mPopupWindow.showAtLocation(view.findViewById(R.id.circum_main_layout), Gravity.BOTTOM, 0, 62);
				return true;
			}
		});
	}
	/*
	 * 获取popupwindwo实例
	 */
	private void getPopupWindowInstance(int position) { 
        if (mPopupWindow != null){
        	mPopupWindow.dismiss();
        }
        initPopuptWindow(position);
    }
	/*
	 * 初始化popupWindow
	 */
	private void initPopuptWindow(final int position) { 
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity()); 
        int mScreenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth(); 
        View popupWindow = layoutInflater.inflate(R.layout.circum_popup_window_layout, null); 
        TextView tvName = (TextView) popupWindow.findViewById(R.id.circum_popup_name);
        TextView tvValue = (TextView) popupWindow.findViewById(R.id.circum_popup_value);
        ImageView imageView = (ImageView) popupWindow.findViewById(R.id.circum_popup_image);
        ImageView detailView = (ImageView) popupWindow.findViewById(R.id.circum_popup_indetail);
        detailView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("url", mRimParkList.get(position).get("id"));
				intent.putExtras(bundle);
				intent.setClass(getActivity(), CircumDetailActivity.class);
				getActivity().startActivity(intent);
			}
		});
        Bitmap bitmap;
		try {
			bitmap = ImageService.getBitmap(mRimParkList.get(position).get("img"));
			imageView.setImageBitmap(bitmap);
		} catch (IOException e) {
			Log.i("CircumFragment", e.toString()); 
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.load_error));
		}
        tvName.setText(mRimParkList.get(position).get("name"));
        tvValue.setText(mRimParkList.get(position).get("about"));
        mPopupWindow = new PopupWindow(popupWindow, mScreenWidth, 100); 
 
    } 
	
	/**
	 * 定位SDK监听函数
	 */
	private class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				mCoder.reverseGeoCode(new ReverseGeoCodeOption()//反编码，调用onGetReverseGeoCodeResult方法
				.location(ll));
				mLocationClient.stop();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	/**
	* 根据圆心、半径算出经纬度范围
	* 4
	* @param lon
	* 圆心经度
	* @param lat
	* 圆心纬度
	* @param r
	* 半径（米）
	* @return double[4] 南侧经度，北侧经度，西侧纬度，东侧纬度
	*/
	public static double[] getRange(double lon, double lat, int r) {
		double DEF_PI = 3.14159265359; // PI
		double DEF_PI180 = 0.01745329252; // PI/180.0
		double DEF_R = 6370693.5; // radius of earth
		
	    double[] range = new double[4];
	    // 角度转换为弧度
	    double ns = lat * DEF_PI180;
	    double sinNs = Math.sin(ns);
	    double cosNs = Math.cos(ns);
	    double cosTmp = Math.cos(r / DEF_R);
	    // 经度的差值
	    double lonDif = Math.acos((cosTmp - sinNs * sinNs) / (cosNs * cosNs)) / DEF_PI180;
	    // 保存经度
	    range[0] = lon - lonDif;
	    range[1] = lon + lonDif;
	    double m = 0 - 2 * cosTmp * sinNs;
	    double n = cosTmp * cosTmp - cosNs * cosNs;
	    double o1 = (0 - m - Math.sqrt(m * m - 4 * (n))) / 2;
	    double o2 = (0 - m + Math.sqrt(m * m - 4 * (n))) / 2;
	    // 纬度
	    double lat1 = 180 / DEF_PI * Math.asin(o1);
	    double lat2 = 180 / DEF_PI * Math.asin(o2);
	    // 保存
	    range[2] = lat1;
	    range[3] = lat2;
	    return range;
	}
	
	public static boolean onKeyDown(Activity context){
		context.finish();
		return false;
	}
	
	@Override
	public void onDestroy() {  
		mLocationClient.stop();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mPoiSearch.destroy();
		mMapView = null;

        super.onDestroy();  
    }  
    @Override
	public void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        isFirstLoc = true;//解决在每次切换标签后，会执行一次追踪当前位置
        mMapView.onResume();  
    }  
    @Override
	public void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        if (null != mPopupWindow) { 
            mPopupWindow.dismiss(); 
        }
        mMapView.onPause();  
    }  
}
