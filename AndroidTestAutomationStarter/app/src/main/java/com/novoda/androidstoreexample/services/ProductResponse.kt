package com.novoda.androidstoreexample.services

import com.novoda.androidstoreexample.models.Product
import java.util.*

data class ProductResponse(val articles: Array<Product>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductResponse

        if (!Arrays.equals(articles, other.articles)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(articles)
    }
}
