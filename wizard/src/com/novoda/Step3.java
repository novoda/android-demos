
package com.novoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Step3 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step3);

        Button nextStep = (Button)findViewById(R.id.goToFinish);
        nextStep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent it = new Intent();
                it.putExtra("STEP3RESULT", ((EditText)findViewById(R.id.step3value)).getText()
                        .toString());
                setResult(Activity.RESULT_OK, it);
                finish();
            }
        });
    }
}
