package com.yeagle.sky.lock.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.yeagle.sky.lock.KeyguardDeviceAdminReceiver;
import com.yeagle.sky.lock.R;

public class DevicePolicyUtils {
	private DevicePolicyManager mPolicyManager;
	private ComponentName mWho;
	private Context mContext;
	
	private static DevicePolicyUtils mInstance;

	public static DevicePolicyUtils createInstance(Context context) {
		if (mInstance == null)
			mInstance = new DevicePolicyUtils(context.getApplicationContext());
		
		return mInstance;
	}
	
	public static DevicePolicyUtils getInstance() {
		return mInstance;
	}
	
	private DevicePolicyUtils(Context context) {
		mPolicyManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mWho = new ComponentName(context, KeyguardDeviceAdminReceiver.class);
		mContext = context;
	}

	public boolean isAdminActive() {
		return mPolicyManager.isAdminActive(mWho);
	}

	public void activeManger(Context context) {
		try{
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mWho);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				mContext.getString(R.string.lock_active_info));
		
		context.startActivity(intent);
		} catch (Exception e){
		}
	}
	
	public void clearAdmin() {
		mPolicyManager.removeActiveAdmin(mWho);
	}
	
	public void lockNow() {
		mPolicyManager.lockNow();
	}
}
