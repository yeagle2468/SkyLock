package com.yeagle.sky.lock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
	
public class AnimLayoutBase extends View {
	public Matrix mMatrix = new Matrix();
//	Runnable b = new AnimLayoutBase.1(this);
//	Handler c = new Handler();
	
	private boolean mIsDetached;
	private boolean mIsCanceled;
	
	public AnimLayoutBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setWillNotDraw(false);
	}

	public AnimLayoutBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);
	}

	public AnimLayoutBase(Context context) {
		super(context);
		setWillNotDraw(false);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		mIsDetached = false;
//		post(mAnimRunnable);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		mIsDetached = true;
		removeCallbacks(mAnimRunnable);
		super.onDetachedFromWindow();
	}
	
	public void init() {
	}
	
	public void drawAnim(Canvas canvas) {
	}
	
	public final void startAnim() {
		mIsCanceled = false;
		Log.e(VIEW_LOG_TAG, "mIsCanceled false");
		post(mAnimRunnable);
	}
	
	public final void stopAnim() {
		mIsCanceled = true;
		Log.e(VIEW_LOG_TAG, "mIsCanceled true");
		removeCallbacks(mAnimRunnable);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawAnim(canvas);
	}

	private Runnable mAnimRunnable = new Runnable() {
		
		@Override
		public void run() {
			if (mIsDetached || mIsCanceled) 
				return;
			
			removeCallbacks(this);
			postDelayed(this, 10);
			
			invalidate();
		}
	};
}
