package com.novoda.demo.edittextchips.splitwise;

import com.novoda.demo.edittextchips.HintSafeTextWatcher;
import com.tokenautocomplete.HintSpan;

import android.text.Editable;
import android.text.Spanned;

class HintTextWatcher extends HintSafeTextWatcher {

    private final TagsView tagsView;
    private final CharSequence suffixHint;
    private final HintSpan suffixHintSpannable;

    HintTextWatcher(TagsView tagsView, CharSequence suffixHint, HintSpan suffixHintSpannable) {
        this.tagsView = tagsView;
        this.suffixHint = suffixHint;
        this.suffixHintSpannable = suffixHintSpannable;
    }

    @Override
    protected void onTextChanged(Editable text) {
        String rawText = text.toString();
        if (!textContainsHint(text) && isNotCurrentlyTyping(rawText)) {
            addHint(text);
        } else if (textContainsHint(text) && isCurrentlyTyping(text)) {
            removeHint(text);
        }
    }

    boolean textContainsHint(Editable text) {
        if (text.length() < suffixHint.length()) {
            return false;
        }
        return text.getSpans(hintStartPosition(text), text.length(), HintSpan.class).length > 0;
    }

    private int hintStartPosition(Editable text) {
        return text.length() - suffixHint.length();
    }

    private boolean isNotCurrentlyTyping(String rawText) {
        // means either the text is empty or we have only tokens (no half-written stuff)
        return rawText.isEmpty() || containsSpansOnly(rawText);
    }

    private boolean containsSpansOnly(String rawText) {
        // doesn't take into account the hint, just tokens
        return rawText.matches("(( )*,, )*");
    }

    private boolean isCurrentlyTyping(Editable text) {
        // in the middle of typing (there's text that isn't a token yet)
        return doesNotContainJustSpans(text);
    }

    private boolean doesNotContainJustSpans(Editable text) {
        String rawText = text.toString();
        String rawTextWithoutHint = rawText.substring(0, rawText.length() - suffixHint.length());
        return !containsSpansOnly(rawTextWithoutHint);
    }

    void addHint(Editable text) {
        text.insert(text.length(), suffixHint);
        text.setSpan(suffixHintSpannable, hintStartPosition(text), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tagsView.setSelection(hintStartPosition(text));
    }

    private void removeHint(Editable text) {
        text.removeSpan(suffixHintSpannable);
        text.replace(hintStartPosition(text), text.length(), "");
    }

    boolean isHintVisible(Editable text) {
        return textContainsHint(text);
    }

}
