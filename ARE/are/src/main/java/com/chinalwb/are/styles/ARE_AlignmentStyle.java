package com.chinalwb.are.styles;

import android.content.Context;
import android.text.Editable;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.AlignmentSpan.Standard;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.chinalwb.are.Constants;
import com.chinalwb.are.Util;
import com.chinalwb.are.styles.toolbar.ARE_Toolbar;

public class ARE_AlignmentStyle extends ARE_ABS_FreeStyle {


    private Alignment mAlignment;

    public ARE_AlignmentStyle(Context context, EditText editText) {
        super(context);
        this.mEditText = editText;
    }

    boolean handleWholeText = false;

    public void alignLeft() {
        alignment(Alignment.ALIGN_NORMAL);
    }

    public void alignCenter() {
        alignment(Alignment.ALIGN_CENTER);
    }

    public void alignRight() {
        alignment(Alignment.ALIGN_OPPOSITE);
    }

    public void alignWholeTextRight() {
        handleWholeText = true;
        alignRight();
    }

    private void alignment(Alignment alignment) {
        mAlignment = alignment;
        EditText editText = getEditText();
        if (handleWholeText) {
            Editable editable = editText.getEditableText();
            Standard[] alignmentSpans = editable.getSpans(0, editable.length(), Standard.class);
            if (null != alignmentSpans) {
                for (Standard span : alignmentSpans) {
                    editable.removeSpan(span);
                }
            }

            AlignmentSpan alignSpan = new Standard(mAlignment);
            if (editable.length()==0) {
                editable.insert(0, Constants.ZERO_WIDTH_SPACE_STR);
            }
            editable.setSpan(alignSpan, 0, editable.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            int currentLine = Util.getCurrentCursorLine(editText);
            alignmentSingleLine(editText, currentLine);
        }
    }

    private void alignmentSingleLine(EditText editText, int currentLine) {
        int lineStart = Util.getThisLineStart(editText, currentLine);
        int lineEnd = Util.getThisLineEnd(editText, currentLine);

        Editable editable = editText.getEditableText();

        Standard[] alignmentSpans = editable.getSpans(lineStart, lineEnd, Standard.class);
        if (null != alignmentSpans) {
            for (Standard span : alignmentSpans) {
                editable.removeSpan(span);
            }
        }

        AlignmentSpan alignSpan = new Standard(mAlignment);
        if (lineStart == lineEnd) {
            editable.insert(lineStart, Constants.ZERO_WIDTH_SPACE_STR);
            lineEnd = Util.getThisLineEnd(editText, currentLine);
        }
        editable.setSpan(alignSpan, lineStart, lineEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    @Override
    public void setListenerForImageView(ImageView imageView) {

    }

    @Override
    public void applyStyle(Editable editable, int start, int end) {
        AlignmentSpan[] alignmentSpans = editable.getSpans(start, end, AlignmentSpan.class);
        if (null == alignmentSpans || alignmentSpans.length == 0) {
            return;
        }

        Alignment alignment = alignmentSpans[0].getAlignment();
        if (mAlignment != alignment) {
            return;
        }

        if (end > start) {
            //
            // User inputs
            //
            // To handle the \n case
            char c = editable.charAt(end - 1);
            if (c == Constants.CHAR_NEW_LINE) {
                int alignmentSpansSize = alignmentSpans.length;
                int previousAlignmentSpanIndex = alignmentSpansSize - 1;
                if (previousAlignmentSpanIndex > -1) {
                    AlignmentSpan previousAlignmentSpan = alignmentSpans[previousAlignmentSpanIndex];
                    int lastAlignmentSpanStartPos = editable.getSpanStart(previousAlignmentSpan);
                    if (end > lastAlignmentSpanStartPos) {
                        editable.removeSpan(previousAlignmentSpan);
                        editable.setSpan(previousAlignmentSpan, lastAlignmentSpanStartPos, end - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }

                    markLineAsAlignmentSpan(mAlignment);
                }
            } // #End of user types \n
        } else {
            //
            // User deletes
            int spanStart = editable.getSpanStart(alignmentSpans[0]);
            int spanEnd = editable.getSpanEnd(alignmentSpans[0]);

            if (spanStart >= spanEnd) {
                //
                // User deletes the last char of the span
                // So we think he wants to remove the span
                editable.removeSpan(alignmentSpans[0]);

                //
                // To delete the previous span's \n
                // So the focus will go to the end of previous span
                if (spanStart > 0) {
                    editable.delete(spanStart - 1, spanEnd);
                }
            }
        }
    }

    private void markLineAsAlignmentSpan(Alignment alignment) {
        EditText editText = getEditText();
        int currentLine = Util.getCurrentCursorLine(editText);
        int start = Util.getThisLineStart(editText, currentLine);
        int end = Util.getThisLineEnd(editText, currentLine);
        Editable editable = editText.getText();
        editable.insert(start, Constants.ZERO_WIDTH_SPACE_STR);
        start = Util.getThisLineStart(editText, currentLine);
        end = Util.getThisLineEnd(editText, currentLine);

        if (end < 1) {
            return;
        }

        if (editable.charAt(end - 1) == Constants.CHAR_NEW_LINE) {
            end--;
        }

        AlignmentSpan alignmentSpan = new Standard(alignment);
        editable.setSpan(alignmentSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    @Override
    public ImageView getImageView() {
        return null;
    }

    @Override
    public void setChecked(boolean isChecked) {
        // TODO Auto-generated method stub
    }
}
