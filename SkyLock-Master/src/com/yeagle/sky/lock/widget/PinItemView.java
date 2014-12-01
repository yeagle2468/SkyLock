package com.yeagle.sky.lock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.utils.Log;

public class PinItemView extends ImageView implements OnClickListener {
	
	private static final String TAG = PinItemView.class.getName();
	
	private int mNormalColor, mTouchedColor;
	private CharSequence mText;
	private Paint mPaint;
	private boolean mIsTouch;
	
	public PinItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PinItemView);
//		mNormalColor = a.getInt(R.styleable.SkyTouch_normal_color, android.R.color.transparent);
		mTouchedColor = a.getInt(R.styleable.PinItemView_bg_color, android.R.color.white);
		mText = a.getText(R.styleable.PinItemView_text);
		
		Log.e(TAG, "mTouchedColor:" + mTouchedColor);
		Log.e(TAG, "mText:" + mText);
		
//		mTouchedColor = Color.WHITE;
		
		a.recycle();
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextSize(28);
		setOnClickListener(this);
	}

	public PinItemView(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mIsTouch = true;
			invalidate();
//			Log.e(VIEW_LOG_TAG, "ACTION_DOWN");
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mIsTouch = false;
			invalidate();
//			Log.e(VIEW_LOG_TAG, "ACTION_UP");
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mPaint.setColor(mTouchedColor);
		
		final int radius = getMeasuredWidth()/2;
		
		if (mText != null) {
			final int length = mText.length();
			final float textWidth = mPaint.measureText(mText, 0, length);
			float ascent = mPaint.getFontMetrics().ascent;
			float y = (getMeasuredHeight() - ascent)/2;
			
			canvas.drawText(mText, 0, length, radius-textWidth/2, y, mPaint);
		} else {
			if (mIsTouch)
				mPaint.setStyle(Style.FILL);
			else
				mPaint.setStyle(Style.STROKE);
			
			canvas.drawCircle(radius, radius, radius, mPaint);
		}
	}

	@Override
	public void onClick(View v) {
		
	}
	
	
}
