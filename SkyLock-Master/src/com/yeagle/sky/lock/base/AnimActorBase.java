package com.yeagle.sky.lock.base;

import java.util.Random;

import com.yeagle.sky.lock.utils.RecycleCache;
import com.yeagle.sky.lock.utils.RecycleCache.OnRecycleListener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public abstract class AnimActorBase {
	private static Random mRandom = new Random();
	private static RecycleCache<Integer, Bitmap> mCache;
	
	public String mPrefix;
	public int mMaxNum;
	public Bitmap mBit;
	protected int mWidth;
	protected int mHeight;
	public Item mItem = new Item();
	
	static {
		RecycleCache<Integer, Bitmap> cache = new RecycleCache<Integer, Bitmap>();
		mCache = cache;
		
		cache.setOnRecycleListener(new OnRecycleListener() {
			@Override
			public void recycle(Object object) {
				if (object == null)
					return;
				
				Bitmap bitmap = (Bitmap) object;
				if (!bitmap.isRecycled())
					bitmap.recycle();
			}
		});
	}
	
	public AnimActorBase(Context context, String prefix, int maxNum) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		
		manager.getDefaultDisplay().getMetrics(outMetrics);
		mWidth = outMetrics.widthPixels;
		mHeight = outMetrics.heightPixels;
		
		this.mPrefix = prefix;
		this.mMaxNum = maxNum;
		
		final int num = mRandom.nextInt(maxNum);
		Resources res = context.getResources();
		int id;
		
		if (num < 10) {
			StringBuilder builder = new StringBuilder(prefix);
			builder.append('0').append(num);
			
			id = res.getIdentifier(builder.toString(), "drawable", context.getPackageName());
		} else {
			StringBuilder builder = new StringBuilder(prefix);
			builder.append(num);
			
			id = res.getIdentifier(builder.toString(), "drawable", context.getPackageName());
		}
		
		Bitmap bitmap = mCache.getValue(id);
		
		if (bitmap != null) {
			mBit = bitmap;
		} else {
			mBit = BitmapFactory.decodeResource(res, id);
			mCache.put(id, mBit);
		}
		
		init();
	}
	
	public Bitmap getBitmap() {
		return mBit;
	}
	
	public abstract void init();
	public abstract void applyTransformation(Matrix matrix);
	
	public class Item {
		public float x;
		public float y;
		public float angle;
		public int pWidth;
		public int pHeight;
		public float scalex;
		public float scaley;
		public float h;
		public float dx;
		public float dy;
		public float k;
	}
}
