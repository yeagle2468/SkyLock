package com.yeagle.sky.lock;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.yeagle.sky.lock.utils.DevicePolicyUtils;
//import com.yeagle.sky.lock.utils.LockUtils;
import com.yeagle.sky.lock.utils.Preferences;
import com.yeagle.sky.lock.view.PickerColorFrament;

import static com.yeagle.sky.lock.utils.LockUtils.addShortCut;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private CheckBox mEnableBox, mOneKeyBox;
	private FeedbackAgent mAgent;
	private DevicePolicyUtils policyUtils;
	private boolean mIsActive;
	
	private View mSendSCView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_main);
		
		initViews();
		policyUtils = DevicePolicyUtils.getInstance();
		mAgent = new FeedbackAgent(this);
		mAgent.sync();
		
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);
		
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
	}

	private void initViews() {
		TextView titleView = (TextView) findViewById (R.id.title_textview);
		titleView.setText(R.string.app_name);
		
		mEnableBox = (CheckBox)findViewById(R.id.lock_id_enable_lock);
		mEnableBox.setOnClickListener(this);
		boolean flag = Preferences.isLockEnable(this);
		mEnableBox.setChecked(flag);
		
		CheckBox oneKeyBox = (CheckBox)findViewById(R.id.lock_id_one_lock);
		oneKeyBox.setOnClickListener(this);
		mOneKeyBox = oneKeyBox;
		
		String str = getString(R.string.lock_one_lock);
		int end = str.length();
		int start = str.indexOf("\n");
		
		Spannable span1 = getSpannable(str, end, start);
		oneKeyBox.setText(span1);
		
		findViewById(R.id.lock_id_disenable_lock).setOnClickListener(this);
		findViewById(R.id.lock_id_preview).setOnClickListener(this);
		
		CheckBox fullScreenBox = (CheckBox)findViewById(R.id.lock_id_fullscreen);
		fullScreenBox.setOnClickListener(this);
		fullScreenBox.setChecked(Preferences.isFullScreen(this));
		
		str = getString(R.string.lock_fullscreen_show);
		end = str.length();
		start = str.indexOf("\n");
		
		Spannable span = getSpannable(str, end, start);
		fullScreenBox.setText(span);
		
		findViewById(R.id.lock_id_setpaper).setOnClickListener(this);
		CheckBox dateBox = (CheckBox) findViewById(R.id.lock_id_dateformat);
		dateBox.setChecked(Preferences.is24HourFormat(this));
		dateBox.setOnClickListener(this);
		findViewById(R.id.lock_id_type).setOnClickListener(this);
		
		findViewById(R.id.lock_id_feedback).setOnClickListener(this);
		findViewById(R.id.lock_id_share).setOnClickListener(this);
		findViewById(R.id.lock_id_about).setOnClickListener(this);
		findViewById(R.id.lock_id_help).setOnClickListener(this);
		findViewById(R.id.lock_id_font_color).setOnClickListener(this);
		
		mSendSCView = findViewById(R.id.lock_id_shortcut);
		mSendSCView.setOnClickListener(this);
	}

	private Spannable getSpannable(String str, final int end, final int start) {
		final int size = (int)getResources().getDimension(R.dimen.lock_assist_tv_size);
		Spannable span = new SpannableString(str);  
		span.setSpan(new AbsoluteSizeSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		final int color = getResources().getColor(R.color.lock_assist_tv_color);
		span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		return span;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (policyUtils.isAdminActive()) {
			mOneKeyBox.setChecked(true);
			mSendSCView.setVisibility(View.VISIBLE);
		} else {
			mOneKeyBox.setChecked(false);
			mSendSCView.setVisibility(View.GONE);
		}
		
		if (mIsActive) {
			if (policyUtils.isAdminActive())
				addShortCut(this);
			mIsActive = false;
		}
		
		MobclickAgent.onPageStart( "MainHome" );
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		MobclickAgent.onPageEnd( "MainHome" );
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lock_id_enable_lock: {
			CheckBox cb = (CheckBox)v;
			boolean flag = cb.isChecked();
			Intent service = new Intent(this, KeyguardService.class);
			
			if (flag) {
				getApplicationContext().startService(service);
			} else {
//				service = new Intent(KeyguardService.STOP_SERVICE_ACTION);
				stopService(service);
			}
			
			Preferences.setLockEnable(this, flag);
			break;
		}
		case R.id.lock_id_one_lock:
			if (!policyUtils.isAdminActive()) {
				mIsActive = true;
				policyUtils.activeManger(this);
			} else {
				policyUtils.clearAdmin();
				mSendSCView.setVisibility(View.GONE);
			}
			break;
		case R.id.lock_id_disenable_lock: {
			try {
			Intent intent = new Intent("android.app.action.SET_NEW_PASSWORD");
			ComponentName component = new ComponentName("com.android.settings", "com.android.settings.ChooseLockGeneric");
			intent.setComponent(component);
			
			startActivity(intent);
			} catch (Exception e) {
			}
			break;
		}
		case R.id.lock_id_preview: 
			Intent service = new Intent(this, KeyguardService.class);
			service.putExtra("start_preview", true);
			getApplicationContext().startService(service);
			break;
		case R.id.lock_id_fullscreen: {
			CheckBox fullCb = (CheckBox)v;
			boolean fullFlag = fullCb.isChecked();
			
			Preferences.setFullScreen(this, fullFlag);
			break;
		}
		case R.id.lock_id_setpaper: {
			Intent intent = new Intent(this, SetPaperActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.lock_id_dateformat: 
			CheckBox dateBox = (CheckBox)v;
			boolean dateFlag = dateBox.isChecked();
			
			Preferences.set24HourFormat(this, dateFlag);
			break;
		case R.id.lock_id_type:
			// TODO 选择类型
			break;
		
		case R.id.lock_id_feedback:
			mAgent.startFeedbackActivity();
			break;
		case R.id.lock_id_share: {
			Intent sendIntent = new Intent();  
			sendIntent.setAction(Intent.ACTION_SEND);  
			
			StringBuffer buffer = new StringBuffer(
					getString(R.string.lock_share_content));
//			buffer.append(getPackageName());
			
			sendIntent.putExtra(Intent.EXTRA_TEXT, buffer.toString());  
			sendIntent.setType("text/plain");  
			startActivity(sendIntent);
			break;
		}
		case R.id.lock_id_about: {
			Intent intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.lock_id_shortcut:
			addShortCut(this);
			break;
		case R.id.lock_id_font_color:
			if (mColorFragment == null)
				mColorFragment = new PickerColorFrament();
			
//			if (mColorPicker != null)
//				mColorPicker.setColor(Preferences.getFontColor(this));
//			MainActivity.this.color = Preferences.getFontColor(this);
			mColorFragment.show(getSupportFragmentManager(), "color_choice");
			break;
		case R.id.lock_id_help:{
			Intent intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			break;
		}
		default:
			break;
		}
	}
	
	private DialogFragment mColorFragment;
//	private ColorPicker mColorPicker;
}
