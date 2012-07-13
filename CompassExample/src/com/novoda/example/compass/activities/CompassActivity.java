package com.novoda.example.compass.activities;

import com.novoda.example.compass.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.widget.TextView;

public class CompassActivity extends BasicSensorActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        
    }
    
    @Override
    public void onSensorChanged(SensorEvent evt) {
        super.onSensorChanged(evt);
        updateText();
    }

    private void updateText() {
        TextView text = (TextView)findViewById(android.R.id.text1);
        String dirTxt = getDirectionFromBearing(getBearing());
        text.setText(" Direction = " + dirTxt + " Rotation in degress " + getRotation());
        
    }

    private String getDirectionFromBearing(double bearing) {
        int range = (int) (bearing / (360f / 16f));
        String dirTxt = "";
        if (range == 15 || range == 0)
            dirTxt = "N";
        else if (range == 1 || range == 2)
            dirTxt = "NE";
        else if (range == 3 || range == 4)
            dirTxt = "E";
        else if (range == 5 || range == 6)
            dirTxt = "SE";
        else if (range == 7 || range == 8)
            dirTxt = "S";
        else if (range == 9 || range == 10)
            dirTxt = "SW";
        else if (range == 11 || range == 12)
            dirTxt = "W";
        else if (range == 13 || range == 14)
            dirTxt = "NW";
        return dirTxt;
    }


}
