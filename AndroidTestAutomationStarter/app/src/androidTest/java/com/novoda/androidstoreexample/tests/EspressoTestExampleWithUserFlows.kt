package com.novoda.androidstoreexample.tests

import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.activities.MainActivity
import com.novoda.androidstoreexample.pageobjects.MainActivityPageObject
import com.novoda.androidstoreexample.pageobjects.ProductDetailsPageObject
import com.novoda.androidstoreexample.pageobjects.ProductListPageObject
import com.novoda.androidstoreexample.userflows.productUserFlow
import org.junit.Rule
import org.junit.Test

class EspressoTestExampleWithUserFlows {
    private val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = activityTestRule

    @Test
    fun firstNavigationTestWithUserFlows() {
        val category = "HATS"
        val product = "hat white"

        productUserFlow {
            navigateToCategory(category)
            openItemFromProductlist(product)
            checkThatCorrectProductIsDisplayed()
        }
    }
}
