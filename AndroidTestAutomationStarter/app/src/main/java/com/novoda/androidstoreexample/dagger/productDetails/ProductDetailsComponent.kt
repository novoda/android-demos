package com.novoda.androidstoreexample.dagger.productDetails

import com.novoda.androidstoreexample.activities.ProductDetailsActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ProductDetailsModule::class))
interface ProductDetailsComponent {
    fun inject(productDetailsActivity: ProductDetailsActivity)
}