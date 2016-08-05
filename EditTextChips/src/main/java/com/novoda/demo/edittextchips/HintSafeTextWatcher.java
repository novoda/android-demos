package com.novoda.demo.edittextchips;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class HintSafeTextWatcher implements TextWatcher {

    private boolean selfUpdateInProgress;

    /**
     * According to the documentation modifying the text from this method is an error so we don't need to worry.
     */
    @Override
    public final void beforeTextChanged(CharSequence text, int start, int count, int after) {
        // no-op
    }

    /**
     * According to the documentation modifying the text from this method is an error so we don't need to worry.
     */
    @Override
    public final void onTextChanged(CharSequence text, int start, int before, int count) {
        // no-op
    }

    /**
     * Prevents text changes from becoming recursive by using a boolean flag.
     */
    @Override
    public final void afterTextChanged(Editable text) {
        if (selfUpdateInProgress) {
            return;
        }
        selfUpdateInProgress = true;
        onTextChanged(text);
        selfUpdateInProgress = false;
    }

    protected abstract void onTextChanged(Editable text);

}
