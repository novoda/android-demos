/*
Copyright 2009-2011 Urban Airship Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE URBAN AIRSHIP INC ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL URBAN AIRSHIP INC OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.novoda.urbanairship.demo;

import java.util.Calendar;
import java.util.Date;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TimePicker;

import com.urbanairship.analytics.InstrumentedActivity;
import com.urbanairship.push.PushManager;
import com.urbanairship.push.PushPreferences;

// This class represents the UI and implementation of the activity enabling users
// to set Quiet Time preferences.

public class PushPreferencesActivity extends InstrumentedActivity {
	
    CheckBox pushEnabled;
    CheckBox soundEnabled;
    CheckBox vibrateEnabled;
    CheckBox quietTimeEnabled;
    
    TimePicker startTime;
    TimePicker endTime;
    
    PushPreferences prefs = PushManager.shared().getPreferences();

    
    private void pushSettingsActive(boolean b) {
    	soundEnabled.setEnabled(b);
    	vibrateEnabled.setEnabled(b);
    }
    
    private void quietTimeSettingsActive(boolean b) {
    	startTime.setEnabled(b);
    	endTime.setEnabled(b);
    }
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window w = getWindow();
        w.requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.push_preferences_dialog);
        
        pushEnabled = (CheckBox) findViewById(R.id.push_enabled);
        soundEnabled = (CheckBox) findViewById(R.id.sound_enabled);
        vibrateEnabled = (CheckBox) findViewById(R.id.vibrate_enabled);
        quietTimeEnabled = (CheckBox) findViewById(R.id.quiet_time_enabled);
        
        startTime = (TimePicker) findViewById(R.id.start_time);
        endTime = (TimePicker) findViewById(R.id.end_time);

        startTime.setIs24HourView(DateFormat.is24HourFormat(this));
        endTime.setIs24HourView(DateFormat.is24HourFormat(this));
        
        pushEnabled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pushSettingsActive(((CheckBox)v).isChecked());
			}
        	
        });
        
        quietTimeEnabled.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				quietTimeSettingsActive(((CheckBox)v).isChecked());
			}
        });
        
    }

    // When the activity starts, we need to fetch and display the user's current
    // Push preferences in the view, if applicable.
    @Override
    public void onStart() {
        super.onStart();
        
        boolean isPushEnabled = prefs.isPushEnabled();
        pushEnabled.setChecked(isPushEnabled);
        soundEnabled.setChecked(prefs.isSoundEnabled());
        vibrateEnabled.setChecked(prefs.isVibrateEnabled());
        pushSettingsActive(isPushEnabled);
        
        boolean isQuietTimeEnabled = prefs.isQuietTimeEnabled();
        quietTimeEnabled.setChecked(isQuietTimeEnabled);
        quietTimeSettingsActive(isQuietTimeEnabled);
        
        //this will be null if a quiet time interval hasn't been set
        Date[] interval = prefs.getQuietTimeInterval();
        if(interval != null) {
	        startTime.setCurrentHour(interval[0].getHours());
	        startTime.setCurrentMinute(interval[0].getMinutes());
	        endTime.setCurrentHour(interval[1].getHours());
	        endTime.setCurrentMinute(interval[1].getMinutes());
        }
    }

    // When the activity is closed, save the user's Push preferences
    @Override
    public void onStop() {
        super.onStop();
        
        boolean isPushEnabledInActivity = pushEnabled.isChecked();
        boolean isQuietTimeEnabledInActivity = quietTimeEnabled.isChecked();
        
        if(isPushEnabledInActivity) {
        	PushManager.enablePush();
        }
        else {
        	PushManager.disablePush();
        }
       
        prefs.setSoundEnabled(soundEnabled.isChecked());
        prefs.setVibrateEnabled(vibrateEnabled.isChecked());
              
        prefs.setQuietTimeEnabled(isQuietTimeEnabledInActivity);
        
        if(isQuietTimeEnabledInActivity) {
        
	        // Grab the start date.
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.HOUR_OF_DAY, startTime.getCurrentHour());
	        cal.set(Calendar.MINUTE, startTime.getCurrentMinute());
	        Date startDate = cal.getTime();
	
	        // Prepare the end date.
	        cal = Calendar.getInstance();
	        cal.set(Calendar.HOUR_OF_DAY, endTime.getCurrentHour());
	        cal.set(Calendar.MINUTE, endTime.getCurrentMinute());
	        Date endDate = cal.getTime();
	        
	        prefs.setQuietTimeInterval(startDate, endDate);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // DO NOT REMOVE, just having it here seems to fix a weird issue with
        // Time picker where the fields would go blank on rotation.
    }

}
