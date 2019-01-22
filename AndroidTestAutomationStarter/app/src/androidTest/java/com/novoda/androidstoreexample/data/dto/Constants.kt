package com.novoda.androidstoreexample.data.dto

import com.novoda.androidstoreexample.BuildConfig

object Constants {
    object Urls {
        const val category = "${BuildConfig.API_URL}/categories"
        const val itemsPrefix = "${BuildConfig.API_URL}/category/"
        const val itemsSuffix = "items"
    }

    object Identifier {
        const val category = "categories"
        const val articles = "articles"
    }
}
