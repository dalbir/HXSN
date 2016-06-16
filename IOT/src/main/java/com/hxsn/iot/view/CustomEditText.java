package com.hxsn.iot.view;

import android.R;
import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

public class CustomEditText extends EditText{

	public CustomEditText(Context context) {
		super(context);
	}
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onDraw(Canvas canvas){
		int lineHeight=this.getLineHeight();
		Paint mPaint=getPaint();
		mPaint.setColor(getResources().getColor(R.color.transparent));//文本编辑线
		int topPadding=this.getPaddingTop();
		int leftPadding=this.getPaddingLeft();
		float textSize=getTextSize();
		setGravity(Gravity.LEFT|Gravity.TOP);
		int y =(int)(topPadding+textSize);
		for(int i=0;i<getLineCount();i++){
			canvas.drawLine(leftPadding, y+2, getRight()-leftPadding, y+2, mPaint);
			y+=lineHeight;
		}
		canvas.translate(0, 0);
		super.onDraw(canvas);
	}

}
