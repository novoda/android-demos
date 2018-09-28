package com.novoda.androidstoreexample.tests

import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.activities.MainActivity
import com.novoda.androidstoreexample.pageobjects.*
import org.junit.Rule
import org.junit.Test

class EspressoTestExampleWithPageObjects {

    private val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = activityTestRule

    @Test
    fun firstNavigationTestWithPageObjects() {

        mainPage {
            navigateToProductList("HATS")
        }

        productList {
            navigateToProductDetails("hat white")
        }

        productDetailsPage {
            assertProductDetailsDisplayed()
        }
    }
}
