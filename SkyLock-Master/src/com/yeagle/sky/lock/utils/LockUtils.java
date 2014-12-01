package com.yeagle.sky.lock.utils;

import java.lang.reflect.Field;
import java.util.List;

import com.yeagle.sky.lock.KeyguardAdminLockActivity;
import com.yeagle.sky.lock.LockActivity;
import com.yeagle.sky.lock.R;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;

public class LockUtils {
	private static final String SCHEME = "package";
	private static final String SDK7 = "com.android.settings.ApplicationPkgName";
	private static final String SDK8 = "pkg";
	private static final String SETTING_PACKAGE_NAME = "com.android.settings";
	private static final String DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	
	public static boolean isSecureLock(Context context) {
		try {
			Class localClass = Class
					.forName("com.android.internal.widget.LockPatternUtils");
			boolean flag = ((Boolean) localClass.getMethod("isSecure", null).invoke(localClass.getConstructors()[0].newInstance(
					new Object[] { context }), new Object[0])).booleanValue();
			
			return flag;
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return false;
	}
	
	public static boolean isLockTopActivity(Context context) {
		List localList = ((ActivityManager)context.getSystemService("activity")).getRunningTasks(1);
		
	    if ((localList != null) && (localList.size() > 0)) {
	    	ComponentName componentName = ((ActivityManager.RunningTaskInfo)localList.get(0)).topActivity;
	    	if (!componentName.getClassName().endsWith(LockActivity.class.getName()))
	    		return false;
	    } else
	    	return false;
	    
	    return true;
	}
	
	public static void addShortCut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
				.getString(R.string.lock_one_key));
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				context.getApplicationContext(), R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, iconRes);

		// 是否允许重复创建
		shortcut.putExtra("duplicate", false);

		// 设置桌面快捷方式的图片
		Parcelable icon = Intent.ShortcutIconResource.fromContext(context,
				R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

		// 点击快捷方式的操作
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(context, KeyguardAdminLockActivity.class);

		// 设置启动程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 广播通知桌面去创建
		context.sendBroadcast(shortcut);
	}

	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;

		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return statusBarHeight;

	}
	
	public static Intent getAppInfoIntent(String packageName) {
		Intent intent = new Intent();

		final int apiLevel = Build.VERSION.SDK_INT;

		if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		} else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
			final String appPkgName = (apiLevel == 8 ? SDK8 : SDK7);
			
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(SETTING_PACKAGE_NAME, DETAILS_CLASS_NAME);

			intent.putExtra(appPkgName, packageName);
		}

		return intent;
	}
}
