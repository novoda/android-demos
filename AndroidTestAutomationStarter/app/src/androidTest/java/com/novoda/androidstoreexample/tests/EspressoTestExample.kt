package com.novoda.androidstoreexample.tests

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.novoda.androidstoreexample.activities.MainActivity
import com.novoda.androidstoreexample.pageobjects.MainActivityPageObject
import com.novoda.androidstoreexample.pageobjects.ProductDetailsPageObject
import com.novoda.androidstoreexample.pageobjects.ProductListPageObject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EspressoTestExample {

    private val mainActivityPageObject = MainActivityPageObject()
    private val productListPageObject = ProductListPageObject()
    private val productDetailsPageObject = ProductDetailsPageObject()

    private val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = activityTestRule


    @Test
    fun firstNavigationTest() {

        mainActivityPageObject.navigateToProductList()

        productListPageObject.navigateToProductDetails()

        productDetailsPageObject.assertProductDetailsDisplayed()

    }
}
