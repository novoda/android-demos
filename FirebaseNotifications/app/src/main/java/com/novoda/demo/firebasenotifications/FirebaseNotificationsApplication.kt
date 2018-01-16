package com.novoda.demo.firebasenotifications

import android.app.Application
import com.novoda.demo.firebasenotifications.messaging.Messenger

class FirebaseNotificationsApplication : Application() {

    lateinit var messenger : Messenger

    override fun onCreate() {
        super.onCreate()
        messenger = Messenger()
    }
}

