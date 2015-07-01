package com.novoda.demo.optionaldependencies;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OptionalDependencies optionalDependencies = ((OptionalDependencyApplication) getApplication()).getOptionalDependencies();

        TextView paidContent = (TextView) findViewById(R.id.paid_content);
        optionalDependencies.updatePaidContent(paidContent);
    }
}
