package com.novoda.androidstoreexample.data.dto

import com.novoda.androidstoreexample.models.Category
import com.squareup.moshi.Moshi
import khttp.get
import org.json.JSONArray

class CategoryDTO {
    private val moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter(Category::class.java)

    fun getAllCategories(): ArrayList<Category> {
        val categoryResponse = requestCategories()
        return mapJsonOnModel(categoryResponse)
    }

    private fun mapJsonOnModel(categoryResponse: JSONArray): ArrayList<Category> {
        val categories = arrayListOf<Category>()
        for (i in 0 until categoryResponse.length()) {
            categories.add(jsonAdapter.fromJson(categoryResponse.getString(i)))
        }
        return categories
    }

    private fun requestCategories() =
            get(Constants.Urls.category).jsonObject.getJSONArray(Constants.Identifier.category)
}
