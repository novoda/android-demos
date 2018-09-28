package com.novoda.androidstoreexample.pageobjects

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.novoda.androidstoreexample.R

fun productDetailsPage(func: ProductDetailsPageObject.() -> Unit) = ProductDetailsPageObject().apply(func)

class ProductDetailsPageObject {

    private val PRODUCT_DESCRIPTION = onView(withId(R.id.productDetailDescription))

    fun assertProductDetailsDisplayed() = apply {
        PRODUCT_DESCRIPTION.check(matches(isDisplayed()))
    }
}
