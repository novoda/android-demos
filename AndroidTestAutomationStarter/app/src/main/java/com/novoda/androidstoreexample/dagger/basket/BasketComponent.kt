package com.novoda.androidstoreexample.dagger.basket

import com.novoda.androidstoreexample.activities.BasketActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(BasketModule::class))
interface BasketComponent {
    fun inject(basketActivity: BasketActivity)
}
