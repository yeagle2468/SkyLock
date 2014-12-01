package com.yeagle.sky.lock.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.utils.Log;

public class BatteryView extends View {
	private Bitmap mBit, mBg, mChargeIcon, mBatteryIcon;
	private int mLeft, mTop, mWidth, mHeight, mChargeLeft;
	private boolean mCharging = false;
	private boolean mIsDestroy, mIsCanceled;
	private float mBetteryPos;
	
	private float mPercent;
	
	public BatteryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mBg = ((BitmapDrawable)getResources().getDrawable(R.drawable.battery_bg)).getBitmap();
		Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.battery_full)).getBitmap();
		mBatteryIcon = bitmap;
		
		mChargeIcon = ((BitmapDrawable)getResources().getDrawable(R.drawable.battery_charging_icon)).getBitmap();
		
		mHeight = mBg.getHeight();
		mWidth = mBg.getWidth() + (int)(getResources().getDisplayMetrics().density * 1);
		int w = mBg.getWidth() - bitmap.getWidth();
		int h = mBg.getHeight() - bitmap.getHeight();
		
		mLeft = (int)(0.38f * w);
		mChargeLeft = (int) (mWidth * 0.38f);
		mTop = (int) (0.51f * h);
	}

	public BatteryView(Context context) {
		this(context, null);
	}
	
	public void setPowerPercent(float percent) {
		mPercent = percent;
		if (mBit != null && !mBit.isRecycled())
			mBit.recycle();
		
		if (percent >= 1) {
			invalidate();
			return;
		}
		
		perBitmapGenerate(percent);
		
		if (!mCharging)
			invalidate();
		
		Log.e(VIEW_LOG_TAG, "ss");
	}

	private void perBitmapGenerate(float percent) {
		Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.battery_full)).getBitmap();
		int w = (int) (bitmap.getWidth() * percent);
		if (w == 0)
			return;
		
		int h = bitmap.getHeight();
		mBit = Bitmap.createBitmap(bitmap, 0, 0, w, h);
	}
	
	public void setCharge(boolean charging) {
		if (mCharging != charging) {
			mCharging = charging;
			if (charging) {
				post(mRunnable);
			} else {
				if (mBetteryPos != mPercent) {
					if (mBit != null && !mBit.isRecycled())
						mBit.recycle();
					
					mBetteryPos = mPercent;
					perBitmapGenerate(mPercent);
					
					invalidate();
				}
				removeCallbacks(mRunnable);
			}
		}
	}
	
	public void setCanceled(boolean flag) {
		mBetteryPos = 1.0f;
		if (flag) {
			mIsCanceled = true;
			removeCallbacks(mRunnable);
		} else {
			mIsCanceled = false;
			post(mRunnable);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mPercent <= 0)
			return;
		
		int t = (getMeasuredHeight() - mHeight)/2;
		
		if (mBg != null && !mBg.isRecycled())
			canvas.drawBitmap(mBg, 0, t, null);
		
		if (mPercent >= 1 && mBatteryIcon != null && !mBatteryIcon.isRecycled()) {
			canvas.drawBitmap(mBatteryIcon, mLeft, mTop+t, null);
		} else if (mBit != null && !mBit.isRecycled())
			canvas.drawBitmap(mBit, mLeft, mTop+t, null);
		
		if (mCharging) {
			if (mPercent < 1 && mBetteryPos>=1 && mBatteryIcon != null && !mBatteryIcon.isRecycled())
				canvas.drawBitmap(mBatteryIcon, mLeft, mTop+t, null);
			
			if (mChargeIcon != null && !mChargeIcon.isRecycled())
				canvas.drawBitmap(mChargeIcon, mChargeLeft, mTop+t, null);
		}
	}
	

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		Log.e(VIEW_LOG_TAG, "battery:" + getHandler());
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
		mIsDestroy = true;
		removeCallbacks(mRunnable);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mIsDestroy || mIsCanceled || !mCharging)
				return;
			
			removeCallbacks(this);
			if (mBetteryPos >= 1)
				mBetteryPos = mPercent;
			else
				mBetteryPos += 0.2f;
			
			if (mBetteryPos < 1) {
				if (mBit != null && !mBit.isRecycled())
					mBit.recycle();
				perBitmapGenerate(mBetteryPos);
			}
			
			invalidate();
			postDelayed(this, 1000);
		}
	};
}
