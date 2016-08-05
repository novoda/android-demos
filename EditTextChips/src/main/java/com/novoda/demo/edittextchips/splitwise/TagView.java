package com.novoda.demo.edittextchips.splitwise;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TagView extends TextView {

    private OnSelectedListener listener;

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected && listener != null) {
            listener.onSelected();
        }
    }

    void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    interface OnSelectedListener {
        void onSelected();
    }

}
