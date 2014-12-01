package com.yeagle.sky.lock.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yeagle.sky.lock.LockHelper;
import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.adapter.LockViewAdapter;
import com.yeagle.sky.lock.factory.ILockViewFactory;
import com.yeagle.sky.lock.utils.LockUtils;
import com.yeagle.sky.lock.utils.Preferences;

public class LockScreen extends FrameLayout {
	private static final int MAX_ALPHA = 70;
	
	private LockHelper mHelper;
	private ViewPager mViewPager;
	private OnLockListener mLockListener;
	
	private ColorDrawable mForeground;
	
	public LockScreen(Context context) {
		super(context);
		
		setForeground();
		mHelper = LockHelper.getInstance();
	}

	private void setForeground() {
		final ColorDrawable foreGround = new ColorDrawable(Color.BLACK);
		foreGround.setAlpha(0);
		setForeground(foreGround);
		mForeground = foreGround;
	}
	
	public void addLockView(ILockViewFactory factory) {
		ViewPager pager = new ViewPager(getContext());
		pager.setPageMargin(30);
		mViewPager = pager;
		
		FireflyLayout flyLayout = new FireflyLayout(getContext());
		addView(flyLayout, -1, -1);
		
		mHelper.setAnimLayoutBase(flyLayout);
		LockView lockView = factory.createLockView(mHelper, getContext());
		
		LockViewAdapter adapter = new LockViewAdapter();
		View view = new View(getContext());
		
		adapter.addView(view);
		adapter.addView(lockView);
		
		pager.setAdapter(adapter);
		pager.setCurrentItem(1);
		pager.setOnPageChangeListener(mPageListener);
		addView(pager, -1, -1);
		
		mHelper.addWindowView(this, lockView);
	}
	
	public void addBetteryView() {
		int barHeight = LockUtils.getStatusBarHeight(getContext());
		Resources res = getResources();
		
		if (barHeight <= 0)
			barHeight = (int)res.getDimension(R.dimen.status_bar_height);
		
		LinearLayout linearLayout = new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		
		TextView tv = new TextView(getContext());
		int color = 0xff000000 | Preferences.getFontColor(getContext());
		tv.setTextColor(color);
		tv.setTextSize(16);
		
		// 话说临时变量使函数变得更冗长，但是这个变量体现这个值是干什么用的
		final int paddingRight = (int)res.getDimension(R.dimen.status_bar_text_right);
		tv.setPadding(0, 0, paddingRight, 0);
		linearLayout.addView(tv);
		
		final int right = (int) res.getDimension(R.dimen.status_bar_height_right);
		linearLayout.setPadding(0, 0, right, 0);
		
		BatteryView batteryView = new BatteryView(getContext());
		linearLayout.addView(batteryView);
		
		mHelper.setBatteryStateTextView(tv);
		mHelper.setBetteryIcon(batteryView);
		
		addView(linearLayout, -1, barHeight);
	}
	
	public void setOnLockListener(OnLockListener listener) {
		this.mLockListener = listener;
	}
	
	private OnPageChangeListener mPageListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			if (mLockListener != null)
				mLockListener.unLock();
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (mForeground == null)
				return;
			
			int alpha = (int)((1-positionOffset) * MAX_ALPHA);
//			final int item = mViewPager.getCurrentItem();
//			Log.e(STORAGE_SERVICE, "alpha:" + alpha + "position:" + item + ",positionOffset:" + positionOffset);
			
			if (alpha == MAX_ALPHA && mForeground.getAlpha() == 0) {
				alpha = 0;
			}
			
			mForeground.setAlpha(alpha);
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	
	public static interface OnLockListener {
		public void unLock();
	}
}
