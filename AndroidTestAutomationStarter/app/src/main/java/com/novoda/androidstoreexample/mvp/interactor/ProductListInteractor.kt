package com.novoda.androidstoreexample.mvp.interactor

import com.novoda.androidstoreexample.mvp.listener.ProductListListener

interface ProductListInteractor : BaseInteractor {

    fun loadProductList(produListListener: ProductListListener, category: Int)
}