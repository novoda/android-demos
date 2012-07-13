package com.novoda.example.compass.activities;

import java.util.concurrent.atomic.AtomicBoolean;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.novoda.example.compass.NovoCompass;
import com.novoda.example.compass.utils.CompassUtils;
import com.novoda.example.compass.utils.LowPassFilter;
import com.novoda.location.exception.NoProviderAvailable;

import android.hardware.SensorManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;

public abstract class BasicSensorActivity extends SherlockFragmentActivity implements SensorEventListener {

    private static final int FIRST_SENSOR_ITEM = 0;
    private static final AtomicBoolean ATOMIC_BOOLEAN = new AtomicBoolean(false);

    private static final float grav[] = new float[3]; 
    private static final float mag[] = new float[3]; 
    private static final float rotation[] = new float[9]; 
    private static final float orientation[] = new float[3]; 
    private static float smoothed[] = new float[3];

    private Location location;
    private GeomagneticField magField;
    private IntentFilter filter;
    private SensorManager sensorManager;
    private Sensor acclSensor;
    private Sensor magFieldSensor;
    
    private double bearing = 0;
    private int rotationDegrees = 0;


    public BroadcastReceiver freshLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            location = NovoCompass.getLocator().getLocation();
            magField = new GeomagneticField((float) location.getLatitude(), (float) location.getLongitude(),
                    (float) location.getAltitude(), System.currentTimeMillis());
        }
    };

  

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initLocationIntentFilter();

    }

    private void initLocationIntentFilter() {
        filter = new IntentFilter();
        filter.addAction(NovoCompass.LOCATION_UPDATE_ACTION);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(freshLocationReceiver, filter);
        registerAcclSensor();
        registerMagSensor();
        registerLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(freshLocationReceiver);
        unRegisterAcclSensor();
        unRegisterMagSensor();
        unRegisterLocationUpdates();
    }
    
    private void registerAcclSensor() {
        if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0) {
            acclSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(FIRST_SENSOR_ITEM);
            sensorManager.registerListener(this, acclSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    };

    private void registerMagSensor() {
        if (sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).size() > 0) {
            magFieldSensor = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(FIRST_SENSOR_ITEM);
            sensorManager.registerListener(this, magFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    };

    private void registerLocationUpdates() {
        try {
            NovoCompass.getLocator().startLocationUpdates();
        } catch (NoProviderAvailable np) {
            // TODO add implementation
        }
    }

    private void unRegisterLocationUpdates() {
        NovoCompass.getLocator().stopLocationUpdates();
    }

    private void unRegisterAcclSensor() {
        if (acclSensor == null) {
            return;
        }
        sensorManager.unregisterListener(this, acclSensor);
    }

    public void unRegisterMagSensor() {
        if (magFieldSensor == null) {
            return;
        }
        sensorManager.unregisterListener(this, magFieldSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!ATOMIC_BOOLEAN.compareAndSet(false, true))
            return;

        updateSensorValues(event);

        ATOMIC_BOOLEAN.set(false);
    }

    private void updateSensorValues(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            setAcclValues(event);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            setmagValues(event);
        }

        SensorManager.getRotationMatrix(rotation, null, grav, mag);
        SensorManager.getOrientation(rotation, orientation);
        double floatBearing = orientation[0];
        floatBearing = Math.toDegrees(floatBearing); 
        if (magField != null)
            floatBearing += magField.getDeclination();
        if (floatBearing < 0)
            floatBearing += 360;
        setBearing(floatBearing);
        setRotationInDegrees(floatBearing);
    }

    private void setRotationInDegrees(double floatBearing) {
        this.bearing = floatBearing;        
    }

    private void setBearing(double floatBearing) {
       this.rotationDegrees = CompassUtils.getRotationDegreesFromBearing(floatBearing);
    }
    
    public int getRotation(){
        return rotationDegrees;
    }
    
    public double getBearing(){
        return bearing;
    }

    private void setmagValues(SensorEvent event) {
        smoothed = LowPassFilter.filter(event.values, mag);
        mag[0] = smoothed[0];
        mag[1] = smoothed[1];
        mag[2] = smoothed[2];
    }

    private void setAcclValues(SensorEvent event) {
        smoothed = LowPassFilter.filter(event.values, grav);
        grav[0] = smoothed[0];
        grav[1] = smoothed[1];
        grav[2] = smoothed[2];
    }

}
