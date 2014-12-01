package com.yeagle.sky.lock.utils;

import com.yeagle.sky.lock.BuildConfig;

public class Log {
	public static void e(String tag, String msg) {
		if (BuildConfig.DEBUG)
			android.util.Log.e(tag, msg);
	}
}
