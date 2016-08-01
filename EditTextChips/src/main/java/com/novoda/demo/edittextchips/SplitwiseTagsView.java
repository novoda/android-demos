package com.novoda.demo.edittextchips;

import com.tokenautocomplete.TokenCompleteTextView;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SplitwiseTagsView extends TokenCompleteTextView<SplitwiseActivity.Tag> {

    public SplitwiseTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(SplitwiseActivity.Tag tag) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.item_view_tag, (ViewGroup) getParent(), false);
        view.setText(tag.toString());
        return view;
    }

    @Override
    protected SplitwiseActivity.Tag defaultObject(String completionText) {
        if (completionText.startsWith("#")) {
            return new SplitwiseActivity.Tag(completionText.replaceFirst("#", ""));
        } else {
            return new SplitwiseActivity.Tag(completionText);
        }
    }

}
