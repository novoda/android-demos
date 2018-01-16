package com.novoda.demo.firebasenotifications

import android.app.Activity
import android.os.Handler
import android.widget.Toast
import com.novoda.demo.firebasenotifications.messaging.Messenger


class MessageDisplayer(private val activity: Activity) : Messenger.MessageDisplayer {

    override fun displayToast(message: String) {
        Handler(activity.mainLooper).post({
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        })
    }

    override fun displaySnackbar(message: String, actionLabel: String?, actionData: String?, indefinite: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayDialog(title: String?, message: String, positiveText: String?, negativeText: String?, positiveData: String?, negativeData: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
