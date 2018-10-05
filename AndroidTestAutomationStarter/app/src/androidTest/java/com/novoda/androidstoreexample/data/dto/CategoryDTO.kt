package com.novoda.androidstoreexample.data.dto

import com.google.gson.Gson
import com.novoda.androidstoreexample.BuildConfig
import com.novoda.androidstoreexample.data.models.Category
import khttp.get
import org.json.JSONArray

class CategoryDTO {
    private val categoryIdentifier = "categories"
    private val categoriesUrl = "${BuildConfig.API_URL}/categories"
    private val gson = Gson()

    fun getAllCategories(): ArrayList<Category> {
        val categoryResponse = requestCategories()
        return mapJsonOnModel(categoryResponse)
    }

    private fun mapJsonOnModel(categoryResponse: JSONArray): ArrayList<Category> {
        val categories = arrayListOf<Category>()
        for (i in 0 until categoryResponse.length()) {
            categories.add(gson.fromJson(categoryResponse.getString(i), Category::class.java))
        }
        return categories
    }

    private fun requestCategories() =
            get(categoriesUrl).jsonObject.getJSONArray(categoryIdentifier)
}
