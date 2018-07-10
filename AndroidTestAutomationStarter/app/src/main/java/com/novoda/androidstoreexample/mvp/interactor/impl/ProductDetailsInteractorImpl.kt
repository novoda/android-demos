package com.novoda.androidstoreexample.mvp.interactor.impl

import com.novoda.androidstoreexample.mvp.interactor.ProductDetailsInteractor
import com.novoda.androidstoreexample.mvp.listener.ProductDetailsListener
import com.novoda.androidstoreexample.services.ProductDetailsResponse
import com.novoda.androidstoreexample.services.ShopService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ProductDetailsInteractorImpl @Inject constructor(private val shopService: ShopService) : ProductDetailsInteractor {

    private var call: Call<ProductDetailsResponse>? = null

    override fun loadProductDetails(productDetailsListener: ProductDetailsListener, itemId: Int) {
        call = shopService.getItemForId(itemId).apply {
            enqueue(object : Callback<ProductDetailsResponse> {
                override fun onFailure(call: Call<ProductDetailsResponse>, t: Throwable) {
                    productDetailsListener.onFailure("Error")
                }

                override fun onResponse(call: Call<ProductDetailsResponse>, response: Response<ProductDetailsResponse>) {
                    if (response.isSuccessful) {
                        productDetailsListener.onSuccess(response.body()!!)
                    } else {
                        productDetailsListener.onFailure("Error")
                    }
                }
            })
        }
    }

    override fun cancel() {
        call?.cancel()
    }
}
