package com.yeagle.sky.lock.widget;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.utils.Preferences;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class ColorDialogBuilder extends Builder {
	
	public ColorDialogBuilder(Context arg0) {
		super(arg0, R.style.dialog);
	}

	@Override
	public AlertDialog create() {
		setView(createView());
		return super.create();
	}
	
	private View createView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.lock_colorpicker_layout, null);
		ColorPicker picker = (ColorPicker)view.findViewById(R.id.colorpicker_view);
		SaturationBar bar = (SaturationBar)view.findViewById(R.id.staturation_view);
		
		picker.addSaturationBar(bar);
		picker.setColor(Preferences.getFontColor(getContext()));
		
		return view;
	}

	
	
}
