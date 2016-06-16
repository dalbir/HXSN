package com.hxsn.town;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.hxsn.town.fragment.CircumFragment;
import com.hxsn.town.fragment.HomePageFragment;
import com.hxsn.town.fragment.MyPageFragment;
import com.hxsn.town.fragment.ParkPageFragment;
import com.hxsn.town.fragment.ShopCartFragment;
import com.hxsn.town.view.CustomTabHost;


/*
 * 程序主界面
 */
public class MainActivity extends FragmentActivity{
	
	private CustomTabHost mTabHost;
	private LayoutInflater inflater;
	//定义数组来存放Fragment界面  
    private Class fragmentArray[] = {HomePageFragment.class,ParkPageFragment.class,CircumFragment.class,ShopCartFragment.class,MyPageFragment.class};
      
    //定义数组来存放按钮图片  
    private int mImageViewArray[] = {R.drawable.bottom_home,R.drawable.bottom_park,R.drawable.bottom_circum,  
                                     R.drawable.bottom_shopcart,R.drawable.bottom_mypage};  
      
    //Tab选项卡的文字  
    private String mTextviewArray[] = {"首页", "园区", "周边", "购物车", "我"};  

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		initView();
//		startService(new Intent(this,PushService.class));
	}
	
	/** 
     * 初始化组件 
     */  
    private void initView(){  
        //实例化布局对象  
    	inflater = LayoutInflater.from(this);  
                  
        //实例化TabHost对象，得到TabHost  
        mTabHost = (CustomTabHost)findViewById(android.R.id.tabhost);  
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);   
          
        //得到fragment的个数  
        int count = fragmentArray.length;     
                  
        for(int i = 0; i < count; i++){    
            //为每一个Tab按钮设置图标、文字和内容  
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));  
            //将Tab按钮添加进Tab选项卡中  
            mTabHost.addTab(tabSpec, fragmentArray[i], null);  
            //设置Tab按钮的背景  
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background); 
            
            mTabHost.getTabWidget().getChildAt(i).setOnClickListener(new TabChildListener(i));
        }  
        mTabHost.setOnTabChangedListener(new OnTabChangedListener());
        updateTab(mTabHost);
    }  
    
    //切换选项卡
    public void setCurrentTab(int index){
    	mTabHost.setCurrentTab(index);
    	updateTab(mTabHost);
    }
                  
    /** 
     * 给Tab按钮设置图标和文字 
     */  
    private View getTabItemView(int index){  
        View view = inflater.inflate(R.layout.main_tab_item_view, null);  
      
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);  
        imageView.setImageResource(mImageViewArray[index]);  
          
        TextView textView = (TextView) view.findViewById(R.id.textview);          
        textView.setText(mTextviewArray[index]);  
      
        return view;  
    }  
    
    private class TabChildListener implements OnClickListener{
    	private int index;
    	public TabChildListener(int index){
    		this.index = index;
    	}

		@Override
		public void onClick(View v) {
			updateTab(mTabHost);
			if(mTabHost.getTabWidget().getChildAt(index).isSelected()){
				switch (index) {
				case 0:
					HomePageFragment.goToHome();
					break;
				case 1:
					ParkPageFragment.goToHome();
					break;
				case 3:
					ShopCartFragment.goToHome();
					break;
				case 4:
					MyPageFragment.goToHome();
					break;
				default:
					break;
				}
			}else {
				mTabHost.setCurrentTab(index);
			}
		}
    	
    }
    
    //监听没有fragment返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	boolean tag = false;
    	if(keyCode == event.KEYCODE_BACK){
    		int num = mTabHost.getCurrentTab();
    		switch (num) {
			case 0:
				tag = HomePageFragment.onKeyDown(keyCode, event);
				break;
			case 1:
				tag = ParkPageFragment.onKeyDown(keyCode, event);
				break;
			case 2:
				tag = CircumFragment.onKeyDown(this);
				break;
			case 3:
				tag = ShopCartFragment.onKeyDown(keyCode, event);
				break;
			case 4:
				tag = MyPageFragment.onKeyDown(keyCode, event);
				break;
			default:
				break;
			}
    	}
    	if(tag == false){
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    private class OnTabChangedListener implements OnTabChangeListener { 
    	 
        @Override 
        public void onTabChanged(String tabId) { 
            updateTab(mTabHost); 
        } 
    } 
    
    /**
     * 更新Tab标签文字的颜色
     * @param tabHost
     */ 
    private void updateTab(CustomTabHost tabHost) { 
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) { 
            View view = tabHost.getTabWidget().getChildAt(i); 
            TextView tv = (TextView) view.findViewById(R.id.textview); 
            if (tv == null){
                continue;
            }
            if (tabHost.getCurrentTab() == i) {//选中  
                tv.setTextColor(this.getResources().getColorStateList( 
                        R.color.app_bottom_press)); 
            } else {//不选中  
                tv.setTextColor(this.getResources().getColorStateList( 
                        R.color.app_bottom_defalut)); 
            } 
        } 
    }

}
