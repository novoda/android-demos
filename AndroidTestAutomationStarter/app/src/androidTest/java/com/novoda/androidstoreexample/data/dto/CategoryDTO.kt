package com.novoda.androidstoreexample.data.dto

import com.google.gson.Gson
import com.novoda.androidstoreexample.BuildConfig
import com.novoda.androidstoreexample.data.models.Categories
import khttp.get
import org.json.JSONArray

class CategoryDTO {
    private val categoryIdentifier = "categories"
    private val categoriesUrl = "${BuildConfig.API_URL}/categories"
    private val gson = Gson()

    fun getAllCategories(): ArrayList<Categories> {
        val categoryResponse = requestCategories()
        return mapJsonOnModel(categoryResponse)
    }

    private fun mapJsonOnModel(categoryResponse: JSONArray): ArrayList<Categories> {
        val categories = arrayListOf<Categories>()
        for (i in 0 until categoryResponse.length()) {
            categories.add(gson.fromJson(categoryResponse.getString(i), Categories::class.java))
        }
        return categories
    }

    private fun requestCategories() =
            get(categoriesUrl).jsonObject.getJSONArray(categoryIdentifier)
}
