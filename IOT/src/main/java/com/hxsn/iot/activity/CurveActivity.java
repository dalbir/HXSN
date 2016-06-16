package com.hxsn.iot.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.model.Danyuan;
import com.hxsn.iot.model.Dapeng;
import com.hxsn.iot.view.CurveItemView;
import com.hxsn.iot.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class CurveActivity extends Activity implements OnClickListener,OnPageChangeListener{
	private String dpId;
	private ViewPager viewPager;
	
	private ViewPagerAdapter vpAdapter;
	
	//定义一个ArrayList来存放View
	private ArrayList<View> views;

    private ImageView[] points;
    
    //记录当前选中位置
    private int currentIndex;
    String[] tabs  ; //名称
	private List<Danyuan> listDy;
	private Dapeng dp;
	private TextView nameTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dp= AbsApplication.getInstance().getDapeng();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏 
        setContentView(R.layout.curve_main_layout);
        initView();
		initData();
		if(getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}
	
	/**
	 * 初始化组件
	 */
	private void initView(){
		nameTv = (TextView) findViewById(R.id.line_textview_name);
		
		views = new ArrayList<View>();
		
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		
		vpAdapter = new ViewPagerAdapter(views);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		//获得单元列表
		listDy = DataController.getDanyuanOfDapeng(dp.getJidiname(), dp.getName());
		getHasDyCount();
		nameTv.setText(dp.getName()+"\\"+tabs[0]);
		
        for (int i = 0; i < listDy.size(); i++) {
			String id = listDy.get(i).getId();
			CurveItemView item = new CurveItemView(this,null,id);
			views.add(item);
        } 
        
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);
        
        //初始化底部小点
        initPoint();
	}
	
	//根据传入的基地和大棚名称获得对应的单元列表
	private void getHasDyCount() {
		tabs = new String[listDy.size()];
		for (int i = 0; i < listDy.size(); i++) {
			tabs[i] = listDy.get(i).getName();
		}
	}
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);       
		
        points = new ImageView[listDy.size()];
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				  LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.rightMargin = 5;
        //循环取得小点图片
        for (int i = 0; i < listDy.size(); i++) {
        	ImageView img = new ImageView(this);
        	img.setClickable(true);
        	img.setImageResource(R.drawable.point);
        	img.setLayoutParams(mParams);
        	points[i] = img;
        	//默认都设为灰色
        	points[i].setEnabled(true);
        	//给每个小点设置监听
        	points[i].setOnClickListener(this);
        	//设置位置tag，方便取出与当前位置对应
        	points[i].setTag(i);
        	linearLayout.addView(img);
        }
        
        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
	}
	
	/**
	 * 当滑动状态改变时调用
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}
	
	/**
	 * 当当前页面被滑动时调用
	 */

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	
	/**
	 * 当新的页面被选中时调用
	 */

	@Override
	public void onPageSelected(int position) {
		//设置底部小点选中状态
        setCurDot(position);
        nameTv.setText(dp.getName()+"\\"+tabs[position]);
	}

	/**
	 * 通过点击事件来切换当前的页面
	 */
	@Override
	public void onClick(View v) {
		 int position = (Integer)v.getTag();
         setCurView(position);
         setCurDot(position);		
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position){
         if (position < 0 || position >= listDy.size()) {
             return;
         }
         viewPager.setCurrentItem(position);
     }

     /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon){
         if (positon < 0 || positon > listDy.size() - 1 || currentIndex == positon) {
             return;
         }
         points[positon].setEnabled(false);
         points[currentIndex].setEnabled(true);

         currentIndex = positon;
     }

}
