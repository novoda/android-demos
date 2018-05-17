package com.novoda.androidstoreexample.dagger.categoryList

import com.novoda.androidstoreexample.activities.ProductListActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ProductListModule::class))
interface ProductListComponent {

    fun inject(productListActivity: ProductListActivity)
}