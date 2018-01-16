package com.novoda.demo.firebasenotifications.messaging

class ForegroundRemoteMessageHandler(private val messenger: Messenger) {

    fun handleMessage(pushMessage: PushMessage) {
        val foregroundMessage = ForegroundMessage(pushMessage, ForegroundNotificationHandling.TOAST)
        messenger.publish(foregroundMessage)
    }
}
