package com.novoda.androidstoreexample.mvp.listener

import com.novoda.androidstoreexample.services.ProductResponse

interface ProductListListener : BaseListener {

    fun onSuccess(productResponse: ProductResponse)
}