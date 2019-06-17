package com.novoda.kotlinapi.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Article(
    val id: Int,
    val title: String,
    val price: Int,
    val image: String,
    @get:JsonProperty("product_description") val productDescription: String
)
