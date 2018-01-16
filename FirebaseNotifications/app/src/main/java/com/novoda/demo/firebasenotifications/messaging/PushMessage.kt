package com.novoda.demo.firebasenotifications.messaging

import android.content.Intent
import android.os.Bundle
import com.google.firebase.messaging.RemoteMessage

fun RemoteMessage.toPushMessage(): PushMessage {
    val title = notification?.title
    val body = notification?.body ?: "default body"
    val data = data ?: HashMap()

    return PushMessage(title, body, data)
}

fun Bundle.toPushMessage(): PushMessage {
    var title: String? = null
    var body = ""
    val customValues = HashMap<String, String>()

    keySet().forEach { key ->
        val value: String = get(key).toString()
        when (value) {
            PushMessage.TITLE -> title = value
            PushMessage.BODY -> body = value
            else -> customValues.put(key, value)
        }
    }

    return PushMessage(title, body, customValues)
}

fun Intent.isPushMessageAvailable(): Boolean {
    return extras?.containsKey("google.message_id") ?: false
}

class PushMessage(
        val title: String?,
        val body: String,
        private val customValues: Map<String, String>) {

    fun getPositiveActionLabel(): String? = customValues[POSITIVE_ACTION_LABEL]
    fun getPositiveAction(): String? = customValues[POSITIVE_ACTION]
    fun getNegativeActionLabel(): String? = customValues[NEGATIVE_ACTION_LABEL]
    fun getNegativeAction(): String? = customValues[NEGATIVE_ACTION]
    fun getAction(): String? = customValues[BACKGROUND_ACTION]
    fun getIsIndefinite(): String? = customValues[IS_INDEFINITE]

    companion object {
        val TITLE = "title"
        val BODY = "body"
        private val POSITIVE_ACTION_LABEL = "positive_action_label"
        private val POSITIVE_ACTION = "positive_action"
        private val NEGATIVE_ACTION_LABEL = "negative_action_label"
        private val NEGATIVE_ACTION = "negative_action"
        private val BACKGROUND_ACTION = "background_action"
        private val IS_INDEFINITE = "is_indefinite"
    }

    fun toIntent(): Intent {
        val intent = Intent()
        intent.putExtra(TITLE, title)
        intent.putExtra(BODY, body)

        customValues.keys.forEach { key ->
            intent.putExtra(key, customValues[key])
        }

        return intent
    }

    fun toMap(): Map<String, String> {
        val map = HashMap<String, String>()

        title?.let {
            map.put(TITLE, it)
        }

        map.put(BODY, body)
        map.putAll(customValues)
        return map
    }

}
