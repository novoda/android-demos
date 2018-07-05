package com.novoda.androidstoreexample.mvp.interactor.impl

import com.novoda.androidstoreexample.services.ProductResponse
import com.novoda.androidstoreexample.mvp.interactor.ProductListInteractor
import com.novoda.androidstoreexample.mvp.listener.ProductListListener
import com.novoda.androidstoreexample.services.ShopService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ProductListInteractorImpl : ProductListInteractor {
    val retrofit: Retrofit
    val apiService: ShopService

    lateinit var call: Call<ProductResponse>

    @Inject
    constructor(retrofit: Retrofit, apiService: ShopService) {
        this.retrofit = retrofit
        this.apiService = apiService
    }

    override fun cancel() {
        call.cancel()
    }

    override fun loadProductList(produListListener: ProductListListener, category: Int) {
        call = apiService.getProductsFromCategory(category)
        call.enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>?, t: Throwable?) {
                produListListener.onFailure("Error while fetching")
            }

            override fun onResponse(call: Call<ProductResponse>?, response: Response<ProductResponse>?) {
                if (response != null && response.isSuccessful) {
                    produListListener.onSuccess(response.body()!!)
                } else {
                    produListListener.onFailure("Error while fetching data")
                }
            }
        })
    }
}