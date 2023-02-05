package com.chinalwb.are.styles;

import android.content.Context;
import android.text.Editable;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.Util;
import com.chinalwb.are.colorpicker.ColorPickerListener;
import com.chinalwb.are.spans.AreForegroundColorSpan;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public class ARE_FontColorStyle extends ARE_ABS_Dynamic_Style<AreForegroundColorSpan> {
	private AREditText mEditText;
	private int mColor = -1;

	public ARE_FontColorStyle(Context context, AREditText editText) {
		super(context);
		mEditText = editText;
	}

	public void color(int color){
		this.mColor = color;
		applyStyle(mEditText.getEditableText(), mEditText.getSelectionStart(), mEditText.getSelectionEnd());
	}

	public void colorAll(int color) {
		this.mColor = color;
		applyStyle(mEditText.getEditableText(), 0, mEditText.getEditableText().length());
	}

	@Override
	public void setListenerForImageView(final ImageView imageView) {
	}

	@Override
	protected void changeSpanInsideStyle(Editable editable, int start, int end, AreForegroundColorSpan existingSpan) {
		int currentColor = existingSpan.getForegroundColor();
		if (currentColor != mColor) {
			Util.log("color changed before: " + currentColor + ", new == " + mColor);
			applyNewStyle(editable, start, end, mColor);
			logAllFontColorSpans(editable);
		}
	}

	private void logAllFontColorSpans(Editable editable) {
		ForegroundColorSpan[] listItemSpans = editable.getSpans(0,
				editable.length(), ForegroundColorSpan.class);
		for (ForegroundColorSpan span : listItemSpans) {
			int ss = editable.getSpanStart(span);
			int se = editable.getSpanEnd(span);
			Util.log("List All: " + " :: start == " + ss + ", end == " + se);
		}
	}

	@Override
	public AreForegroundColorSpan newSpan() {
		return new AreForegroundColorSpan(this.mColor);
	}

	@Override
	public ImageView getImageView() {
		return null;
	}

	@Override
	public void setChecked(boolean isChecked) {
		// Do nothing
	}

	@Override
	public boolean getIsChecked() {
		return this.mColor != -1;
	}

	@Override
	public EditText getEditText() {
		return this.mEditText;
	}

	@Override
	protected AreForegroundColorSpan newSpan(int color) {
		return new AreForegroundColorSpan(color);
	}

	@Override
	protected void featureChangedHook(int lastSpanColor) {
		mColor = lastSpanColor;
	}
}
