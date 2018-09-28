package com.novoda.androidstoreexample.pageobjects

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.ViewMatchers

fun productList(func: ProductListPageObject.() -> Unit) {
    ProductListPageObject().apply(func)
}

class ProductListPageObject {

    private val PRODUCT_LIST = onView(withId(R.id.productListView))

    fun navigateToProductDetails(product: String) = apply {
        val productMatcher = ViewMatchers.withProductTitle(product)

        PRODUCT_LIST.perform(RecyclerViewActions.actionOnHolderItem(productMatcher, ViewActions.click()))
    }
}
