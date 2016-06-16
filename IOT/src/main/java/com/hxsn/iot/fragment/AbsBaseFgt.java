package com.hxsn.iot.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.hxsn.iot.activity.AbsFgtActivity;

/**
 * @author zhjx
 * 中间部ragment类，使用时需继承此类
 */
public abstract class AbsBaseFgt extends Fragment{

	private IFgtAEventListener iAEventListener;
	
//	protected abstract AbsFgtActivity setFgtActivity();
	
//	/**初始化Layout*/
//	protected abstract int setCreateView();
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.iAEventListener = (IFgtAEventListener) activity;
	}
	
	/**
	 * @param topFragment 顶部标签 
	 * @param midleFragment 中间部标签
	 * 改变此标签的view视图
	 */
	public void changTabFragments(Fragment topFragment, Fragment midleFragment){
		iAEventListener = (IFgtAEventListener) getActivity();
		iAEventListener.onChangeThisTabViews(topFragment, midleFragment);
	}
	
	/**
	 * @param absActivity 继承AbsFgtActivity的Activity
	 * 跳转到下一级FgtActivity中
	 */
	public void startInAbsFgtActivity(Class<? extends AbsFgtActivity> absActivity ,Bundle bundle){
		//在新的AbsFgtActivity中的TopFragment上加back事件。
		iAEventListener =(IFgtAEventListener) getActivity();
		iAEventListener.onAClick(absActivity ,bundle);
	}
	
	public interface IFgtAEventListener {
		
		/**
		 * @param absActivity 下一层级AbsFgtActivity
		 * 进入下一层级
		 */
		public void onAClick(Class<? extends AbsFgtActivity> absActivity, Bundle bundle);
		
		/**
		 * @param topFragment 顶部Fragment
		 * @param midleFragment 内容Fragment
		 * 改变此标签的视图
		 */
		public void onChangeThisTabViews(Fragment topFragment, Fragment midleFragment);
		
	}
	
}
