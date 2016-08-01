package com.novoda.demo.edittextchips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SplitwiseActivity extends Activity {

    static Intent intent(Context context) {
        return new Intent(context, SplitwiseActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
