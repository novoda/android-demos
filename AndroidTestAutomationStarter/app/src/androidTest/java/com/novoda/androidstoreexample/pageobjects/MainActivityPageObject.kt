package com.novoda.androidstoreexample.pageobjects

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.ViewMatchers

class MainActivityPageObject {

    private val TITLE = onView(withId(R.id.titleTextView))

    fun verifyTitle() {
        TITLE.check(matches(isDisplayed()))
    }

    fun navigateToProductList() {
        val categoryMatcher = ViewMatchers.withCategoryTitle("HATS")

        onView(withId(R.id.categoryListView)).perform(actionOnHolderItem(categoryMatcher, click()))
    }
}
