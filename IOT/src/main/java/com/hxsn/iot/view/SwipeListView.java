package com.hxsn.iot.view;

//import android.annotation.SuppressLint;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxsn.iot.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SwipeListView extends ListView implements OnScrollListener {
	//起始点y值
	private int startY;
	private int state;
	private int headerHeight;
	
	//list动作常量
	public static final int NONE = 0;
	public static final int PULL = 1;
	public static final int RELASE = 2;
	public static final int LOAD = 3;
	public static final int UP = 4;
	public static final int DOWN = 5;
	
	private OnloadListner listner;
	private boolean refresh;
	
	private TextView tip;
	private TextView lastUpdate;
	private ImageView arrow;
	private ProgressBar refreshing;
	
	private int pullHeight;
	private int direct;
	
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	
	private View header;
	private View footer;
	
	private int visibleItemCount;
	private int totalItemCount;
	private int firstVisibleItem;
	private int scrollState;
	
	private LayoutInflater inflater;
	
    private Boolean mIsHorizontal;

    private View mPreItemView;

    private View mCurrentItemView;

    private float mFirstX;

    private float mFirstY;

    private int mRightViewWidth;

    // private boolean mIsInAnimation = false;
    private final int mDuration = 100;

    private final int mDurationStep = 10;

    private boolean mIsShown;

    public SwipeListView(Context context) {
        this(context,null);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle); 
      TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.swipelistviewstyle);
      //获取自定义属性和默认值  
      mRightViewWidth = (int) mTypedArray.getDimension(R.styleable.swipelistviewstyle_right_width, 200);   
      mTypedArray.recycle();  
      
      inflater = LayoutInflater.from(context);
      initView();
      this.setOnScrollListener(this);
      
   // 设置箭头特效
   		animation = new RotateAnimation(0, -180,
   				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
   				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
   		animation.setInterpolator(new LinearInterpolator());
   		animation.setDuration(100);
   		animation.setFillAfter(true);

   		reverseAnimation = new RotateAnimation(-180, 0,
   				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
   				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
   		reverseAnimation.setInterpolator(new LinearInterpolator());
   		reverseAnimation.setDuration(100);
   		reverseAnimation.setFillAfter(true);
    }

    /**
     * return true, deliver to listView. return false, deliver to child. if
     * move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int)mFirstX, (int)mFirstY);

                if (motionPosition >= 0) {
                    View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            	Log.i("aaaa", "move");
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;
                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                System.out.println("onInterceptTouchEvent----->ACTION_UP");
                if (mIsShown && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                    System.out.println("1---> hiddenRight");
                    /**
                     * 情况一：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }

    /**
     * @param dx
     * @param dy
     * @return judge if can judge scroll direction
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;
        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
        } else {
            canJudge = false;
        }
        return canJudge;
    }

    /**
     * return false, can't move any direction. return true, cant't move
     * vertical, can move horizontal. return super.onTouchEvent(ev), can move
     * both.
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	startY = (int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;
                // confirm is scroll direction
                if (mIsHorizontal == null) {
                    if (!judgeScrollDirection(dx, dy)) {
                        break;
                    }
                }
                if (mIsHorizontal) {
                    if (mIsShown && mPreItemView != mCurrentItemView) {
                        System.out.println("2---> hiddenRight");
                        /**
                         * 情况二：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
                         * <p>
                         * 向左滑动只触发该情况，向右滑动还会触发情况五
                         */
                        hiddenRight(mPreItemView);
                    }

                    if (mIsShown && mPreItemView == mCurrentItemView) {
                        dx = dx - mRightViewWidth;
                        System.out.println("======dx " + dx);
                    }
                    // can't move beyond boundary
                    if (dx < 0 && dx > -mRightViewWidth) {
                        mCurrentItemView.scrollTo((int)(-dx), 0);
                    }
                    return true;
                } else {
                    if (mIsShown) {
                        System.out.println("3---> hiddenRight");
                        /**
                         * 情况三：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                         */
                        hiddenRight(mPreItemView);
                    }
                    if(!refresh){
                    	move(ev);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            	if(Math.abs(startY-ev.getY())>20){
            		relase(ev);
            	}else{
            		//恢复界面
            	}
            	//break;
            case MotionEvent.ACTION_CANCEL:
                clearPressedState();
                if (mIsShown) {
                    /**
                     * 情况四：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }
                if (mIsHorizontal != null && mIsHorizontal) {
                    if (mFirstX - lastX > mRightViewWidth / 2) {
                        showRight(mCurrentItemView);
                    } else {
                        /**
                         * 情况五：
                         * <p>
                         * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                         */
                        hiddenRight(mCurrentItemView);
                    }
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void clearPressedState() {
    	if(mCurrentItemView==null){
    		return;
    	}
        mCurrentItemView.setPressed(false);
        setPressed(false);
        refreshDrawableState();
        // invalidate();
    }

    private void showRight(View view) {
        Message msg = new MoveHandler().obtainMessage();
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = mRightViewWidth;
        msg.sendToTarget();
        mIsShown = true;
    }

    private void hiddenRight(View view) {
        if (mCurrentItemView == null) {
            return;
        }
        Message msg = new MoveHandler().obtainMessage();//
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = 0;
        msg.sendToTarget();
        mIsShown = false;
    }

    /**
     * show or hide right layout animation
     */
   // @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;
        int fromX;
        int toX;
        View view;
        private boolean mIsInAnimation = false;
        private void animatioOver() {
            mIsInAnimation = false;
            stepX = 0;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View)msg.obj;
                fromX = msg.arg1;
                toX = msg.arg2;
                stepX = (int)((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
                    animatioOver();
                    return;
                }
            }
            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX) || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }
            view.scrollTo(fromX, 0);
            invalidate();

            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }
    }

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }
    
    //初始化list header footer view 
    public void initView(){
    	header = inflater.inflate(R.layout.pull_to_refresh_header, null);
    	measureView(header);
    	headerHeight = header.getMeasuredHeight();
    	header.setPadding(header.getPaddingLeft(), -headerHeight, header.getPaddingRight(), header.getPaddingBottom());
    	arrow = (ImageView) header.findViewById(R.id.arrow);
		tip = (TextView) header.findViewById(R.id.tip);
		lastUpdate = (TextView) header.findViewById(R.id.lastUpdate);
		refreshing = (ProgressBar) header.findViewById(R.id.refreshing);
		
    	footer = inflater.inflate(R.layout.listview_footer, null);
    	this.addHeaderView(header);
    	//this.addFooterView(footer);
    }
    
    //刷新头部list header显示padding
    public void refreashTop(int topPadding){
    	header.setPadding(header.getPaddingLeft(), topPadding-headerHeight, header.getPaddingRight(), header.getPaddingBottom());
    	chanageTip();
    }
    
    public void refreashBottom(int bottomPadding){
    	footer.setPadding(footer.getPaddingLeft(), footer.getPaddingTop(), footer.getPaddingRight(), bottomPadding);
    }
    
    // 用来计算header大小的。比较隐晦。因为header的初始高度就是0,貌似可以不用。
 	private void measureView(View view ) {
 		int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
 		int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
 		view.measure(width,height);
 	}
 	
 	public void move(MotionEvent ev){
 		//判断是上拉还是下拉
 		int distance = (int)(ev.getY() - startY);
 		if(distance>0){
 			this.direct = DOWN;
 		}else{
 			this.direct = UP;
 		}
 		//判断是否需要显示刷新提示
 		if(this.direct==DOWN&&this.firstVisibleItem==0){
 			//下拉刷新操作
 			refreashTop(distance);
 			return ;
 		}
 		if(this.direct==UP&&(this.totalItemCount<=this.firstVisibleItem+this.visibleItemCount)){
 			//上拉刷新操作
 			if(this.getFooterViewsCount()==0){
 				this.addFooterView(footer);
 			}
 			refreashBottom(Math.abs(distance));
 		}
 	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		this.scrollState = scrollState;
	}
    
	public interface OnloadListner{
		public void loadMore();
		public void loadNew();
	}

	public OnloadListner getListner() {
		return listner;
	}

	public void setListner(OnloadListner listner) {
		this.listner = listner;
	}
	
	public void chanageTip(){
		if(!this.refresh){
			int paddingTop= header.getPaddingTop();
			if(paddingTop>0){
				refreshing.setVisibility(View.GONE);
				tip.setText(R.string.release_to_refresh);
				arrow.clearAnimation();
				arrow.setAnimation(reverseAnimation);
				return ;
			}else{
				arrow.setVisibility(View.VISIBLE);
				tip.setVisibility(View.VISIBLE);
				lastUpdate.setVisibility(View.VISIBLE);
				tip.setText(R.string.pull_to_refresh);
				refreshing.setVisibility(View.GONE);
				arrow.clearAnimation();
				arrow.setAnimation(animation);
				return;
			}
		}else{
			refreshing.setVisibility(View.VISIBLE);
			arrow.clearAnimation();
			arrow.setVisibility(View.GONE);
			tip.setVisibility(View.GONE);
			lastUpdate.setVisibility(View.GONE);
			header.setPadding(header.getPaddingLeft(), 0, header.getPaddingRight(), header.getPaddingBottom());
		}
	}
	
	public void relase(MotionEvent ev){
		if(this.refresh){
			return ;
		}else{
			this.refresh = true;
		}
		if(this.direct==UP){
			footer.setPadding(footer.getPaddingLeft(), footer.getPaddingTop(), footer.getPaddingRight(), 0);
			listner.loadMore();
		}
		if(this.direct==DOWN){
			if(header.getPaddingTop()>0){
				listner.loadNew();
			}else{
				onLoadNewCompleted();
			}
			chanageTip();
		}
	}
	
	//加载更多完成
	public void onLoadMoreCompleted(){
		this.state = NONE;
		this.refresh = false;
		this.removeFooterView(footer);
	}
	
	//加载最新完成
	public void onLoadNewCompleted(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		lastUpdate.setText(this.getContext().getString(R.string.lastUpdateTime,
				sdf.format(new Date())));
		this.state = NONE;
		this.refresh = false;
		header.setPadding(header.getPaddingLeft(), -headerHeight, header.getPaddingRight(), header.getPaddingBottom());
		chanageTip();
	}
	
}
