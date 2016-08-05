package com.novoda.demo.edittextchips.eyeem;

import com.eyeem.chips.AutocompletePopover;
import com.eyeem.chips.ChipsEditText;
import com.novoda.demo.edittextchips.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;

public class EyeemActivity extends Activity {

    private static final ArrayList<String> TAG_SUGGESTIONS = new ArrayList<String>() {{
        add("tagWeir");
        add("tagSmith");
        add("tagJordan");
        add("tagPeterson");
        add("tagJohnson");
        add("tagAnderson");
    }};

    private ChipsEditText chips;
    private AutocompletePopover popover;

    public static Intent intent(Context context) {
        return new Intent(context, EyeemActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eyeem);
        chips = (ChipsEditText) findViewById(R.id.chips);
        popover = (AutocompletePopover) findViewById(R.id.popover);

        chips.setAutocompleteResolver(demoResolver());
        chips.setAutocomplePopover(popover);
        chips.addTextChangedListener(textWatcher());
        popover.setChipsEditText(chips);

        showSuggestions(chips, popover);
    }

    private ChipsEditText.AutocompleteResolver demoResolver() {
        return new ChipsEditText.AutocompleteResolver() {
            @Override
            public ArrayList<String> getSuggestions(String query) throws Exception {
                return TAG_SUGGESTIONS;
            }

            @Override
            public ArrayList<String> getDefaultSuggestions() {
                return TAG_SUGGESTIONS;
            }
        };
    }

    private TextWatcher textWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lastCharacterIsSeparator(s)) {
                    showSuggestions(chips, popover);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no-op
            }
        };
    }

    private boolean lastCharacterIsSeparator(CharSequence s) {
        int lastIndex = s.toString().length() - 1;
        return s.toString().lastIndexOf(',') == lastIndex;
    }

    private void showSuggestions(ChipsEditText chips, AutocompletePopover popover) {
        chips.startManualMode();
        popover.show();
    }

}
