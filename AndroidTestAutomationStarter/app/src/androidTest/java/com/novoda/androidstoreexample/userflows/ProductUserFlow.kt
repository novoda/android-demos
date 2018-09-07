package com.novoda.androidstoreexample.userflows

import com.novoda.androidstoreexample.pageobjects.mainPage
import com.novoda.androidstoreexample.pageobjects.productDetailsPage
import com.novoda.androidstoreexample.pageobjects.productList

fun productUserFlow(func: ItemUserFlow.() -> Unit) { ItemUserFlow().apply(func) }

class ItemUserFlow {
    fun navigateToCategory(category: String) = apply {
        mainPage {
            navigateToProductList(category)
        }
    }

    fun openItemFromProductlist(product: String) = apply {
        productList {
            navigateToProductDetails(product)
        }
    }

    fun checkThatCorrectProductIsDisplayed() = apply {
        productDetailsPage {
            assertProductDetailsDisplayed()
        }
    }
}
