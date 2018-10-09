package com.novoda.androidstoreexample.rules

import android.support.test.InstrumentationRegistry
import com.novoda.androidstoreexample.EspressoHostModule
import com.novoda.androidstoreexample.dagger.App
import com.novoda.androidstoreexample.dagger.component.DaggerAppComponent
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.InputStream


class MockServerTestRule(private val port: Int = 9091) : TestRule {

    private val mockWebServer = MockWebServer()

    override fun apply(base: Statement, description: Description?): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                val appComponent = DaggerAppComponent.builder().hostModule(EspressoHostModule(port)).build()
                App.component = appComponent
                mockWebServer.play(port)
                try {
                    base.evaluate()
                } finally {
                    shutdownServer()
                }
            }
        }
    }

    internal fun enqueueJson(resourceName: String) {
        mockWebServer.enqueue(createResponseFor(resourceName))
    }

    internal fun shutdownServer() {
        mockWebServer.shutdown()
    }

    private fun convertStreamToString(input: InputStream): String {
        return input.bufferedReader().use { it.readText() }
    }

    private fun createResponseFor(resourceName: String): MockResponse {
        val resources = InstrumentationRegistry.getContext().resources.assets
        return MockResponse().setBody(convertStreamToString(resources.open(resourceName)))
    }

}
