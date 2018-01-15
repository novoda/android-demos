package com.novoda.demo.firebasenotifications.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d("MessagingService", "PushMessage received: "+remoteMessage.toString())


        super.onMessageReceived(remoteMessage)
    }
}
