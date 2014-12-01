package com.yeagle.sky.lock.factory;

import android.content.Context;

import com.yeagle.sky.lock.LockHelper;
import com.yeagle.sky.lock.widget.LockView;
import com.yeagle.sky.lock.widget.NormalLockView;

public class NormalLockViewFactory implements ILockViewFactory {

	@Override
	public LockView createLockView(LockHelper helper, Context context) {
		NormalLockView view = new NormalLockView(context);
		view.setLockHelper(helper);
		
		return view;
	}

}
