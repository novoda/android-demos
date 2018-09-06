package com.novoda.androidstoreexample.pageobjects

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.ViewMatchers

fun mainPage(func: MainActivityPageObject.() -> Unit) = MainActivityPageObject().apply(func)

class MainActivityPageObject {

    private val TITLE = onView(withId(R.id.titleTextView))
    private val CATEGORY_LIST = onView(withId(R.id.categoryListView))

    fun verifyTitle() = apply {
        TITLE.check(matches(isDisplayed()))
    }

    fun navigateToProductList(category: String) = apply {
        val categoryMatcher = ViewMatchers.withCategoryTitle(category)

        CATEGORY_LIST.perform(actionOnHolderItem(categoryMatcher, click()))
    }
}
