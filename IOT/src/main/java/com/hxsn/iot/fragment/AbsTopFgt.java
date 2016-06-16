package com.hxsn.iot.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hxsn.iot.R;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.activity.AiotBActivity;
import com.hxsn.iot.model.Contents;


/**
 * @author zhjx
 * 顶部Fragment类，使用时需继承此类
 */
public abstract class AbsTopFgt extends Fragment{

	private IFgtBackEventListener iBackEventListener;
	
	/**一级的返回true*/
	protected abstract boolean isATop();
	
	protected abstract boolean hideTop();
	
	protected abstract void onMenuClick();
	
	protected abstract void getTextTitle(TextView textView);
	
	public interface IFgtBackEventListener {
		
		public void onBackClick();
		
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.iBackEventListener = (IFgtBackEventListener) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.layout_top_back, container, false);
		TextView textView = (TextView) view.findViewById(R.id.text_topbar_title);
		getTextTitle(textView);
		
		Button bck_btn = (Button) view.findViewById(R.id.btn_topbar_back);
		Button menu_btn = (Button) view.findViewById(R.id.btn_topbar_menu);
		if(isATop()){//A级别的隐藏
			bck_btn.setVisibility(View.INVISIBLE);
			menu_btn.setVisibility(View.INVISIBLE);
		}
		bck_btn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				if(Contents.getInstance().getIsSingleAlarm()){
					((AbsFgtActivity)getActivity()).popFragment(0);
					Contents.getInstance().setIsSingleAlarm(false);
				} else {
					iBackEventListener.onBackClick();
				}
				
			}
		});
		menu_btn.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				//onMenuClick();调用子类方法
				((AiotBActivity) getActivity()).openOrCloseSingle();
			}
		});
		if(hideTop()){
			view.setVisibility(View.GONE);
		}
		return view;
	}
	
}
