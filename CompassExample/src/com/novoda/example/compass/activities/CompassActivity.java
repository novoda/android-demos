package com.novoda.example.compass.activities;

import com.actionbarsherlock.view.Menu;
import com.novoda.example.compass.R;
import com.novoda.example.compass.utils.CompassUtils;
import com.novoda.example.compass.view.RotatableImageView;
import com.novoda.example.compass.view.RotatedImageView;

import android.hardware.SensorEvent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CompassActivity extends BasicSensorActivity {

    private boolean isCompassEnabled;
    private RotatableImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
         image = (RotatableImageView) findViewById(R.id.image);
        isCompassEnabled = false;
        refreshText();
    }

    private void refreshText() {
        String text = isCompassEnabled ? (" Direction = " + CompassUtils.getDirectionFromBearing(getBearing())
                + " Rotation in degress " + getRotation()) : "Compass disabled";
        updateText(text);
    }
    
    private void refreshImageRotation() {
        if (isCompassEnabled){ 
            image.setRotationInDegrees(getRotation());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent evt) {
        super.onSensorChanged(evt);
        refreshText();
        refreshImageRotation();
    }

 

    private void updateText(String textString) {
        TextView text = (TextView) findViewById(android.R.id.text1);
        text.setText(textString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_compass, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_enable_compass_rotation:
            isCompassEnabled = !isCompassEnabled;
            image.useCompassToRotate(isCompassEnabled);
            String text = isCompassEnabled ? "Compass rotation has been enabled!"
                    : "Compass rotation has been disabled";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
