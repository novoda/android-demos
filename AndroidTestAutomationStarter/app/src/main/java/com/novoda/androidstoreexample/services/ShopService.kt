package com.novoda.androidstoreexample.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ShopService {

    @GET("categories")
    fun getCategories(): Call<CategoryResponse>

    @GET("category/{categoryId}/items")
    fun getProductsFromCategory(@Path("categoryId") categoryName: Int): Call<ProductResponse>

    @GET("items/{itemId}")
    fun getItemForId(@Path("itemId") itemId: Int): Call<ProductDetailsResponse>
}