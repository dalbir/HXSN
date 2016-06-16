package com.hxsn.iot.activity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.fragment.AbsBottomFgt;
import com.hxsn.iot.util.AbsData;
import com.hxsn.iot.fragment.AbsTopFgt;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.TLog;
import com.hxsn.iot.view.MoreExpandableList;
import com.hxsn.iot.view.SingleExpandableList;
import com.hxsn.iot.model.Contents;
import com.hxsn.iot.model.JiDi;

import java.util.ArrayList;
import java.util.List;


/**
 * 包含Fragment的Activity
 */
public abstract class AbsFgtActivity extends Activity implements AbsBaseFgt.IFgtAEventListener
			,AbsTopFgt.IFgtBackEventListener,AbsBottomFgt.IFgtBottomListener {
	
	public static final String POPWINDOW = "POPWINDOW";
	
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	private DrawerLayout mDrawerLayout;  

	private SingleExpandableList mSingleDrawerList;
	private MoreExpandableList mMoreDrawerList;
	
	private String[] topFgtNames;
	private String[] baseFgtNames;

	/**设置带有上中下三层的fragments布局Layout。布局id定义fgt_top、fgt_middle、fgt_bottom*/
	public abstract int setBaseLayout();
	
	/**此Activity对应的中间Fragments*/
	public abstract String[] setMiddleFargmentNames();
	
	/**此Activity对应的顶部Fragments*/
	public abstract String[] setTopFargmentNames();
	
	/**在此方法中实现popwindows操作*/
	public abstract void canOpenPoP();
	
	/**此方法中实现多选确定按钮实现*/
	public abstract void realizeSureBtn(AbsData absData);
	
//	/**设置是否为首层，true表示为首层、false表示否*/
//	public abstract boolean isAFgtActivity();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//初始加载保存当前activity
		Contents.getInstance().setActivity(this);
		initData();
		setContentView(R.layout.activity_main);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_view_layout);
		LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view=inflater.inflate(setBaseLayout(), null);
	    LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
	    view.setLayoutParams(params);
		mainLayout.addView(view);
		baseFgtNames = setMiddleFargmentNames();
		topFgtNames = setTopFargmentNames();
		
		findView();
		
	}
	//网络错误码判断
	private void initData(){
		TLog.log("取得当前用户所管理的大棚数据");
		ParseDatas parse = DataController.getJidiDapengData();
		List<JiDi> list = null;
		if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), this)){
				list = new ArrayList<JiDi>();
			} else {
				list = (List<JiDi>)(parse.getObject());
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), this);
			list = new ArrayList<JiDi>();
		}
		AbsApplication.getInstance().setJiDiList(list);
	}
	
	//刷新界面
	public void refresh() {
		//onCreate(null);另一种刷新方法，报错
        Intent intent = new Intent(this, AiotBActivity.class);
        startActivity(intent); 
        finish(); 
	}
	
	public void refreshA() {
		mMoreDrawerList.refresh();
	}
	
	private void findView(){
		fragmentManager = getFragmentManager();
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout); 
        mSingleDrawerList = (SingleExpandableList) findViewById(R.id.left_drawer);
        mSingleDrawerList.setHostActivity(this);
        
        mMoreDrawerList = (MoreExpandableList) findViewById(R.id.right_drawer);
        mMoreDrawerList.setHostActivity(this);
        //屏蔽touch事件
        //mDrawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        onBottomClick(0);
		
	}
	
	public void setLockMode(boolean bol){
		if(bol == true){
			mDrawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		} else {
			mDrawerLayout.setDrawerLockMode( DrawerLayout.LOCK_MODE_UNLOCKED);
		}
	}
	
	public boolean isOpenedDrawer(){
		if(mDrawerLayout.isDrawerOpen(mSingleDrawerList)||mDrawerLayout.isDrawerOpen(mMoreDrawerList)){
       	 	mDrawerLayout.closeDrawer(Gravity.RIGHT);
       	 	mDrawerLayout.closeDrawer(Gravity.LEFT);
       	 	return true;
        }
		return false;
	}
	
	//根据id弹出回退堆栈中的fragment(第一个存入的fragment的id为0，然后1，。。)
	public void popFragment(int id){
		fragmentManager.popBackStack(id, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	//判断回退堆栈中的fragment数量
	public boolean isBackStackNumZero(){
		if(fragmentManager.getBackStackEntryCount()==0){
			return true;
		}
		return false;
	}
	
	public void openOrCloseSingle() {
		if(mDrawerLayout.isDrawerOpen(mSingleDrawerList)){
       	 	mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }else{
       	 	mDrawerLayout.openDrawer(Gravity.RIGHT);
        }		
	}
	
	public void openOrCloseMore() {
		if(mDrawerLayout.isDrawerOpen(mSingleDrawerList)){
			mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else{
        	mDrawerLayout.openDrawer(Gravity.LEFT);
        }		
	}
	
	public void closeDrawer(int mDir) {
    	if (mDir == 0) {
    		mDrawerLayout.closeDrawer(Gravity.RIGHT);
    	} else {
    		mDrawerLayout.closeDrawer(Gravity.LEFT);
    	} 
    }
	
	private Class getTargetClass(Intent intent){
		Class clazz = null;
		try {
			if(intent.getComponent() != null)
			clazz = Class.forName(intent.getComponent().getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}
	
	// 重写了startActivity()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivity(Intent intent) {
		if( getTargetClass(intent) != null && AbsFgtActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			intent.putExtra("fromSubActivity", getClass().getName());
			super.startActivity(intent);
		}else{
			super.startActivity(intent);
		}
	}

	// 重写了startActivityForResult()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if( getTargetClass(intent) != null && AbsFgtActivity.class.isAssignableFrom(getTargetClass(intent)) ){
			intent.putExtra("fromSubActivity", getClass().getName());
			super.startActivityForResult(intent, requestCode);
		}else{
			super.startActivityForResult(intent, requestCode);
		}
	}
		
	/** 调用此方法来返回上一个界面 */
	private void goback() {
		Class clazz = null;
		try {
			clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(this,clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		super.startActivity(intent);
		this.finish();
	}
	
	@Override
	public void onChangeThisTabViews(Fragment topFragment,
			Fragment midleFragment){
		changeTop(topFragment);
		changeMiddle(midleFragment);
	}
	
	@Override
	public void onAClick(Class<? extends AbsFgtActivity> absActivity,Bundle bundle){
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(this, absActivity);
		startActivity(intent);
//		startActivity(new Intent(this, absActivity));
	}
	
	@Override
	public void onBackClick(){
		goback();
	}
	
	@Override
	public void onBottomClick(int index){
		if(baseFgtNames[index].equals(POPWINDOW)){
			canOpenPoP();
			return;
		}
		try{
			//此处是为了解决后退栈中还存在元素时切换回标签会显示错乱的bug
			int count = fragmentManager.getBackStackEntryCount();
			if(count > 0){
				for (int i = 0; i < count; i++) {
					popFragment(i);
				}
			}
			
			Fragment topFragment = (AbsTopFgt) Class.forName(topFgtNames[index]).newInstance();
			changeTop(topFragment);
			Fragment fragment = (AbsBaseFgt) Class.forName(baseFgtNames[index]).newInstance();
			changeMiddle(fragment);
			
		}catch (Exception e){
			Log.e("WLW2", null, e);
		}
	}
	
	public Fragment getFragment(String fgtName) {
		try{
			Fragment fragment = (Fragment) Class.forName(fgtName).newInstance();
			return fragment;
		}catch (Exception e){
			System.out.println("class name err!");
		}
		return null;
	}
	
	private void changeTop(Fragment fragment){
		fragmentTransaction = fragmentManager.beginTransaction();
		//fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right); 
		fragmentTransaction.replace(R.id.fgt_top, fragment);
		//fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//设置动画效果
		fragmentTransaction.commit();//提交
	}
	
	private void changeMiddle(Fragment fragment){
		if(!checkNetwork()){
			return;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
		//fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right); 
		fragmentTransaction.replace(R.id.fgt_middle, fragment);
		//fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	public void onChangeThisTabViewsEnBack(Fragment topFragment,
			Fragment midleFragment){
		changeTop(topFragment);//不用存入队列，因为第一层没有top
		changeMiddleEnBack(midleFragment);//增加到队列，用于返回按键的调用
	}
	
	//可以通过返回键返回上一个Fragment
	private void changeMiddleEnBack(Fragment fragment){
		if(!checkNetwork()){
			return;
		}
		fragmentTransaction = fragmentManager.beginTransaction();
		//fragmentTransaction.setCustomAnimations(R.animator.slide_in_left,R.animator.slide_out_right); 
		fragmentTransaction.replace(R.id.fgt_middle, fragment);
		//fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}
	
	/**
     * 检测网络是否可用
     * @return
     */
    protected boolean checkNetwork(){
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		//网络不可用
		if(null == networkInfo || !networkInfo.isAvailable()){
			new AlertDialog.Builder(this).setMessage(R.string.nonetwork)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
					
					@Override
					public void onClick(DialogInterface dialog, int which){
						Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
						startActivity(intent);
						finish();
					}
				}).show();
			return false;
		}
		return true;
	}
	
}
