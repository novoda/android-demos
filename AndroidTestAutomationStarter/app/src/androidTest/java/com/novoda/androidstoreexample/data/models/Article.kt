package com.novoda.androidstoreexample.data.models

import com.google.gson.annotations.SerializedName

data class Article(val id: Int,
                   val title: String,
                   val price: String,
                   val image: String,
                   @SerializedName("product_description")val productDescription: String)