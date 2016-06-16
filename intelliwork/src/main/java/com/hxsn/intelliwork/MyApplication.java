package com.hxsn.intelliwork;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;

import com.hxsn.intelliwork.beans.Contactor;
import com.hxsn.intelliwork.db.DBForQQ;
import com.hxsn.intelliwork.utils.Configura;
import com.hxsn.intelliwork.utils.Shared;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.senter.support.openapi.StUhf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyApplication extends Application {

	private static Map<String, Object> CacheData = new HashMap<String, Object>();
	public static boolean isShowLog = true;
	public static Context context;
	//public static MyApplication app;
	public static DBForQQ dbForQQ;
	public static List<Contactor> servers = null;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//app = this;
		context = getApplicationContext();
		Shared.init(this);
		servers = new ArrayList<Contactor>();
		dbForQQ = new DBForQQ(getApplicationContext());
		getVersion();
		initImageLoader(getApplicationContext());
	}
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
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
//		if (version != null) {
//			Shared.setVersion(version);
//		}
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
	/**
	 * RFID初始化
	 * */
	private static StUhf rfid;
	private static Configura mAppConfiguration;
	/**
	 * 初始化时用以生成超高频对象，以后就可以直接用rfid()来调用了。
	 */
	public static StUhf getRfid() {
		if (rfid == null) {
			StUhf rf = null;

			if (getSavedModel()==null) {
				try{
					rf = StUhf.getUhfInstance();//InterrogatorModel.InterrogatorModelD2
				}catch(Exception e) {
					rf = null;
				}
			}else {
				rf=StUhf.getUhfInstance(getSavedModel());
			}

			if (rf == null) {
				return null;
			}

			boolean b = rf.init();
			if (b == false) {
				return null;
			}
			rfid = rf;
			StUhf.InterrogatorModel model=rfid.getInterrogatorModel();
			saveModelName(model);
		}
		return rfid;
	}
	public static StUhf rfid()
	{
		return rfid;
	}
	private static final StUhf.InterrogatorModel getSavedModel() {
		String modelName=getConfiguration().getString("modelName", "");
		if (modelName.length()!=0) {
			return StUhf.InterrogatorModel.valueOf(modelName);
		}
		return null;
	}

	private static final void saveModelName(StUhf.InterrogatorModel model) {
		if (model==null) {
			throw new NullPointerException();
		}
		getConfiguration().setString("modelName", model.name());
	}

	private static final Configura getConfiguration() {
		if (mAppConfiguration==null) {
			mAppConfiguration=new Configura(context, "settings", Context.MODE_PRIVATE);
		}
		return mAppConfiguration;
	}

	public static boolean stop() {
		if (rfid != null) {
			if (rfid.isFunctionSupported(com.senter.support.openapi.StUhf.Function.StopOperation)) {
				for (int i = 0; i < 3; i++) {
					if (rfid().stopOperation()) {
						return true;
					}
				}
				return false;
			}
		}
		return true;
	}
}
