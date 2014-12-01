package com.yeagle.sky.lock.widget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yeagle.sky.lock.LockHelper;
import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.utils.FireFlyTypeface;
import com.yeagle.sky.lock.utils.Log;
import com.yeagle.sky.lock.utils.Preferences;

public class NormalLockView extends LockView {
	private static final String EN_MONTHS[] = {"January", "February", "March", 
		"April", "May", "June", "July",  "August", 
		"September", "October", "November", "December"};
	
	private LockHelper mHelper;
//	private boolean mIsPinEnable;
	
	private String mWeekList[];
	private String mFormat;
	
	private TextView mTimeView, mDateView;
	private TextView mBatteryState;
	private LockObserver mObserver;
	
	private SlidingTextView mSlidingText;
	
	private boolean mIsRegisted = false;
	private boolean mIsFull = true;
	
	protected static final int MAX_SETTLE_DURATION = 600; // ms
	
	private Typeface mLightTypeface = FireFlyTypeface.mLightTypeface;
	private int mColor;
	
	public NormalLockView(Context context, AttributeSet attrs) {
//		this(context, attrs);
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		setWeightSum(1.0f);
		
		mWeekList = getResources().getStringArray(
				R.array.lock_weekday_array);
		mFormat = getResources().getString(R.string.lock_format);
		
		mColor = Preferences.getFontColor(getContext());
		addViews();
	}

	public NormalLockView(Context context) {
		this(context, null);
	}
	
	private void addViews() {
		mTimeView = new TextView(getContext());
		mDateView = new TextView(getContext());
		final int color = 0xff000000|mColor;
		
		Typeface typeface = mLightTypeface;
		if (typeface == null)
			typeface = FireFlyTypeface.createLightTypeface(getContext().getAssets());
		mTimeView.setTypeface(typeface);
		mTimeView.setGravity(Gravity.CENTER_HORIZONTAL);
		mDateView.setTypeface(typeface);
		mDateView.setGravity(Gravity.CENTER_HORIZONTAL);
		
		mTimeView.setTextSize(75);
		mDateView.setTextSize(18);
		
		mTimeView.setTextColor(color);
		mDateView.setTextColor(color);
		
		addView(new View(getContext()), new LinearLayout.LayoutParams(-2, 0, 0.11f));
		addView(mTimeView);
		addView(mDateView);
		
		Typeface tf = FireFlyTypeface.mLTSTypeface;
		if (tf == null)
			tf = FireFlyTypeface.createLSTTypeface(getContext().getAssets());
		
		float per = 0.70f;
		if (!Preferences.isFullScreen(getContext())) {
			TextView batteryStatus = new TextView(getContext());
			batteryStatus.setTextColor(color);
			batteryStatus.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
			batteryStatus.setTextSize(16);
			batteryStatus.setTypeface(tf);
			
			addView(batteryStatus, new LinearLayout.LayoutParams(-1, 0, 0.10f));
			mBatteryState = batteryStatus;
			
			mIsFull = false;
		} else
			per += 0.1f;
		
		LinearLayout layout = new LinearLayout(getContext());
		SlidingTextView slidingTextView = new SlidingTextView(getContext());
		slidingTextView.setTextColor(mColor);
		slidingTextView.setSTypeface(tf);
		slidingTextView.setTextSize(23);
		slidingTextView.setDefaultText();
		
		mSlidingText = slidingTextView;
		
		layout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
		params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
		
		layout.addView(new View(getContext()), new LinearLayout.LayoutParams(-2, 0, 1));
		layout.addView(slidingTextView, params);
		
		addView(layout, new LinearLayout.LayoutParams(-1, 0, per));
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		PowerManager manager = (PowerManager)getContext().getSystemService(Context.POWER_SERVICE);
		if (manager.isScreenOn()) {
//			Log.e(VIEW_LOG_TAG, "animStart");
			animStart();
		} else 
			updateTime();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		unregister();
	}
	
	/**
	 * @param state
	 */
	public void setBatteryPercent(String state) {
		if (mBatteryState != null)
			mBatteryState.setText(state);
		else if (mHelper != null) {
			mHelper.setBatteryPercent(state);
		}
	}
	
	synchronized void unregister() {
		if (!mIsRegisted)
			return;
		
		getContext().unregisterReceiver(mReceiver);
		getContext().getContentResolver().unregisterContentObserver(mObserver);
		mIsRegisted = false;
	}

	public void setLockHelper(LockHelper helper) {
		this.mHelper = helper;
	}
	
	synchronized void register() {
		if (mIsRegisted)
			return;
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		
		getContext().registerReceiver(mReceiver, filter);
		
		this.mObserver = new LockObserver(getHandler());
	    this.getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, this.mObserver);
	    
	    mIsRegisted = true;
	}
	
	private void updateTime() {
		try {
			boolean is24 = Preferences.is24HourFormat(getContext());
			int hours01, hours02, minutes01, minutes02, h;

			Calendar calendar = Calendar.getInstance();
			int m = calendar.get(Calendar.MINUTE);

			if (is24) {
				h = calendar.get(Calendar.HOUR_OF_DAY);
			} else {
				h = calendar.get(Calendar.HOUR);
				h = h == 0 ? 12 : h;
			}
			hours01 = h / 10;
			hours02 = h % 10;

			minutes01 = m / 10;
			minutes02 = m % 10;

			StringBuffer timeStr = new StringBuffer();
			timeStr.append(hours01)
			.append(hours02)
			.append(':')
			.append(minutes01)
			.append(minutes02);
			
			mTimeView.setText(timeStr.toString());
			mDateView.setText(getNowDate());
		} catch (Exception e) {
		}
	}
	
	private String getNowDate() {
		Calendar calendar = Calendar.getInstance();
		StringBuffer buffer = new StringBuffer();
		
		if (Locale.getDefault().getLanguage()
				.equals(Locale.CHINA.getLanguage())) {
			SimpleDateFormat format = new SimpleDateFormat(mFormat);
			
			Date date = new Date(System.currentTimeMillis());
			
			buffer.append(getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
			.append(',')
			.append(format.format(date));
		} else {
			buffer.append(getDateOfWeek(calendar.get(Calendar.DAY_OF_WEEK)))
			.append(',');
			
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE);
			buffer.append(EN_MONTHS[month])
			.append(' ')
			.append(day);
		}
		
		return buffer.toString();
	}
	
	private String getDateOfWeek(int day) {
		if (day >= 1 && day <= 7) {
			return mWeekList[day - 1];
		} else {
			return mWeekList[0];
		}
	}
	
	@Override
	public void onStart() {
		animStart();
	}

	@Override
	public void onStop() {
		animStop();
	}

	private void animStart() {
		register();
		
		postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				if (mSlidingText != null)
					mSlidingText.animStart();
			}
		}, 200);
		updateTime();
	}
	
	public void animStop() {
//		if (mHelper != null)
//			mHelper.animStop();
		
		if (mSlidingText != null)
			mSlidingText.animStop();
		
		unregister();
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(Intent.ACTION_TIME_TICK)
					|| action.equals(Intent.ACTION_TIME_CHANGED)
					|| action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				updateTime();
			} else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				final int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
				int current = intent.getExtras().getInt("level");//获得当前电量
	            int total = intent.getExtras().getInt("scale");//获得总电
	            float percent = (float)current / (float)total;
				
	            String string;
	            if (mIsFull) {
	            	string = (int)(100*percent) + "%";
	            	
	            	mHelper.setCharge(status == BatteryManager.BATTERY_STATUS_CHARGING, percent);
	            } else {
	            	if(status==BatteryManager.BATTERY_STATUS_CHARGING) {
						string = getResources().getString(R.string.lock_charging_info, (int)(100*percent)+"%");
					} else if (status==BatteryManager.BATTERY_STATUS_FULL) {
						string = getResources().getString(R.string.lock_battery_full);
					} else {
						string = getResources().getString(R.string.lock_battery_info, (int)(100*percent)+"%");
					}
	            }
				
				setBatteryPercent(string);
			}
		}
	};
	
	private final class LockObserver extends ContentObserver {
	    public LockObserver(Handler handler) {
			super(handler);
		}

		public final void onChange(boolean changed) {
			updateTime();
	    }
	  }
}
