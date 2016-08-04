package com.novoda.demo.edittextchips.splitwise;

import com.novoda.demo.edittextchips.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

class TagViewAdapter {

    private final Context context;

    TagViewAdapter(Context context) {
        this.context = context;
    }

    View getView(Tag tag, final ViewGroup container) {
        LayoutInflater l = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TagView view = (TagView) l.inflate(R.layout.item_view_tag, container, false);
        view.setText(tag.toString());
        view.setOnSelectedListener(new TagView.OnSelectedListener() {
            @Override
            public void onSelected() {
                showKeyboard(container);
            }
        });
        return view;
    }

    private void showKeyboard(ViewGroup container) {
        InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.showSoftInput(container.findViewById(R.id.tags), 0);
    }

}
