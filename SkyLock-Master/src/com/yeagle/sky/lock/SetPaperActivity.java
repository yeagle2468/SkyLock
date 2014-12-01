package com.yeagle.sky.lock;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.yeagle.sky.lock.adapter.ImageAdapter;
import com.yeagle.sky.lock.base.BaseActivity;
import com.yeagle.sky.lock.utils.Preferences;

public class SetPaperActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener, ViewFactory{
	private static final int REQUEST_GET_IMAGE = 237;
	
	private Gallery mGallery;
	private ImageSwitcher mSwitcher;
	private int mPosition;
	
	private static final int[] mImageIds = {R.drawable.picture1, 
    		R.drawable.picture3, R.drawable.picture4,
    		R.drawable.picture5, R.drawable.picture6,
    		R.drawable.picture8, R.drawable.picture9}; 
	
	private static final int[] mThumbnailImageIds = {R.drawable.picture1_small, 
		R.drawable.picture3_small, R.drawable.picture4_small, 
		R.drawable.picture5_small, R.drawable.picture6_small, 
		R.drawable.picture8_small, R.drawable.picture9_small
		};  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.lock_setpaper_layout);
		initViews();
	}
	
	private void initViews() {
		TextView titleView = (TextView) findViewById (R.id.title_textview);
		titleView.setText(R.string.lock_wallpaper);
		
		findViewById(R.id.title_back_view).setOnClickListener(this);
		findViewById(R.id.setpaper_btn).setOnClickListener(this);
		
		mGallery = (Gallery) findViewById (R.id.setpaper_gallery);
		mGallery.setOnItemSelectedListener(this);
		mGallery.setAdapter(new ImageAdapter(this, mThumbnailImageIds));
		mSwitcher = (ImageSwitcher) findViewById (R.id.setpaper_switcher);
		
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
			    android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
			    android.R.anim.fade_out));
		
		ImageView iv = (ImageView) findViewById (R.id.title_pic_view);
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(0x55202020));
		drawable.addState(new int[]{}, new ColorDrawable(0x01202020));
		
		iv.setBackgroundDrawable(drawable);
		iv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_view:
			scrollToFinishActivity();
			break;
		case R.id.setpaper_btn:
			String entryName = getResources().getResourceEntryName(mImageIds[mPosition]);
			Preferences.setWallpaperPath(this, entryName);
			
			finish();
			break;
		case R.id.title_pic_view:
			Intent intent = new Intent();  
            /* �?��Pictures画面Type设定为image */  
            intent.setType("image/*");  
            /* 使用Intent.ACTION_GET_CONTENT这个Action */  
            intent.setAction(Intent.ACTION_GET_CONTENT);   
            /* 取得相片后返回本画面 */  
            startActivityForResult(Intent.createChooser(intent, getString(R.string.lock_choose_wallpaper))
            		, REQUEST_GET_IMAGE); 
			break;
		default:
			break;
		}
	}
	
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_GET_IMAGE:
				Uri uri = data.getData();
				Preferences.setWallpaperPath(this, uri.toString());
				
				finish();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
//		  i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(-1, -1));
		
		return i;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		mPosition = position;
		mSwitcher.setBackgroundResource(mImageIds[position]);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}  
}
