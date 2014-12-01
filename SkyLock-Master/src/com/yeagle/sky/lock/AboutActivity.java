package com.yeagle.sky.lock;

import com.yeagle.sky.lock.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = AboutActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.lock_about_layout);
		initViews();
	}
	
	private void initViews() {
		TextView titleView = (TextView) findViewById (R.id.title_textview);
		titleView.setText(R.string.lock_about);
		
		findViewById(R.id.title_back_view).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_view:
			scrollToFinishActivity();
			break;
		default:
			break;
		}
	}

}
