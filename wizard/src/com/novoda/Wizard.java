package com.novoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Wizard extends Activity {

    private TextView step1result, step2result, step3result;

    public static final String INTENT_STEP1 = "com.novoda.STEP1";
    public static final String INTENT_STEP2 = "com.novoda.STEP2";
    public static final String INTENT_STEP3 = "com.novoda.STEP3";

    private static final int STEP1 = 1;
    private static final int STEP2 = 2;
    private static final int STEP3 = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard);
        
        this.step1result = (TextView)findViewById(R.id.step1result);
        this.step2result = (TextView)findViewById(R.id.step2result);
        this.step3result = (TextView)findViewById(R.id.step3result);  
        
        startActivityForResult(new Intent(Wizard.INTENT_STEP1), STEP1);        
    }
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case STEP1:
                this.step1result.setText(data.getStringExtra("STEP1RESULT"));
                startActivityForResult(new Intent(Wizard.INTENT_STEP2), STEP2);    
                break;
            case STEP2:
                this.step2result.setText(data.getStringExtra("STEP2RESULT"));
                startActivityForResult(new Intent(Wizard.INTENT_STEP3), STEP3);    
                break;
            case STEP3:
                this.step3result.setText(data.getStringExtra("STEP3RESULT"));
                break;
        }
    }
}