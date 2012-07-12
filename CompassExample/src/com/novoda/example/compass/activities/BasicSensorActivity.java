package com.novoda.example.compass.activities;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.novoda.example.compass.NovoCompass;
import com.novoda.location.exception.NoProviderAvailable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public abstract class BasicSensorActivity extends SherlockFragmentActivity implements SensorEventListener {

    private Location location;

    public BroadcastReceiver freshLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            location = NovoCompass.getLocator().getLocation();
            Log.i("Loc", " location = " + location.getLatitude() + " " + location.getLongitude());
        }
    };

    private IntentFilter filter;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        filter = new IntentFilter();
        filter.addAction(NovoCompass.LOCATION_UPDATE_ACTION);

    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(freshLocationReceiver, filter);
        try {
            NovoCompass.getLocator().startLocationUpdates();
        } catch (NoProviderAvailable np) {
            // TODO add implementation
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

    }

}
