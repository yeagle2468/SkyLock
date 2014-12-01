package com.yeagle.sky.lock.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.yeagle.sky.lock.R;

public class SlidingTextView extends View {
	private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private int mTextColor;
	private char[] mText;
	private int mTextLength;
	
	private int mCount;
	private int mNormalColor, mColor1, mColor2, mColor3;
	
	private boolean mIsFinished = false;
	private boolean mIsStop = true;
	private boolean mRefresh = false;
	
	private int mPaddingBottom;
	
	public SlidingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		final Resources res = getResources();
        
		float density = res.getDisplayMetrics().density;
		
        mTextPaint.density = density;
        mPaddingBottom = (int)(density * 2);
	}

	public SlidingTextView(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(Color.TRANSPARENT);
		float x = 0;
//		FontMetrics fm = mTextPaint.getFontMetrics();  
//		int h =  (int) Math.ceil(fm.descent - fm.top);
		float y = getMeasuredHeight() - mPaddingBottom; //
		
		if (mIsStop) {
			mTextPaint.setColor(mNormalColor);
			canvas.drawText(mText, 0, mText.length, x, y, mTextPaint);
			return;
		} 
		
		if (mText == null || mText.length == 0)
			return;
		
		if (mCount >= mTextLength)
			mCount = 0;
		
		int index = 0;
		final int countM2 = mCount - 2;
		final int countM1 = mCount - 1;
		
		if (countM1 >= 0) {
			if (countM2 >= 0) {
				if (countM2 > 0) {
					mTextPaint.setColor(mNormalColor);
					canvas.drawText(mText, index, countM2, x, y, mTextPaint);
					x += mTextPaint.measureText(mText, index, countM2);
					index += countM2;
				}
				
				mTextPaint.setColor(mColor1);
				canvas.drawText(mText, index, 1, x, y, mTextPaint);
				
				x += mTextPaint.measureText(mText, index, 1);
				index += 1;
			}
			
			mTextPaint.setColor(mColor2);
			canvas.drawText(mText, index, 1, x, y, mTextPaint);
			
			x += mTextPaint.measureText(mText, index, 1);
			index += 1;
		}
		
		mTextPaint.setColor(mColor3);
		canvas.drawText(mText, index, 1, x, y, mTextPaint);
		
		x += mTextPaint.measureText(mText, index, 1);
		index += 1;
		
		final int count1 = mCount + 1;
		final int count2 = mCount + 2;
		if (count1 < mTextLength) {
			mTextPaint.setColor(mColor2);
			canvas.drawText(mText, index, 1, x, y, mTextPaint);
			
			x += mTextPaint.measureText(mText, index, 1);
			index += 1;
			
			if (count2 < mTextLength) {
				mTextPaint.setColor(mColor1);
				canvas.drawText(mText, index, 1, x, y, mTextPaint);
				
				x += mTextPaint.measureText(mText, index, 1);
				index += 1;
				
				if (count2 < (mTextLength-1)) {
					mTextPaint.setColor(mNormalColor);
					
					final int count = mTextLength - 1 - count2;
					canvas.drawText(mText, index, count, x, y, mTextPaint);
				}
			}
		}
		
		if (mRefresh) {
			mCount++;
			mRefresh = false;
		}
	}
	
	public final void animStart() {
		this.mIsStop = false;
		removeCallbacks(mRunnable);
		post(mRunnable);
	}
	
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mIsStop || mIsFinished)
				return;
			
			mRefresh = true;
			invalidate();
			postDelayed(this, 200L);
		}
	};
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mText != null) {
			int w = (int)Math.ceil(mTextPaint.measureText(mText, 0, mText.length));
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY);
		
			FontMetrics fm = mTextPaint.getFontMetrics();  
			int h =  (int) (Math.ceil(fm.descent - fm.top) + 2*getResources().getDisplayMetrics().density);
			
			Log.e(VIEW_LOG_TAG, "niw:" + w + ",h:" + h);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public final void animStop() {
		this.mIsStop = true;
		removeCallbacks(this.mRunnable);
	}
	
	protected void onAttachedToWindow() {
		this.mIsFinished = false;
//		animStart();
	}

	protected void onDetachedFromWindow() {
		this.mIsFinished = true;
//		removeCallbacks(this.mRunnable);
		animStop();
		super.onDetachedFromWindow();
	}

	public void setSTypeface(Typeface typeface) {
		mTextPaint.setTypeface(typeface);
	}
	
	/**
	 * 不包括alpha部分
	 * @param color
	 */
	public void setTextColor(int color) {
		mTextColor = color & 0x00ffffff;
		mNormalColor = 0x33000000 | mTextColor;
		mColor1 = 0x58000000 | mTextColor;
		mColor2 = 0xa8000000 | mTextColor;
		mColor3 = 0xff000000 | mTextColor;
	}
	
	public void setText(String text) {
		if (text == null) {
			mText = null;
			mTextLength = 0;
			
			return;
		} 
		
		mTextLength = text.length();
		mText = text.toCharArray();
	}
	
	public void setDefaultText() {
		String text = getResources().getString(R.string.lock_slide_to_unlock);
		setText(text);
	}
	
	public void setTextSize(float size) {
		setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	}
	
	public void setTextSize(int unit, float size) {
		final float textSize = TypedValue.applyDimension(
	            unit, size, getResources().getDisplayMetrics());
		
		mTextPaint.setTextSize(textSize);
	}
}
