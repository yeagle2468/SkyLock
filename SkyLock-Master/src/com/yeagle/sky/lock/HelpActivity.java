package com.yeagle.sky.lock;

import com.yeagle.sky.lock.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class HelpActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.lock_help_layout);
		initViews();
	}
	
	private void initViews() {
		TextView titleView = (TextView) findViewById (R.id.title_textview);
		titleView.setText(R.string.lock_help);
		
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
