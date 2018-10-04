package com.novoda.androidstoreexample.data.dto

import com.google.gson.Gson
import com.novoda.androidstoreexample.BuildConfig
import com.novoda.androidstoreexample.data.models.Categories
import khttp.get

class CategoryDTO {

    private val gson = Gson()

    fun getAllCategories(): ArrayList<Categories> {
        val categories = arrayListOf<Categories>()
        val categoryResponse = get("${BuildConfig.API_URL}/categories").jsonObject.getJSONArray("categories")
        for (i in 0 until categoryResponse.length()) {
            categories.add(gson.fromJson(categoryResponse.getString(i), Categories::class.java))
        }
        return categories
    }
}