package com.novoda.androidstoreexample.tests

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.EspressoHostModule
import com.novoda.androidstoreexample.activities.MainActivity
import com.novoda.androidstoreexample.dagger.App.Companion.component
import com.novoda.androidstoreexample.dagger.component.DaggerAppComponent
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.InputStream

class MockServerTestExample {

    private val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, false, false)
    private val mockWebServer = MockWebServer()
    private val port = 9091

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = activityTestRule

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val appComponent = DaggerAppComponent.builder().hostModule(EspressoHostModule(port)).build()
        component = appComponent
        mockWebServer.play(port)
    }

    @Test
    fun categoryListCanBeAdapted() {
        mockWebServer.enqueue(createResponseFor("categories/categories.json"))
        activityTestRule.launchActivity(Intent())

        onView(withText("Thingies to wear")).check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun createResponseFor(resourceName: String): MockResponse {
        val resources = InstrumentationRegistry.getContext().resources.assets
        return MockResponse().setBody(convertStreamToString(resources.open(resourceName)))
    }

    private fun convertStreamToString(input: InputStream): String {
        return input.bufferedReader().use { it.readText() }
    }
}
