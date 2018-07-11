package com.novoda.androidstoreexample.mvp.interactor.impl

import com.novoda.androidstoreexample.mvp.interactor.ProductListInteractor
import com.novoda.androidstoreexample.mvp.listener.ProductListListener
import com.novoda.androidstoreexample.services.ProductResponse
import com.novoda.androidstoreexample.services.ShopService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class ProductListInteractorImpl @Inject constructor(val retrofit: Retrofit, private val apiService: ShopService) : ProductListInteractor {

    private var call: Call<ProductResponse>? = null

    override fun loadProductList(produListListener: ProductListListener, category: Int) {
        call = apiService.getProductsFromCategory(category).apply {
            enqueue(object : Callback<ProductResponse> {
                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                    produListListener.onFailure("Error while fetching")
                }

                override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                    if (response.isSuccessful) {
                        produListListener.onSuccess(response.body()!!)
                    } else {
                        produListListener.onFailure("Error while fetching data")
                    }
                }
            })
        }
    }

    override fun cancel() {
        call?.cancel()
    }

}
