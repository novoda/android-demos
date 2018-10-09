package com.novoda.androidstoreexample.tests

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.activities.MainActivity
import com.novoda.androidstoreexample.rules.MockServerTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule


class EspressoTestExampleWithMockServer {

    private val activityTestRule = ActivityTestRule(MainActivity::class.java,
            false,
            false)
    private val mockServerTestRule: MockServerTestRule = MockServerTestRule()

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(activityTestRule)
            .around(mockServerTestRule)

    @Test
    fun categoryListCanBeAdapted() {
        mockServerTestRule.enqueueJson("categories/categories.json")
        activityTestRule.launchActivity(Intent())

        onView(withText("Thingies to wear")).check(matches(isDisplayed()))
    }
}
