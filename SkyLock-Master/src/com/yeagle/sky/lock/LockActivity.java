package com.yeagle.sky.lock;

import java.io.FileNotFoundException;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yeagle.sky.lock.factory.ILockViewFactory;
import com.yeagle.sky.lock.factory.NormalLockViewFactory;
import com.yeagle.sky.lock.utils.Preferences;
import com.yeagle.sky.lock.widget.LockScreen;
import com.yeagle.sky.lock.widget.LockScreen.OnLockListener;

public class LockActivity extends FragmentActivity implements OnLockListener {
	
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		addWindowFlags();
		super.onCreate(arg0);
		
		addLockScreen();
	}

	private void addWindowFlags() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	}
	
	public void addLockScreen() {
		LockScreen layout = new LockScreen(this);
		layout.setOnLockListener(this);
		
		setBackground(layout);
		
		if (Preferences.isFullScreen(this)) {
			layout.addBetteryView();
		}
		layout.addLockView(getLockViewFactory());
	}
	
	private ILockViewFactory getLockViewFactory() {
		String className = Preferences.getLockType(this);
		if (className == null) {
			return new NormalLockViewFactory();
		}
		
		try {
			Class<?> c = Class.forName(className);
			return (ILockViewFactory)c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return new NormalLockViewFactory();
	}
	
	private void setBackground(View view) {
		String path = Preferences.getWallpaperPath(this);
		
		if (!path.startsWith("picture")) {
			ContentResolver cr = this.getContentResolver();  
            try {  
            	Uri uri = Uri.parse(path);
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                if (bitmap != null) {
    				view.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
    			} else {
    				view.setBackgroundDrawable(getResources().getDrawable(R.drawable.picture8));
//    				view.setBackgroundResource(R.drawable.picture8);
    			}
            } catch (FileNotFoundException e) {
            	Preferences.setWallpaperPath(getApplicationContext(), 
            			getResources().getResourceEntryName(R.drawable.picture8));
            	view.setBackgroundDrawable(getResources().getDrawable(R.drawable.picture8));
            } catch (Exception e) {
            	Preferences.setWallpaperPath(getApplicationContext(), 
            			getResources().getResourceEntryName(R.drawable.picture8));
            	view.setBackgroundDrawable(getResources().getDrawable(R.drawable.picture8));
			}
		} else {
			try {
//				int id = Integer.valueOf(path);
				int id = getResources().getIdentifier(path, "drawable", getPackageName());
				Drawable drawable = getResources().getDrawable(id);
				if (drawable != null) 
					view.setBackgroundDrawable(drawable);
				else
					view.setBackgroundResource(id);
			} catch (Exception e) {
				Preferences.setWallpaperPath(getApplicationContext(), 
            			getResources().getResourceEntryName(R.drawable.picture8));
				view.setBackgroundResource(R.drawable.picture8);
			}
		}
	}
	
	public void exit() {
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		closeSystemDialog(getApplicationContext());
	}
	
	public void closeSystemDialog(Context context) {
		context.sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
	}
	
	public void onBackPressed() {
	}

	@Override
	public void unLock() {
		LockHelper.getInstance().removeWindowView();
		finish();
	}
	
}
