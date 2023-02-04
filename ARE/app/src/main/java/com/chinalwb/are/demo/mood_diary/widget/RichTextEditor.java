package com.chinalwb.are.demo.mood_diary.widget;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.chinalwb.are.demo.mood_diary.util.ScreenUtils;

import java.io.File;

/**
 * 用于心情日记的富文本编辑器
 *
 * @author: WK
 * @date: 2023/1/31
 */
public class RichTextEditor extends ScrollView {
    private final Context context;
    private final LinearLayout rootLayout = new LinearLayout(getContext());
    private EditText lastFocusEditText;

    // 内容块
    // Block


    private final OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                lastFocusEditText = (EditText) v;
            }
        }
    };
    private final OnKeyListener keyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // 按下了删除键
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                EditText editText = (EditText) v;
                onBackspacePress(editText);
            }
            return false;
        }
    };

    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initEditor();
    }

    private void initEditor() {
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        addView(rootLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setupLayoutTransition();

        EditText firstEditText = createEditText();
        firstEditText.requestFocus();

        rootLayout.addView(firstEditText);
    }

    private void setupLayoutTransition() {
        LayoutTransition transition = new LayoutTransition();
        rootLayout.setLayoutTransition(transition);
    }

    @SuppressLint("SetTextI18n")
    private void onBackspacePress(EditText editText) {
        int selectionStartIndex = editText.getSelectionStart();
        if (selectionStartIndex != 0) {
            return;
        }
        int currEditIndex = rootLayout.indexOfChild(editText);
        View preView = rootLayout.getChildAt(currEditIndex - 1);
        if (preView == null) {
            return;
        }
        // 处理控件的删除和文本框内容的合并操作
        // 上一个 View 是图片类型
        if (preView instanceof FrameLayout) {
            // 当光标位于输入框起始位置且文本内容不为空，需要在移除输入框前将其中的内容合并到上一个输入框
            if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                View prePreView = rootLayout.getChildAt(currEditIndex - 2);
                if (prePreView instanceof EditText) {
                    EditText prePreEditText = (EditText) prePreView;
                    prePreEditText.setText(prePreEditText.getText().toString() + editText.getText().toString());
                    prePreEditText.requestFocus();
                    moveCursorToRight(prePreEditText);
                }
                rootLayout.removeView(preView);
                rootLayout.removeView(editText);
            } else { // 光标位于输入框起始位置且文本内容为空，不需要处理文本合并。
                View prePreView = rootLayout.getChildAt(currEditIndex - 2);
                if (prePreView instanceof EditText) {
                    EditText prePreEditText = (EditText) prePreView;
                    prePreEditText.requestFocus();
                    moveCursorToRight(prePreEditText);
                }
                rootLayout.removeView(preView);
                rootLayout.removeView(editText);
            }
        // 上一个 View 是输入框
        } else if (preView instanceof EditText) {
            EditText preEditText = (EditText) preView;
            // 判断在删除文本框时是否需要处理文本合并
            if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                preEditText.setText(preEditText.getText().toString() + editText.getText().toString());
                preEditText.requestFocus();
                moveCursorToRight(preEditText);
            }
            rootLayout.removeView(editText);
        }
    }

    public void insertImage(String path) {
        if (lastFocusEditText == null) {
            return;
        }
        int focusEditTextIndex = rootLayout.indexOfChild(lastFocusEditText);
        String editStr = lastFocusEditText.getText().toString();
        int startCursorIndex = lastFocusEditText.getSelectionStart();

        // 插入图片时文本框光标的位置
        if (startCursorIndex == 0) { // 开头
            if (focusEditTextIndex == 0) {
                addImageLayoutAtIndex(0, path);
                addEditTextAtIndex(0, "");
            } else {
                addImageLayoutAtIndex(focusEditTextIndex + 1, path);
                EditText editText = addEditTextAtIndex(focusEditTextIndex + 2, "");
                editText.requestFocus();
                moveCursorToRight(editText);
            }
        } else if (startCursorIndex == editStr.length() - 1) { // 结尾
            addImageLayoutAtIndex(focusEditTextIndex + 1, path);
            EditText editText = addEditTextAtIndex(focusEditTextIndex + 2, "");
            editText.requestFocus();
            moveCursorToRight(editText);
        } else { // 中间
            String str1 = editStr.substring(0, startCursorIndex);
            String str2 = editStr.substring(startCursorIndex);
            lastFocusEditText.setText(str1);
            addImageLayoutAtIndex(focusEditTextIndex + 1, path);
            EditText editText = addEditTextAtIndex(focusEditTextIndex + 2, str2);
            editText.requestFocus();
            moveCursorToRight(editText);
        }
    }

    private void addImageLayoutAtIndex(int index, String path) {
        FrameLayout imageLayout = createImageLayout(path);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rootLayout.addView(imageLayout, index, layoutParams);
    }

    private EditText addEditTextAtIndex(int index, String content) {
        EditText editText = createEditText();
        editText.setText(content);
        rootLayout.addView(editText, index, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return editText;
    }

    private FrameLayout createImageLayout(String path) {
        Size bitmapSize = getBitmapSize(path);

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(new File(path)).into(imageView);

        Size imageViewSize = getImageViewSize(bitmapSize);
        LayoutParams layoutParams = new LayoutParams(imageViewSize.getWidth(), imageViewSize.getHeight());

        // 设置图片在水平方向上的居中方式
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.addView(imageView, layoutParams);
        return frameLayout;
    }

    private EditText createEditText() {
        EditText editText = new EditText(getContext());
        editText.setTextSize(14f);
        editText.setBackground(null);
        editText.setTextColor(Color.parseColor("#FD9A6E"));
        editText.setHint("在此处输入日记内容");
        editText.setIncludeFontPadding(false);
        editText.setOnFocusChangeListener(focusChangeListener);
        editText.setOnKeyListener(keyListener);
        return editText;
    }

    private int getHorizontalSpace() {
        return ScreenUtils.dp2px(context, 12);
    }

    private void moveCursorToRight(EditText editText) {
        if (editText.length() == 0) {
            return;
        }
        editText.setSelection(editText.length());
    }

    private Size getImageViewSize(Size bitmapSize) {
        int destWidth = getWidth() - 2 * getHorizontalSpace();
        int imageWidth = 0;
        if (bitmapSize.getWidth() >= destWidth) {
            imageWidth = destWidth;
        } else {
            imageWidth = destWidth / 2;
        }
        int imageHeight = bitmapSize.getHeight() * imageWidth / bitmapSize.getWidth();
        return new Size(imageWidth, imageHeight);
    }

    private Size getBitmapSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        return new Size(options.outWidth, options.outHeight);
    }
}
