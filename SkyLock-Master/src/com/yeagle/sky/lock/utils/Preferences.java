package com.yeagle.sky.lock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.DateFormat;

import com.yeagle.sky.lock.R;

public class Preferences {
	public static boolean isLockEnable(Context context) {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", 
				Context.MODE_PRIVATE).getBoolean("enable_lockscreen", true);
	}
	
	public static void setLockEnable(Context context, boolean flag) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("enable_lockscreen", flag);
		editor.commit();
	}
	
	public static boolean isFullScreen(Context context) {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", 
				Context.MODE_PRIVATE).getBoolean("full_screen", true);
	}
	
	public static void setFullScreen(Context context, boolean flag) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("full_screen", flag);
		editor.commit();
	}
	
	public static String getWallpaperPath(Context context) {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", 
				Context.MODE_PRIVATE).getString("wallpaper_path", String.valueOf(R.drawable.picture8));
	}
	
	/**
	 * @param context
	 * @param path 保存名字才能保证名字不变锁屏不会变，id保存增加或删除就不行�?
	 */
	public static void setWallpaperPath(Context context, String path) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("wallpaper_path", path);
		editor.commit();
	}
	
	public static boolean is24HourFormat(Context context) {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", 
				Context.MODE_PRIVATE).getBoolean("24_format", DateFormat.is24HourFormat(context));
	}
	
	public static String getLockType(Context context) {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", 
				Context.MODE_PRIVATE).getString("lock_type", null);
	}
	
	public static boolean isHomeLockEnable(Context context) {
		SharedPreferences preferences = context.getSharedPreferences("__", 0);
		                                                       
		boolean flag = true;
		if (preferences.contains("key_enable_home_lock"))
			flag = preferences.getBoolean("key_enable_home_lock", true);
		
		return flag;
	}
	
	public static void set24HourFormat(Context context, boolean flag) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean("24_format", flag);
		editor.commit();
	}
	
	public static int getFontColor(Context context) {
		return context.getSharedPreferences(context.getPackageName()+"_preferences", 
				Context.MODE_PRIVATE).getInt("font_color", 0xffffff);
	}
	
	public static void setFontColor(Context context, int color) {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("font_color", color);
		editor.commit();
	}
}
