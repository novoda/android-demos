package com.novoda.androidstoreexample.pageobjects

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.novoda.androidstoreexample.R


class HeaderPageObject {
    private val HEADER: ViewInteraction = onView(withId(R.id.action_bar_container))

    fun verifyHeader() {
        HEADER.check(matches(isDisplayed()))
    }

}

