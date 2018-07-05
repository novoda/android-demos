package com.novoda.androidstoreexample.models
import com.squareup.moshi.Json

data class Product(
    val id: Int,
    val title: String,
    val price: String,
    val image: String,
    @Json(name = "product_description") val productDescription: String
)