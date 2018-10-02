package com.novoda.androidstoreexample.tests

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.novoda.androidstoreexample.activities.MainActivity
import com.novoda.androidstoreexample.rules.MockServerTestRule
import org.junit.After
import org.junit.Rule
import org.junit.Test

class EspressoTestExampleWithMockServer {

    private val activityTestRule = MockServerTestRule(MainActivity::class.java,
            false,
            false)


    @get:Rule
    var activityRule: MockServerTestRule<MainActivity> = activityTestRule

    @Test
    fun categoryListCanBeAdapted() {
        activityRule.enqueueJson("categories/categories.json")
        activityTestRule.launchActivity(Intent())

        onView(withText("Thingies to wear")).check(matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        activityRule.shutdownServer()
    }

}
