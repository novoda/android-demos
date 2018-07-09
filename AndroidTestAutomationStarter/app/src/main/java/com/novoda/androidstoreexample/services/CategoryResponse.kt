package com.novoda.androidstoreexample.services

import com.novoda.androidstoreexample.models.Category
import java.util.*

data class CategoryResponse(val categories: Array<Category>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryResponse

        if (!Arrays.equals(categories, other.categories)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(categories)
    }
}
