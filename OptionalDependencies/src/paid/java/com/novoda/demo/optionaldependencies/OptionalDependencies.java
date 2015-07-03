package com.novoda.demo.optionaldependencies;

import android.widget.TextView;

public class OptionalDependencies extends BaseOptionalDependencies {
    @Override
    public void updatePaidContent(TextView textView) {
        textView.setText(R.string.paid_content);
    }
}
