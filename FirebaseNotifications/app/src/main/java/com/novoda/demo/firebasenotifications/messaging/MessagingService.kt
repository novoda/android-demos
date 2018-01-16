package com.novoda.demo.firebasenotifications.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.novoda.demo.firebasenotifications.FirebaseNotificationsApplication

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("MessagingService", "PushMessage received: "+remoteMessage.toString())

        val foregroundMessageHandler = ForegroundRemoteMessageHandler((application as FirebaseNotificationsApplication).messenger)

        val pushMessage = remoteMessage.toPushMessage()
        foregroundMessageHandler.handleMessage(pushMessage)
    }
}
