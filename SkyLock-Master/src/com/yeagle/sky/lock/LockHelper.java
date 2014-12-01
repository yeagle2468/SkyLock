package com.yeagle.sky.lock;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yeagle.sky.lock.utils.FireFlyTypeface;
import com.yeagle.sky.lock.utils.Preferences;
import com.yeagle.sky.lock.widget.AnimLayoutBase;
import com.yeagle.sky.lock.widget.BatteryView;
import com.yeagle.sky.lock.widget.LockView;

/**
 * 根据迪米特法则，“朋友”之间关联，所以需要中介者
 */
public class LockHelper {
	
	private WindowManager mWindowManager;
	private View mView;
	private LockView mLockView;
	public boolean mIsAddToWindow;
	private WindowManager.LayoutParams mParams;
	private Context mContext;
	
	private Handler mHandler;
	private static LockHelper mHelper;
	
	private AnimLayoutBase mLayoutBase;
	private TextView mBatteryState;
	private BatteryView mBetteryIcon;
	
	private LockHelper(Context context) {
		this.mContext = context;
		
		this.mWindowManager = ((WindowManager)context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
		mParams = new WindowManager.LayoutParams();
		
		mParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		mParams.width = -1;
		mParams.height = -1;
		mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
		
		if (Preferences.isFullScreen(context))
			mParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		else
			mParams.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
		
		mHandler = new Handler(Looper.getMainLooper());
	}
	
	public static LockHelper createInstance(Context context) {
		if (mHelper == null)
			mHelper = new LockHelper(context);
		return mHelper; 
	}
	
	public static LockHelper getInstance() {
		return mHelper; 
	}
	
//	public void initLockView(View v, LockView lockView) {
//		this.mView = v;
//		mLockView = lockView;
//	}
	
	public void animStop() {
		if (mLayoutBase != null)
			mLayoutBase.stopAnim();
		
		if (mBetteryIcon != null)
			mBetteryIcon.setCanceled(true);
		
		if (mLockView != null)
			mLockView.onStop();
	}
	
	public void animStart() {
		if (mLayoutBase != null)
			mLayoutBase.startAnim();
		
		if (mBetteryIcon != null)
			mBetteryIcon.setCanceled(false);
		
		if (mLockView != null)
			mLockView.onStart();
	}
	
	public void setCharge(boolean isCharging, float percent) {
		if (mBetteryIcon == null)
			return;
		
		if (isCharging) {
			mBetteryIcon.setCharge(true);
    		mBetteryIcon.setPowerPercent(percent);
		} else {
			mBetteryIcon.setCharge(false);
    		mBetteryIcon.setPowerPercent(percent);
		}
	}
	
	public void setAnimLayoutBase(AnimLayoutBase layoutBase) {
		this.mLayoutBase = layoutBase;
	}
	
	public void setBatteryStateTextView(TextView tv) {
		mBatteryState = tv;
		mBatteryState.setTypeface(FireFlyTypeface.mLTSTypeface);
	}
	
	public void setBetteryIcon(BatteryView iv) {
		mBetteryIcon = iv;
	}
	
	public void setBatteryPercent(String state) {
		if (mBatteryState != null)
			mBatteryState.setText(state);
	}
	
	public synchronized final void addWindowView(View v, LockView lockView) {
		if (mIsAddToWindow)
			return;
		
		if (v != null && !mIsAddToWindow) {
			if (Preferences.isFullScreen(mContext))
				mParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
			else
				mParams.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
			
			mWindowManager.addView(v, mParams);
			mView = v;
			mLockView = lockView;
		}
		mIsAddToWindow = true;
	}
	
	public final void removeWindowView() {
		if (mWindowManager != null && mIsAddToWindow) {
			mWindowManager.removeView(mView);
			Context context = mView.getContext();
			if (context instanceof Activity)
				((Activity) context).finish();
			
			mIsAddToWindow = false;
			
			String path = Preferences.getWallpaperPath(mContext);
			if (path.startsWith("picture"))
				return;
			
			Drawable drawable = mView.getBackground();
			if (drawable instanceof BitmapDrawable) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				final Bitmap bitmap = bitmapDrawable.getBitmap();
				
				if (bitmap != null && !bitmap.isRecycled()) {
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							bitmap.recycle();
						}
					}, 200);
				}
			}
		}
	}
}
