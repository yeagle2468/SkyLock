package com.yeagle.sky.lock;

import android.app.Notification;
import android.app.PendingIntent;
//import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.yeagle.sky.lock.utils.Log;
import com.yeagle.sky.lock.utils.Preferences;

import static com.yeagle.sky.lock.utils.LockUtils.getAppInfoIntent;

public class KeyguardService extends Service {
	private PowerManager.WakeLock mWakeLock;
	private BroadcastReceiver mReceiver = new LockReceiver();
	private LockHelper mHelper;
	private TelephonyManager mTelephonyManager;
	
	private boolean mIsInterrupt;
	
	private static final int NOTIFICATION_ID = 2323; // 
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, this.getPackageName());
		mWakeLock.setReferenceCounted(false);
		mTelephonyManager = (TelephonyManager) getSystemService (Context.TELEPHONY_SERVICE);
		mHelper = LockHelper.getInstance();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
//		filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		registerReceiver(mReceiver, filter);
		
		mTelephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		if (!manager.isScreenOn()) {
			startLock();
		}
		
		setForeground();
	}
	
	private void setForeground() {
		Intent intent = getAppInfoIntent(getPackageName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    
	    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	    Notification notification;

	    String title = getString(R.string.app_name) + getString(R.string.lock_notification_title);
	    String content = getString(R.string.lock_notification_content);
	    
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    	builder.setContentIntent(contentIntent)
 	    .setSmallIcon(R.drawable.ic_launcher)
 	    .setWhen(System.currentTimeMillis())
 	    .setAutoCancel(false)
 	    .setContentTitle(title)
 	    .setContentText(content)
 	    .setPriority(NotificationCompat.PRIORITY_MIN); 
 	    
 	    notification = builder.build();
 	    
	    notification.flags = 64;
		startForeground(NOTIFICATION_ID, notification);
	}
	
	private PhoneStateListener listener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (!Preferences.isHomeLockEnable(getApplicationContext())) 
				return;
			
			Log.e("onCallStateChanged", "state:" + state);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (mHelper.mIsAddToWindow) { // remove lock view only lock screen
					mHelper.removeWindowView();
					mIsInterrupt = true;
					
					Log.e("onCallStateChanged", "mIsInterrupt:");
				}  
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (mIsInterrupt) {
					startLock();
					mIsInterrupt = false;
				}
				break;
			default:
				break;
			}
		}
		
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		if (intent != null && intent.getBooleanExtra("start_preview", false))
			startLock();
		else if (!Preferences.isLockEnable(this))
			stopSelf(startId);
		
		return START_STICKY;
	}

	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
		mTelephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		
		if (this.mWakeLock.isHeld()) {
			this.mWakeLock.release();
		}
//		Log.e("rer", "onDestroy");
	}

	private void startLock() {
		mWakeLock.release();
		mWakeLock.acquire(500L);

		// 启动
		Intent intent2 = new Intent(getApplicationContext(),
				LockActivity.class);
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplicationContext().startActivity(intent2);
	}
	
	private boolean isPhoneIDLE() {
		return mTelephonyManager.getCallState() == TelephonyManager.CALL_STATE_IDLE;
	}
	
	private class LockReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			Log.e("ds", "action:" + action);
			
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				mWakeLock.release();
				
				mHelper.animStart();
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				if (mIsInterrupt || !isPhoneIDLE())
					return;
				
				mHelper.animStop();
				
				if (!mHelper.mIsAddToWindow) {
					startLock();
				}
			}
		}
	}
}
