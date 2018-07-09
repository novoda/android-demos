package com.novoda.androidstoreexample.tests

import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.activities.ProductListActivity
import com.novoda.androidstoreexample.pageobjects.HeaderPageObject
import org.junit.Test

class HeaderTests {
    private val activityTestRule = ActivityTestRule<ProductListActivity>(ProductListActivity::class.java)

    @Test
    fun actionBarIsDisplayed() {
        val headerPageObject = HeaderPageObject()
        headerPageObject.verifyHeader()
    }
}
