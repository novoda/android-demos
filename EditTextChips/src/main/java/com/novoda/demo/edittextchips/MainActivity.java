package com.novoda.demo.edittextchips;

import com.novoda.demo.edittextchips.eyeem.EyeemActivity;
import com.novoda.demo.edittextchips.splitwise.SplitwiseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eyeem().setOnClickListener(listenerWith(EyeemActivity.intent(this)));
        splitwise().setOnClickListener(listenerWith(SplitwiseActivity.intent(this)));
    }

    private View eyeem() {
        return findViewById(R.id.eyeem);
    }

    private View splitwise() {
        return findViewById(R.id.splitwise);
    }

    private View.OnClickListener listenerWith(final Intent intent) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        };
    }

}
