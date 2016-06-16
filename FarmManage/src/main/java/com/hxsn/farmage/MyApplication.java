package com.hxsn.farmage;


import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;

import com.hxsn.farmage.beans.Contactor;
import com.hxsn.farmage.db.DBForQQ;
import com.hxsn.farmage.activity.DistinctListActivity;
import com.hxsn.farmage.utils.Shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class MyApplication extends Application {

	private static Map<String, Object> CacheData = new HashMap<String, Object>();
	public static boolean isShowLog = true;
	public static MyApplication app;
	public DBForQQ dbForQQ;
	public List<Contactor> servers = null;
	public DistinctListActivity dkActivity;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		Shared.init(this);
		servers = new ArrayList<Contactor>();
		dbForQQ = new DBForQQ(getApplicationContext());
		getVersion();
	}

	private void getVersion() {
		PackageManager manager = getPackageManager();
		String version = null;
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			version = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static Map<String, Object> getCacheData() {
		return CacheData;
	}

	public static void AddCacheData(String key, Object value) {
		CacheData.put(key, value);
	}

	public static void DelCacheData(String key) {
		if (CacheData.containsKey(key))
			CacheData.remove(key);
	}

	public static void CleCacheData(String key) {
		CacheData.clear();
	}

	public static Object GetCacheData(String key) {
		if (CacheData.containsKey(key)) {
			return CacheData.get(key);
		}
		return null;
	}

}
