package com.yeagle.sky.lock.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {  
    private Context myContext;  
    private int[] mImageIds;  
    
    public ImageAdapter(Context c, int[] imageIds) {  
        this.myContext = c;  
        this.mImageIds = imageIds;
    }

    @Override  
    public int getCount() {  
        return this.mImageIds.length;  
    }  

    @Override  
    public Object getItem(int position) {  
        return position;  
    }  

    @Override  
    public long getItemId(int position) {  
        return position;  
    }  

    @Override  
    public View getView(int position, View v, ViewGroup parent) {  
        ImageView i = new ImageView(this.myContext);  
        i.setImageResource(this.mImageIds[position]);  
        
//        if(Build.VERSION.SDK_INT >= 11)
//        	i.setImageAlpha(200);
//        else
//        	i.setAlpha(200);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);  
        i.setLayoutParams(new Gallery.LayoutParams(-2, -2));  
        
        return i;  
    }  

    public float getScale(boolean focused, int offset) {  
        return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));  
    }  
}
