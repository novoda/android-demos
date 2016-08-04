package com.novoda.demo.edittextchips;

import com.tokenautocomplete.HintSpan;
import com.tokenautocomplete.TokenCompleteTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SplitwiseTagsView extends TokenCompleteTextView<Tag> {

    private final CharSequence suffixHint;
    private final SplitwiseHintTextWatcher hintTextWatcher;
    private final SplitwiseTagViewAdapter adapter;

    public SplitwiseTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        suffixHint = getHint();
        HintSpan suffixHintSpannable = new HintSpan(null, hintStyle(), (int) getTextSize(), getHintTextColors(), getHintTextColors());
        hintTextWatcher = new SplitwiseHintTextWatcher(this, suffixHint, suffixHintSpannable);

        hintTextWatcher.addHint(getText());
        addTextChangedListener(hintTextWatcher);
        setHint("");

        adapter = new SplitwiseTagViewAdapter(context);
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
    protected View getViewForObject(Tag tag) {
        return adapter.getView(tag, (ViewGroup) getParent());
    }

    @Override
    protected Tag defaultObject(String completionText) {
        if (completionText.startsWith("#")) {
            return new Tag(completionText.replaceFirst("#", ""));
        } else {
            return new Tag(completionText);
        }
    }

    @Override
    public boolean enoughToFilter() {
        if (hintTextWatcher.textContainsHint(getText())) {
            return false;
        }
        return super.enoughToFilter();
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

}
