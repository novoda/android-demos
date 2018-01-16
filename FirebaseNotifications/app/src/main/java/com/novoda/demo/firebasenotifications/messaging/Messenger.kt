package com.novoda.demo.firebasenotifications.messaging

import com.novoda.demo.firebasenotifications.messaging.ForegroundNotificationHandling.TOAST


class Messenger {
    lateinit var messageDisplayer: MessageDisplayer

    fun publish(foregroundMessage: ForegroundMessage) {

        when (foregroundMessage.foregroundNotificationHandling) {
            TOAST -> messageDisplayer.displayToast(foregroundMessage.pushMessage.body)
            else -> {
                // TODO
            }
        }
    }

    interface MessageDisplayer {
        fun displayToast(message: String)
        fun displaySnackbar(message: String, actionLabel: String?, actionData: String?, indefinite: Boolean)
        fun displayDialog(
                title: String?,
                message: String,
                positiveText: String?,
                negativeText: String?,
                positiveData: String?,
                negativeData: String?
        )
    }

}
