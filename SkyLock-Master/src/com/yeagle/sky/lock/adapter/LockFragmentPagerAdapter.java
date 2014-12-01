package com.yeagle.sky.lock.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

public class LockFragmentPagerAdapter extends FragmentPagerAdapter {
	private Context mContext;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	
	public LockFragmentPagerAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
	}
	
	public void addTab(Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        mTabs.add(info);
        notifyDataSetChanged();
    }
	
	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}
	
	public static final class TabInfo {
        private final Class<?> clss;
        private final Bundle args;

        TabInfo(Class<?> _class, Bundle _args) {
            clss = _class;
            args = _args;
        }
    }
}
