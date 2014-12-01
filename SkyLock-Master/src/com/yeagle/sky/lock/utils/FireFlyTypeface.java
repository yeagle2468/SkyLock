package com.yeagle.sky.lock.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class FireFlyTypeface {
	public static Typeface mLightTypeface;
	public static Typeface mLTSTypeface;
	
	public static Typeface createLightTypeface (AssetManager mgr) {
		if (mLightTypeface == null) {
			mLightTypeface = Typeface.createFromAsset(mgr, "lightfont.otf");
		}
		
		return mLightTypeface;
	}
	
	public static Typeface createLSTTypeface (AssetManager mgr) {
		if (mLTSTypeface == null) {
			mLTSTypeface = Typeface.createFromAsset(mgr, "HelveticaNeueLTStd-Lt-large-less-greater.otf");
		}
		
		return mLTSTypeface;
	}
}
