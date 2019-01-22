package com.novoda.androidstoreexample.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopService {

    @GET("categories")
    fun getCategories(): Call<CategoryResponse>

    @GET("categories/{categoryId}/articles")
    fun getProductsFromCategory(@Path("categoryId") categoryName: Int): Call<ProductResponse>

    @GET("articles/{articleId}")
    fun getItemForId(@Path("articleId") itemId: Int): Call<ProductDetailsResponse>
}
