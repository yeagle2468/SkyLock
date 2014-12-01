package com.yeagle.sky.lock.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorSelectedListener;
import com.yeagle.sky.lock.R;
import com.yeagle.sky.lock.utils.Preferences;
import com.yeagle.sky.lock.widget.PickerColorDialog;

public class PickerColorFrament extends DialogFragment implements OnClickListener,
		OnColorSelectedListener, OnColorChangedListener{
	private boolean mChanged;
	private int mColor;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		PickerColorDialog dialog = new PickerColorDialog(getActivity());
		dialog.setOnClickListener(this);
		dialog.setOnColorChangedListener(this);
		dialog.setOnColorSelectedListener(this);
		return dialog;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.color_picker_ok:
			if (mChanged) {
				Preferences.setFontColor(getActivity(), mColor);
				mChanged = false;
			}
			dismiss();
			break;
		case R.id.color_picker_restore:
			dismiss();
			Preferences.setFontColor(getActivity(), Color.WHITE);
			break;
		case R.id.color_picker_cancel:
			dismiss();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, tag);
		mChanged = false;
	}

	@Override
	public int show(FragmentTransaction transaction, String tag) {
		mChanged = false;
		return super.show(transaction, tag);
	}

	@Override
	public void onColorChanged(int color) {
		mColor = color;
		mChanged = true;
	}

	@Override
	public void onColorSelected(int color) {
		mColor = color;
		mChanged = true;
	}
}
