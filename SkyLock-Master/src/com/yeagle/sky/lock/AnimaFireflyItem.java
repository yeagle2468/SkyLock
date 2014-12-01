package com.yeagle.sky.lock;

import com.yeagle.sky.lock.base.AnimActorBase;

import android.content.Context;
import android.graphics.Matrix;

public class AnimaFireflyItem extends AnimActorBase {
	
	boolean mFlag;
	
	public AnimaFireflyItem(Context context, String prefix, int maxNum) {
		super(context, prefix, maxNum);
	}

	@Override
	public void init() {
		mItem.pWidth = mBit.getWidth();
		mItem.pHeight = mBit.getHeight();
		
		mItem.x = ((float)Math.random() * mWidth);
		mItem.y = ((float)Math.random() * mHeight);
		
		mItem.angle = (180.0F * (float)Math.random() - 180.0F);
		mItem.dy = (0.5F + 0.5F * (float)Math.random());
		mItem.dx = (mItem.dy * (float)Math.tan(Math.toRadians(mItem.angle)));
		mItem.scalex = (0.5F + 0.75F * (float)Math.random());
		mItem.scaley = mItem.scalex;
		mItem.h = (0.8F + 0.2F * (float)Math.random());
		mItem.k = (0.005F + 0.01F * (float)Math.random());
	}

	@Override
	public void applyTransformation(Matrix matrix) {
		Item item = mItem;
		
		item.y += item.dy;
		item.x += item.dx;
	    
	    if ((item.x > this.mWidth) || (item.x < 0 - item.pWidth) || (item.y > mHeight) || (item.y < 0 - item.pHeight)) {
	    	item.y = ((float)Math.random() * mHeight);
	    	item.x = ((float)Math.random() * mWidth);
	    	item.angle = (180.0F * (float)Math.random() - 180.0F);
	    	item.dy = (0.5F + 0.5F * (float)Math.random());
	    	item.scalex = (0.5F + 0.75F * (float)Math.random());
	    	item.scaley = item.scalex;
	    	item.dx = (item.dy * (float)Math.tan(Math.toRadians(item.angle)));
	    }
	    
	    if (mFlag) { 
	    	item.h += item.k;
	    }  else { //cond_4
	    	item.h -= item.k;
	    }
	    
	    if (item.h < 0) { // 
    		item.h = 0;
    	} else {
    		// TODO label438 cond_5
    		if (item.h > 1.0F);
    			item.h = 1.0F;
    	}
	    
	    if (item.h == 0) { // cond_2
			mFlag = true;
		} 
//	    else { //cond_6
//			
//		}
//	    
//	    if (item.h != 1.0F) {
//	    	
//		} else {
//			mFlag = false;
//		}
	    
	    if (item.h == 1.0f)
	    	mFlag = false;
	    
	    // cond_3
		matrix.setTranslate(-item.pWidth / 2, item.pHeight / 2);
		matrix.postRotate(item.angle);
		matrix.postTranslate(item.pWidth / 2 + item.x, item.pHeight / 2 + item.y);
		matrix.postScale(item.scalex, item.scaley);
		// really over
	}
}
