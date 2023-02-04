package com.chinalwb.are.styles;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.AREditText;
import com.chinalwb.are.spans.AreBoldSpan;

public class ARE_BoldStyle extends ARE_ABS_Style<AreBoldSpan> {

    private AREditText mEditText;
    private boolean mBoldChecked;

    public ARE_BoldStyle(Context context, AREditText editText) {
        super(context);
        this.mEditText = editText;
    }

    public ARE_BoldStyle(Context context, ImageView imageView) {
        super(context);
    }

    @Override
    public EditText getEditText() {
        return mEditText;
    }

    public void bold() {
        if (null != mEditText) {
            applyStyle(mEditText.getEditableText(),
                    mEditText.getSelectionStart(),
                    mEditText.getSelectionEnd());
        }
    }

    boolean handleWholeText = false;

    public void boldAll(){
        handleWholeText = true;
        applyStyle(mEditText.getEditableText(), 0, mEditText.getEditableText().length());
    }

    @Override
    public void setListenerForImageView(final ImageView imageView) {

    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public void setChecked(boolean isChecked) {
        mBoldChecked = isChecked;
    }

    @Override
    public boolean getIsChecked() {
        // return: true(加粗)，false(取消加粗)
        int selectionStart = mEditText.getSelectionStart();
        int selectionEnd = mEditText.getSelectionEnd();
        if (handleWholeText){
            selectionStart = 0;
            selectionEnd = mEditText.getSelectionEnd();
            handleWholeText = false;
        }
        return !containStyle(selectionStart, selectionEnd);
    }

    @Override
    public AreBoldSpan newSpan() {
        return new AreBoldSpan();
    }

    // 判断是否包含加粗样式
    protected boolean containStyle(int start, int end) {
        if (start > end) {
            return false;
        }
        if (start == end) { // 未选中任何文本的情况
            return false;
        } else { // 选中文本的情况
            // 逐个文本检测，将已设置样式的字符添加到sb当中。若sb的内容与mEditText内容相等则视为已添加样式，否则为未添加。
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < end; i++) {
                StyleSpan[] spans = mEditText.getEditableText().getSpans(i, i + 1, AreBoldSpan.class);
                if (spans.length != 0) {
                    sb.append(mEditText.getEditableText().subSequence(i, i + 1));
                }
            }
            Log.i("wk", "sb: " + sb.toString());
            return mEditText.getEditableText().subSequence(start, end).toString().equals(sb.toString());
        }
    }
}
