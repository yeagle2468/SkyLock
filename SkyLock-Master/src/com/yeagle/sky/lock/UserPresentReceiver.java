package com.yeagle.sky.lock;

import java.util.List;

import com.yeagle.sky.lock.utils.Preferences;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UserPresentReceiver extends BroadcastReceiver {
	private static final String TAG = "UserPresentReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
	    ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceList = manager.getRunningServices(30);
        
        boolean flag = serviceIsStart(mServiceList, KeyguardService.class.getName());
        
        if (!flag && (Preferences.isLockEnable(context)) ) {
        	Intent service = new Intent(context, KeyguardService.class);
//    		service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		context.startService(service);
    		
    		Log.e(TAG, "startService");
        }
	}
	
	private boolean serviceIsStart(List<ActivityManager.RunningServiceInfo> mServiceList,String className){
        for(int i = 0; i < mServiceList.size(); i ++){
            if(className.equals(mServiceList.get(i).service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
