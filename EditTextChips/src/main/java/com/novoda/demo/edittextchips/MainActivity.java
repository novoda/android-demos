package com.novoda.demo.edittextchips;

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
        klinter().setOnClickListener(listenerWith(KlinkerActivity.intent(this)));
        doodle().setOnClickListener(listenerWith(DoodleActivity.intent(this)));
        plumillon().setOnClickListener(listenerWith(PlumillionActivity.intent(this)));
        splitwise().setOnClickListener(listenerWith(SplitwiseActivity.intent(this)));
    }

    private View eyeem() {
        return findViewById(R.id.eyeem);
    }

    private View klinter() {
        return findViewById(R.id.klinker);
    }

    private View doodle() {
        return findViewById(R.id.doodle);
    }

    private View plumillon() {
        return findViewById(R.id.plumillon);
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
