package com.novoda.androidstoreexample.rules

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.EspressoHostModule
import com.novoda.androidstoreexample.dagger.App
import com.novoda.androidstoreexample.dagger.component.DaggerAppComponent
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import java.io.InputStream

class MockServerTestRule<T : Activity>(
        activityClass: Class<T>?,
        initialTouchMode: Boolean,
        launchActivity: Boolean) : ActivityTestRule<T>(activityClass, initialTouchMode, launchActivity) {

    internal val mockWebServer = MockWebServer()
    private val port = 9091

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val appComponent = DaggerAppComponent.builder().hostModule(EspressoHostModule(port)).build()
        App.component = appComponent
        mockWebServer.play(port)
    }

    override fun afterActivityFinished() {
        mockWebServer.shutdown()
        super.afterActivityFinished()
    }

    internal fun enqueueJson(resourceName: String) {
        mockWebServer.enqueue(createResponseFor(resourceName))
    }

    private fun convertStreamToString(input: InputStream): String {
        return input.bufferedReader().use { it.readText() }
    }

    private fun createResponseFor(resourceName: String): MockResponse {
        val resources = InstrumentationRegistry.getContext().resources.assets
        return MockResponse().setBody(convertStreamToString(resources.open(resourceName)))
    }

}