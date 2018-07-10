package com.novoda.androidstoreexample.mvp.interactor.impl

import com.novoda.androidstoreexample.mvp.interactor.CategoryListInteractor
import com.novoda.androidstoreexample.mvp.listener.CategoryListListener
import com.novoda.androidstoreexample.services.CategoryResponse
import com.novoda.androidstoreexample.services.ShopService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class CategoryListInteractorImpl @Inject constructor(val retrofit: Retrofit, private val apiService: ShopService) : CategoryListInteractor {

    private var call: Call<CategoryResponse>? = null

    override fun loadCategoryList(categoryListListener: CategoryListListener) {
        call = apiService.getCategories().apply {
            enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                    if (response.isSuccessful) {
                        categoryListListener.onSuccess(response.body()!!)
                    } else {
                        categoryListListener.onFailure("Error while fetching data")
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    categoryListListener.onFailure("Error while fetching data")
                }
            })
        }
    }

    override fun cancel() {
        call?.cancel()
    }
}
