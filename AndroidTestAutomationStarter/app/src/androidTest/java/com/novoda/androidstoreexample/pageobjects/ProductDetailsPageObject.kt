package com.novoda.androidstoreexample.pageobjects

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.novoda.androidstoreexample.R

class ProductDetailsPageObject {

    fun assertProductDetailsDisplayed() {
        onView(withId(R.id.productDetailDescription)).check(matches(isDisplayed()))
    }
}
