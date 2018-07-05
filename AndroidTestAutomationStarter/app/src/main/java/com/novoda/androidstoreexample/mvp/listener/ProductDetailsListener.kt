package com.novoda.androidstoreexample.mvp.listener

import com.novoda.androidstoreexample.services.ProductDetailsResponse

interface ProductDetailsListener : BaseListener {
    fun onSuccess(response: ProductDetailsResponse)
}