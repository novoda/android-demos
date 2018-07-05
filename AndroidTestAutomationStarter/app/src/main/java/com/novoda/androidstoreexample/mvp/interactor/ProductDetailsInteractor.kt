package com.novoda.androidstoreexample.mvp.interactor

import com.novoda.androidstoreexample.mvp.listener.ProductDetailsListener

interface ProductDetailsInteractor : BaseInteractor {
    fun loadProductDetails(productDetailsListener: ProductDetailsListener, itemId: Int)
}