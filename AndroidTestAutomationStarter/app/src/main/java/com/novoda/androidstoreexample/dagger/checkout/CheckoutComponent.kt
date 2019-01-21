package com.novoda.androidstoreexample.dagger.checkout

import com.novoda.androidstoreexample.activities.CheckoutActivity
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(CheckoutModule::class))
interface CheckoutComponent {
    fun inject(checkoutActivity: CheckoutActivity)
}