package com.hxsn.town;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

/*
 * Activity管理器
 */
public class AppManager
{
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	private AppManager()
	{

	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager()
	{
		if (instance == null)
		{
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栄1??7
	 */
	public void addActivity(Activity activity)
	{
		if (activityStack == null)
		{
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（得到堆栈中的最后的元素＄1??7
	 */
	public Activity currentActivity()
	{
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（结束堆栈中的最后元素）
	 */
	public void finishActivity()
	{
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity)
	{
		if (activity != null)
		{
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity)
	{
		if (activity != null)
		{
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls)
	{
		for (Activity activity : activityStack)
		{
			if (activity.getClass().equals(cls))
			{
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity()
	{
		for (int i = 0, size = activityStack.size(); i < size; i++)
		{
			if (null != activityStack.get(i))
			{
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 *退出程序，不建议使用
	 */
	public void AppExit(Context context, Boolean isBackground)
	{
		try
		{
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			//android2.1版本以后去掉了该方法，改用killBackgroundProcesses亄1??7
			//霄1??7要权限：<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
			activityMgr.restartPackage(context.getPackageName());
		} catch (Exception e)
		{

		} finally
		{
			// 注意，如果您有后台程序运行，请不要支持此句子
			if (!isBackground)
			{
				System.exit(0);
			}
		}
	}
	
	/**
	 * 退出程序
	 */
	public void AppExit(Context context){
		int currentVersion = android.os.Build.VERSION.SDK_INT;  
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {  
            Intent startMain = new Intent(Intent.ACTION_MAIN);  
            startMain.addCategory(Intent.CATEGORY_HOME);  
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            context.startActivity(startMain);  
            System.exit(0);  
        } else {// android2.1  
            ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);  
            am.restartPackage(context.getPackageName());  
        }  
	}
}
