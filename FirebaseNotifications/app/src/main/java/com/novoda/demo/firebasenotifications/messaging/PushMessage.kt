package com.novoda.demo.firebasenotifications.messaging

import android.content.Intent
import android.os.Bundle
import com.google.firebase.messaging.RemoteMessage


class PushMessage(
        val title: String,
        val body: String,
        val customValues: Map<String, String>) {

    private val POSITIVE_ACTION_LABEL = "positive_action_label"
    private val POSITIVE_ACTION = "positive_action"
    private val NEGATIVE_ACTION_LABEL = "negative_action_label"
    private val NEGATIVE_ACTION = "negative_action"
    private val BACKGROUND_ACTION = "background_action"
    private val IS_INDEFINITE = "is_indefinite"

    fun getPositiveActionLabel(): String? = customValues[POSITIVE_ACTION_LABEL]
    fun getPositiveAction(): String? = customValues[POSITIVE_ACTION]
    fun getNegativeActionLabel(): String? = customValues[NEGATIVE_ACTION_LABEL]
    fun getNegativeAction(): String? = customValues[NEGATIVE_ACTION]
    fun getAction(): String? = customValues[BACKGROUND_ACTION]
    fun getIsIndefinite(): String? = customValues[IS_INDEFINITE]

    companion object {
        private val TITLE = "title"
        private val BODY = "body"

        fun messageAvailableIn(intent: Intent): Boolean {
            return intent.extras?.containsKey("google.message_id") ?: false
        }

        fun from(remoteMessage: RemoteMessage): PushMessage {
            val title = remoteMessage.notification?.title ?: "default title"
            val body = remoteMessage.notification?.body ?: "default body"

            return PushMessage(title, body, remoteMessage.data)
        }

        fun from(extras: Bundle?): PushMessage {
            var title = ""
            var body = ""
            val customValues = HashMap<String, String>()

            if (extras != null) {
                for (key in extras.keySet()) {
                    val value: String = extras[key].toString()
                    when (value) {
                        TITLE -> title = value
                        BODY -> body = value
                        else -> customValues.put(key, value)
                    }
                }
            }
            return PushMessage(title, body, customValues)
        }
    }

    fun toIntent(): Intent {
        val intent = Intent()
        intent.putExtra(TITLE, title)
        intent.putExtra(BODY, body)
        for (key in customValues.keys) {
            intent.putExtra(key, customValues[key])
        }
        return intent
    }

    fun toMap(): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(TITLE, title)
        map.put(BODY, body)
        map.putAll(customValues)
        return map
    }

}