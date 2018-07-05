package com.novoda.androidstoreexample.mvp.interactor.impl

import com.novoda.androidstoreexample.mvp.interactor.ProductDetailsInteractor
import com.novoda.androidstoreexample.mvp.listener.ProductDetailsListener
import com.novoda.androidstoreexample.services.ProductDetailsResponse
import com.novoda.androidstoreexample.services.ShopService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ProductDetailsInterActorImpl : ProductDetailsInteractor {
    private val retrofit: Retrofit
    private val shopService: ShopService

    private lateinit var call: Call<ProductDetailsResponse>

    @Inject
    constructor(retrofit: Retrofit, shopService: ShopService) {
        this.retrofit = retrofit
        this.shopService = shopService
    }

    override fun cancel() {
        call.cancel()
    }

    override fun loadProductDetails(productDetailsListener: ProductDetailsListener, itemId: Int) {
        call = shopService.getItemForId(itemId)
        call.enqueue(object : Callback<ProductDetailsResponse> {
            override fun onFailure(call: Call<ProductDetailsResponse>?, t: Throwable?) {
                productDetailsListener.onFailure("Error")
            }

            override fun onResponse(call: Call<ProductDetailsResponse>?, response: Response<ProductDetailsResponse>?) {
                if (response != null && response.isSuccessful) {
                    productDetailsListener.onSuccess(response.body()!!)
                } else {
                    productDetailsListener.onFailure("Error")
                }
            }
        })
    }
}