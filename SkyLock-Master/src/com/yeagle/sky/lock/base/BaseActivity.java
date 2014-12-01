package com.yeagle.sky.lock.base;

import com.umeng.analytics.MobclickAgent;

import android.view.animation.TranslateAnimation;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BaseActivity extends SwipeBackActivity {
	private SwipeBackLayout mSwipeBackLayout;
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		
		int w = getResources().getDisplayMetrics().widthPixels;
		TranslateAnimation animation = new TranslateAnimation(w, 0, 0, 0);
		animation.setDuration(280);
		animation.setFillAfter(false);
		mSwipeBackLayout.startAnimation(animation);
	}

	@Override
	public void onBackPressed() {
		scrollToFinishActivity();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	
}
