package com.yeagle.sky.lock.factory;

import com.yeagle.sky.lock.LockHelper;
import com.yeagle.sky.lock.widget.LockView;

import android.content.Context;

public interface ILockViewFactory {
	public LockView createLockView(LockHelper helper, Context context);
}
