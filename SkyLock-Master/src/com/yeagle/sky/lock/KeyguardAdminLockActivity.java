package com.yeagle.sky.lock;

import com.yeagle.sky.lock.utils.DevicePolicyUtils;

import android.app.Activity;
import android.os.Bundle;

public class KeyguardAdminLockActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		lockNow();
		finish();
	}

	private void lockNow() {
		DevicePolicyUtils policyUtils = DevicePolicyUtils.getInstance();
		boolean flag = policyUtils.isAdminActive();
		
		if (flag) 
			policyUtils.lockNow();
		else
			policyUtils.activeManger(this);
	}
	
}
