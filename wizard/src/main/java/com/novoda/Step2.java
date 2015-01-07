
package com.novoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Step2 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step2);

        Button nextStep = (Button)findViewById(R.id.goto3);
        nextStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra("STEP2RESULT", ((EditText)findViewById(R.id.step2value)).getText()
                        .toString());
                setResult(Activity.RESULT_OK, it);
                finish();
            }
        });
    }
}
