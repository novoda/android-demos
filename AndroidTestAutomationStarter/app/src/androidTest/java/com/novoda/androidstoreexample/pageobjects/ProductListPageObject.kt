package com.novoda.androidstoreexample.pageobjects

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.ViewMatchers


class ProductListPageObject {

    fun navigateToProductDetails() {
        val productMatcher = ViewMatchers.withProductTitle("hat white")

        onView(withId(R.id.productListView)).perform( RecyclerViewActions.actionOnHolderItem(productMatcher, ViewActions.click()))
    }

}
