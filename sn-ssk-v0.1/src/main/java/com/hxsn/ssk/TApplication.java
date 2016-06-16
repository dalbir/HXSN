package com.hxsn.ssk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.android.volley.RequestQueue;
import com.hxsn.ssk.beans.NotifyInfo;
import com.hxsn.ssk.beans.User;
import com.hxsn.ssk.db.DictionaryService;
import com.hxsn.ssk.utils.Const;
import com.hxsn.ssk.utils.VolleyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *  Created by Administrator on 16-4-7.
 */
public class TApplication extends Application {
    public static int versionType = Const.DEBUG_VERTION;  //0-text 1-debug, 2-release
    public static String URL_STRING,URL_NONGSH_STRING;//农事汇url,在另一个IP下
    public static String URL_UPDATE;//升级的地址有外网和内网
    public static Context context;
    public static DictionaryService dictionaryService;
    public static Resources resources;
    //public static List<User> users = null;
    public static int intAndroidSDK = android.os.Build.VERSION.SDK_INT;
    public static RequestQueue requestQueue;
    public static User user;

    public static List<Activity> activities;
    public static NotifyInfo notifyInfo;
    public static boolean isUpdateNongshImage= false;//判断是否需要更新图标,默认不需要更新，进入APP首先在APPBiz中判断是否更新
    public static String newsUrl = "";//农事汇或咨询的url


    public static String getValue(String key) {//获取字典
        return DictionaryService.getInstanse(context).getValue(key);
    }

    public static void setValue(String key, String value) {//设置字典
        DictionaryService.getInstanse(context).addValue(key, value);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        activities = new ArrayList<>();
        switch (versionType) {
            case Const.TEST_VERTION:
                URL_STRING = "http://192.168.12.94:7990/ssk/sskapp/";
                URL_UPDATE = URL_STRING + "appi/appversion";
                URL_NONGSH_STRING = "http://192.168.12.26:80/sskcms";
                break;
            case Const.TEST_VERTION1:
                URL_STRING = "http://192.168.12.94:7990/ssk/sskapp/";
                URL_UPDATE = URL_STRING + "appi/appversion";
                URL_NONGSH_STRING = "http://192.168.12.94:8980/sskcms";
                break;
            case Const.DEBUG_VERTION:
                URL_STRING = "http://192.168.12.94:7990/ssk/sskapp/";
                URL_UPDATE = URL_STRING + "appi/appversion";
                URL_NONGSH_STRING = "http://192.168.12.94:8980/sskcms";
                break;
            case Const.RELEASE_VERTION:
                URL_STRING = "http://60.10.151.28:7990/ssk/sskapp/";
                URL_UPDATE = URL_STRING + "app/appversion";
                URL_NONGSH_STRING = "http://60.10.151.28:8980/sskcms";
                break;
        }
        context = getApplicationContext();
        dictionaryService = DictionaryService.getInstanse(context);
        resources = getResources();
        VolleyUtil.initialize(this);
        requestQueue = VolleyUtil.getRequestQueue();
    }
}
