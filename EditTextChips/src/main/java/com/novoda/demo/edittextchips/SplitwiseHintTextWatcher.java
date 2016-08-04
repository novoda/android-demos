package com.novoda.demo.edittextchips;

import com.tokenautocomplete.HintSpan;

import android.text.Editable;
import android.text.Spanned;

class SplitwiseHintTextWatcher extends HintSafeTextWatcher {

    private SplitwiseTagsView splitwiseTagsView;
    private final CharSequence suffixHint;
    private final HintSpan suffixHintSpannable;

    SplitwiseHintTextWatcher(SplitwiseTagsView splitwiseTagsView, CharSequence suffixHint, HintSpan suffixHintSpannable) {
        this.splitwiseTagsView = splitwiseTagsView;
        this.suffixHint = suffixHint;
        this.suffixHintSpannable = suffixHintSpannable;
    }

    @Override
    void onTextChanged(Editable text) {
        String rawText = text.toString();
        if (!textContainsHint(text) && (rawText.isEmpty() || containsSpansOnly(rawText))) {
            addHint(text);
        } else if (doesNotContainJustSpansAndHint(text)) {
            removeHint(text);
        }
    }

    private boolean containsSpansOnly(String rawText) {
        return rawText.matches("(( )*,, )*");
    }

    boolean textContainsHint(Editable text) {
        if (text.length() < suffixHint.length()) {
            return false;
        }
        return text.getSpans(text.length() - suffixHint.length(), text.length(), HintSpan.class).length > 0;
    }


    private boolean doesNotContainJustSpansAndHint(Editable text) {
        if (textContainsHint(text)) {
            String rawText = text.toString();
            String rawTextWithoutHint = rawText.substring(0, rawText.length() - suffixHint.length());
            return !containsSpansOnly(rawTextWithoutHint);
        } else {
            return false;
        }
    }

    void addHint(Editable text) {
        text.insert(text.length(), suffixHint);
        text.setSpan(suffixHintSpannable, text.length() - suffixHint.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        splitwiseTagsView.setSelection(text.length() - suffixHint.length());
    }

    private void removeHint(Editable text) {
        text.removeSpan(suffixHintSpannable);
        text.replace(text.length() - suffixHint.length(), text.length(), "");
    }

    boolean isHintVisible(Editable text) {
        return textContainsHint(text);
    }

}
