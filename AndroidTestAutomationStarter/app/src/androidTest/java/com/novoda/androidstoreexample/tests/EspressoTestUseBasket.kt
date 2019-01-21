package com.novoda.androidstoreexample.tests

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import com.novoda.androidstoreexample.EspressoBasketModule
import com.novoda.androidstoreexample.activities.BasketActivity
import com.novoda.androidstoreexample.activities.MainActivity
import com.novoda.androidstoreexample.dagger.App
import com.novoda.androidstoreexample.dagger.component.DaggerAppComponent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EspressoTestUseBasket {
    private val activityTestRule = ActivityTestRule<BasketActivity>(BasketActivity::class.java, false, false)


    @get:Rule
    var activityRule: ActivityTestRule<BasketActivity> = activityTestRule

    @Before
    fun setUp() {
        val appComponent = DaggerAppComponent.builder().basketServiceModule(EspressoBasketModule()).build()
        App.component = appComponent
        activityRule.launchActivity(Intent())
    }

    @Test
    fun name() {
        println("test")
    }
}