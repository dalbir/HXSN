package com.hxsn.intelliwork.utils;

import android.util.Log;

import com.hxsn.intelliwork.MyApplication;



public class LogUtil {

	public static void showLog(String TAG, String message) {
		if (MyApplication.isShowLog) {
			Log.i("HXSN", TAG + "------" + message);
		}
	}
}
