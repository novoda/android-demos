package com.novoda.example.compass.activities;

import com.novoda.example.compass.R;
import com.novoda.example.compass.R.layout;
import com.novoda.example.compass.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CompassActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_compass, menu);
        return true;
    }

}
