package com.yeagle.sky.lock.widget;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorSelectedListener;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.utils.Preferences;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class PickerColorDialog extends Dialog {
	private ColorPicker mPicker;
	private View mRoot;
	
	public PickerColorDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(createView());
		initProperties();
	}
	
	@Override
	public void show() {
		if (mPicker != null)
			mPicker.setColor(Preferences.getFontColor(getContext()));
		super.show();
	}
	
	private View createView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.lock_colorpicker_layout, null);
		ColorPicker picker = (ColorPicker)view.findViewById(R.id.colorpicker_view);
		SaturationBar bar = (SaturationBar)view.findViewById(R.id.staturation_view);
		
		picker.addSaturationBar(bar);
		mRoot = view;
		
		picker.setColor(Preferences.getFontColor(getContext()));
		mPicker = picker;
		
		return view;
	}
	
	public void setOnClickListener(android.view.View.OnClickListener listener) {
		if (mRoot == null || listener == null)
			return;
		
		mRoot.findViewById(R.id.color_picker_ok).setOnClickListener(listener);
		mRoot.findViewById(R.id.color_picker_restore).setOnClickListener(listener);
		mRoot.findViewById(R.id.color_picker_cancel).setOnClickListener(listener);
	}
	
	private void initProperties() {
		setCanceledOnTouchOutside(true);
		getWindow().setWindowAnimations(R.style.window_animstyle);
	}
	
	public void setOnColorSelectedListener(OnColorSelectedListener listener) {
		mPicker.setOnColorSelectedListener(listener);
	}
	
	public void setOnColorChangedListener(OnColorChangedListener listener) {
		mPicker.setOnColorChangedListener(listener);
	}
}
