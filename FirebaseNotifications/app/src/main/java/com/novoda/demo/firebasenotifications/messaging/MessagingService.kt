package com.novoda.demo.firebasenotifications.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.novoda.demo.firebasenotifications.ServiceMessageDisplayer

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d("MessagingService", "PushMessage received: "+remoteMessage.toString())

        val messageDisplayer = ServiceMessageDisplayer(this)
        val messenger = Messenger(messageDisplayer)
        val foregroundMessage = ForegroundMessage(PushMessage.Companion.from(remoteMessage), ForegroundNotificationHandling.TOAST)
        messenger.publish(foregroundMessage)
    }
}
