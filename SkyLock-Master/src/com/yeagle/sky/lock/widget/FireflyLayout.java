package com.yeagle.sky.lock.widget;

import java.util.ArrayList;
import java.util.Iterator;

import com.yeagle.sky.lock.AnimaFireflyItem;
import com.yeagle.sky.lock.base.AnimActorBase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class FireflyLayout extends AnimLayoutBase {
	public ArrayList<AnimActorBase> list = new ArrayList<AnimActorBase>();
	Paint mPaint = new Paint();

	public static final int MAX_NUM = 11;
	public static final int TOTAL_NUM = 26;
	
	public FireflyLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public FireflyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FireflyLayout(Context context) {
		super(context);
		init();
	}

	@Override
	public final void init() {
		for (int i = 0; i<TOTAL_NUM; ++i) {
			AnimaFireflyItem localb = new AnimaFireflyItem(getContext(), "anim_", MAX_NUM);
			list.add(localb);
		}
	}

	@Override
	public final void drawAnim(Canvas canvas) {
		Iterator<AnimActorBase> it = list.iterator();
		
		while (it.hasNext()) {
			canvas.save();
			AnimActorBase item = it.next();
			item.applyTransformation(mMatrix);
			
			mPaint.setAlpha((int)(255.0F * item.mItem.h));
			canvas.drawBitmap(item.mBit, mMatrix, mPaint);
			
		    canvas.restore();
		}
	}
}
