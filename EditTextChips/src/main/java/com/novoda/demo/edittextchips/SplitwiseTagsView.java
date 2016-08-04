package com.novoda.demo.edittextchips;

import com.tokenautocomplete.HintSpan;
import com.tokenautocomplete.TokenCompleteTextView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SplitwiseTagsView extends TokenCompleteTextView<SplitwiseActivity.Tag> {

    private final CharSequence suffixHint;
    private final SplitwiseHintTextWatcher hintTextWatcher;

    public SplitwiseTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        suffixHint = getHint();
        HintSpan suffixHintSpannable = new HintSpan(null, hintStyle(), (int) getTextSize(), getHintTextColors(), getHintTextColors());
        hintTextWatcher = new SplitwiseHintTextWatcher(this, suffixHint, suffixHintSpannable);

        hintTextWatcher.addHint(getText());
        addTextChangedListener(hintTextWatcher);
        setHint("");
    }

    private int hintStyle() {
        Typeface tf = getTypeface();
        int style = Typeface.NORMAL;
        if (tf != null) {
            style = tf.getStyle();
        }
        return style;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (isHintVisible() && isSelectingHint(selStart)) {
            //Don't let users select the hint
            setSelection(getText().length() - suffixHint.length());
        }
    }

    private boolean isHintVisible() {
        return hintTextWatcher != null && hintTextWatcher.isHintVisible(getText());
    }

    private boolean isSelectingHint(int selStart) {
        return selStart >= getText().length() - suffixHint.length();
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

    @Override
    public boolean enoughToFilter() {
        if (hintTextWatcher.textContainsHint(getText())) {
            return false;
        }
        return super.enoughToFilter();
    }

}
