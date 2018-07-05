package com.novoda.androidstoreexample.tests

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.novoda.androidstoreexample.activities.ProductListActivity
import com.novoda.androidstoreexample.pageobjects.HeaderPageObject
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HeaderTests {
    private val activityTestRule = ActivityTestRule<ProductListActivity>(ProductListActivity::class.java)

    @Test
    fun actionBarIsDisplayed() {
        val headerPageObject = HeaderPageObject()
        headerPageObject.verifyHeader()
    }
}
