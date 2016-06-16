package com.hxsn.iot.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hxsn.iot.R;
import com.hxsn.iot.model.Contents;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.HashMap;
import java.util.Map;


/**
 * @author zhjx
 * 底部Fragment抽象类，需继承此类
 */
public abstract class AbsBottomFgt extends Fragment{
	
	/** 选项卡 */
	private RadioGroup radioGroup = null;
	
	/** 选中标签的ID */
	private int radioGroupCheckId;
	
//	/** 切换标签动作的标志位 */
//	private boolean changedFlag;
	
	/** 选项卡的所有标签 */
	private RadioButton[] radioButtons = null;
	
	/** 选项卡所有标签的ID */
	private int[] radioButtonIds = null;
	
	/** 标签ID对应的初始index坐标集合 */
	private Map<Integer, Integer> indexs = new HashMap<Integer, Integer>();
	
	/**初始化Layout*/
	protected abstract int setCreateView();
	
	/** 在子类中需要实现的获取选项卡所有标签的ID的方法 */
	protected abstract int[] getRadioButtonIds();
	
	protected abstract boolean isMainBottom();
	
	private View bottomView;
	
	private IFgtBottomListener iBottomListener;
	
	private Button buttonBad;
	
	private BadgeView badge;
	
	private int num;
	
	/**
	 * @author zhjx
	 * 底部点击事件监听
	 */
	public interface IFgtBottomListener {
		
		/**
		 * @param isMeun 是否为menu扩展菜单
		 * @param fragment 
		 */
//		public void onBottomClick(boolean isMenu, int index);
		
		/**
		 * @param index 下标0 开始， 0对应第一个tab
		 */
		public void onBottomClick(int index);
		
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		this.iBottomListener = (IFgtBottomListener) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		bottomView = inflater.inflate(setCreateView(), container, false);
		
		setDatas();

		initWidgetStatic();

		return bottomView;
	}
	
	private void setDatas(){
		radioButtonIds = getRadioButtonIds();
		
		for(int i=0;i<radioButtonIds.length;i++){
			indexs.put(radioButtonIds[i], i);
		}
	}
	
	/** 初始化控件 */ 
	protected void initWidgetStatic(){
		radioGroup = (RadioGroup) bottomView.findViewById(R.id.activity_group_radioGroup);
		radioButtons = new RadioButton[radioButtonIds.length];
		//判断是否有新报警数据
		num = Contents.getInstance().getAlarmCount();
//		num = 12;//测试
		
		if(num != 0){
			buttonBad = (Button) bottomView.findViewById(R.id.bt);
			remind(buttonBad);
		}
		for(int i=0;i<radioButtons.length;i++){
			final int tag = i;
			radioButtons[i] = (RadioButton) bottomView.findViewById(radioButtonIds[i]);
			radioButtons[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					iBottomListener.onBottomClick(fragmentTypes.get(radioGroupCheckId), classes.get(radioGroupCheckId));
					System.out.println(indexs.get(radioGroupCheckId));
					iBottomListener.onBottomClick(indexs.get(radioGroupCheckId));
					if(badge != null && badge.isShown()&&tag==1){
						badge.hide();
					}
				}
			});
		}
		
		// 给选项卡设定监听
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				radioGroupCheckId = checkedId;
				System.out.println("checked"); 
			}
		});
		
		// 初始化加载
		radioGroupCheckId = getCheckedRadioButtonId();
		
	}
	//在界面添加BadgeView ，报警信息提示
	private void remind(View view) { 
		badge = new BadgeView(getActivity(), view);// 创建一个BadgeView对象，view为你需要显示提醒的控件
		badge.setText(""+num); // 需要显示的提醒类容
		badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
		badge.setTextColor(Color.WHITE); // 文本颜色
		badge.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
		badge.setTextSize(12); // 文本大小
		//badge.setBadgeMargin(3, 3); // 水平和竖直方向的间距
		badge.setBadgeMargin(5); //各边间隔
		// badge.toggle(); //显示效果，如果已经显示，则影藏，如果影藏，则显示
		badge.show();// 只有显示
		// badge.hide();//影藏显示
	}
	
	/** 这个方法用于获取当前ActivityGroup的选项卡按下的单选按钮的ID */  
	public int getCheckedRadioButtonId(){
		return radioGroup.getCheckedRadioButtonId();
	}
	
}
