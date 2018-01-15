package com.novoda.demo.firebasenotifications

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (googlePlayServicesNotAvailable()) {
            makeGooglePlayServicesAvailable()
        }
    }

    private fun googlePlayServicesNotAvailable(): Boolean {
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return result != ConnectionResult.SUCCESS
    }

    private fun makeGooglePlayServicesAvailable() {
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
    }
}
