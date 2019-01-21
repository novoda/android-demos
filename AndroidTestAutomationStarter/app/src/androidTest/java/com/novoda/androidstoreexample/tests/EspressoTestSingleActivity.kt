package com.novoda.androidstoreexample.tests

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.activities.ProductDetailsActivity
import com.novoda.androidstoreexample.rules.MockServerTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class EspressoTestSingleActivity {
    private val activityTestRule = ActivityTestRule(ProductDetailsActivity::class.java,
            false,
            false)
    private val mockServerTestRule: MockServerTestRule = MockServerTestRule()

    @get:Rule
    var chain: TestRule = RuleChain.outerRule(activityTestRule)
            .around(mockServerTestRule)

    @Test
    fun MyTest() {
        mockServerTestRule.enqueueJson("categories/productdetails.json")
        activityTestRule.launchActivity(Intent())
        println("test")
    }
}