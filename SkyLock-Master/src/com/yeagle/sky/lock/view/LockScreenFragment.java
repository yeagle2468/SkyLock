package com.yeagle.sky.lock.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yeagle.sky.lock.LockHelper;
import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.widget.NormalLockView;

public class LockScreenFragment extends Fragment {
	
//	private AnimLayoutBase mLayoutBase;
	private LockHelper mHelper;
//	private TextView mStateView;
//	private BatteryView mBatteryView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		NormalLockView lockView = new NormalLockView(getActivity());
		lockView.setId(R.id.lock_lockview);
		
		mHelper = LockHelper.getInstance();
		
//		lockView.setBetteryIcon(mBatteryView);
//		lockView.setBatteryStateTextView(mStateView);
		lockView.setLockHelper(mHelper);
//		lockView.setAnimLayoutBase(mLayoutBase);
		
		return lockView;
	}
	
//	public void setAnimLayoutBase(AnimLayoutBase base) {
//		this.mLayoutBase = base;
//	}
//	
//	public void setBatteryStateTextView(TextView textView) {
//		this.mStateView = textView;
//	}
//	
//	public void setBetteryIcon(BatteryView iv) {
//		mBatteryView = iv;
//	}
}
