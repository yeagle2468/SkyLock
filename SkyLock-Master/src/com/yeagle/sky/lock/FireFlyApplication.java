package com.yeagle.sky.lock;

import android.app.Application;
import android.content.Intent;

import com.yeagle.sky.lock.utils.DevicePolicyUtils;
import com.yeagle.sky.lock.utils.FireFlyTypeface;
import com.yeagle.sky.lock.utils.Preferences;

public class FireFlyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		FireFlyTypeface.createLightTypeface(getAssets());
		FireFlyTypeface.createLSTTypeface(getAssets());
		DevicePolicyUtils.createInstance(this);
		LockHelper.createInstance(getApplicationContext());
		
		if (Preferences.isLockEnable(getApplicationContext())) {
			Intent service = new Intent(this, KeyguardService.class);
//			service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startService(service);
		}
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		
	}
	
	
}
