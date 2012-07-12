package com.novoda.example.compass;

import com.novoda.location.Locator;
import com.novoda.location.LocatorFactory;
import com.novoda.location.LocatorSettings;

import android.app.Application;

public class NovoCompass extends Application {
    
    
    public static final String PACKAGE_NAME = "com.novoda.example.compass";
    public static final String LOCATION_UPDATE_ACTION = "com.com.novoda.example.compas.ACTION_FRESH_LOCATION";
    private Locator locator;
    
    @Override
    public void onCreate() {
        super.onCreate();
        initLocationListener();
    }

    private void initLocationListener() {
        LocatorSettings settings = new LocatorSettings(PACKAGE_NAME, LOCATION_UPDATE_ACTION);
        settings.setUpdatesInterval(3 * 60 * 1000);
        settings.setUpdatesDistance(50);
        locator = LocatorFactory.getInstance();
        locator.prepare(getApplicationContext(), settings);
    }

}
