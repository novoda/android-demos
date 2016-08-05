package com.novoda.demo.edittextchips.splitwise;

import com.tokenautocomplete.HintSpan;
import com.tokenautocomplete.TokenCompleteTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TagsView extends TokenCompleteTextView<Tag> {

    private final CharSequence suffixHint;
    private final HintTextWatcher hintTextWatcher;
    private final TagViewAdapter adapter;

    public TagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        suffixHint = getHint();
        HintSpan suffixHintSpannable = new HintSpan(null, hintStyle(), (int) getTextSize(), getHintTextColors(), getHintTextColors());
        hintTextWatcher = new HintTextWatcher(this, suffixHint, suffixHintSpannable);

        hintTextWatcher.addHint(getText());
        addTextChangedListener(hintTextWatcher);
        setHint("");

        adapter = new TagViewAdapter(context);
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
            // Don't let users select the hint
            setSelection(hintStartPosition());
        }
    }

    private boolean isHintVisible() {
        return hintTextWatcher != null && hintTextWatcher.isHintVisible(getText());
    }

    private boolean isSelectingHint(int selStart) {
        return selStart >= hintStartPosition();
    }

    private int hintStartPosition() {
        return getText().length() - suffixHint.length();
    }

}
