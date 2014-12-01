package com.yeagle.sky.lock.adapter;

import java.util.ArrayList;

//import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class LockViewAdapter extends PagerAdapter {
	private final ArrayList<View> mListViews = new ArrayList<View>();
//	private JazzyViewPager mViewPager;
	
	public void addView(View view) {
		mListViews.add(view);
		notifyDataSetChanged();
	}
	
//	public void setViewPager(JazzyViewPager viewPager) {
//		this.mViewPager = viewPager;
//	}
	
	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
		
		if (mListViews.contains(arg2))
			mListViews.remove(arg2);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
//		if (mViewPager != null)
//			mViewPager.setObjectForPosition(mListViews.get(position), position);
		
		((ViewPager) container).addView(mListViews.get(position), 0);
		return mListViews.get(position);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public int getItemPosition(Object object) {
		boolean isContains = mListViews.contains(object);
		if (isContains)
			return POSITION_UNCHANGED;
		else
			return POSITION_NONE;
	}

}
