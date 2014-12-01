package com.yeagle.sky.lock.factory;

import android.content.Context;
import android.view.LayoutInflater;

import com.yeagle.sky.lock.LockHelper;
import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.widget.LockView;

public class PinLockViewFactory implements ILockViewFactory {

	@Override
	public LockView createLockView(LockHelper helper, Context context) {
		return (LockView)LayoutInflater.from(context).inflate(R.layout.lock_pin_layout, null);
	}

}
