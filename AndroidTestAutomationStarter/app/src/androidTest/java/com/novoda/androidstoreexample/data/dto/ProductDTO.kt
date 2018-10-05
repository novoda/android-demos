package com.novoda.androidstoreexample.data.dto

import com.google.gson.Gson
import com.novoda.androidstoreexample.data.models.Product
import com.novoda.testautomationstarter.test.BuildConfig
import khttp.get
import org.json.JSONArray

class ProductDTO {
    private val gson = Gson()
    private val productsIdentifier = "products"

    fun getArticlesForCategory(id: Int): ArrayList<Product> {
        val articleJson = requestProductJson(id)
        return mapJsonOnModel(articleJson)
    }

    private fun requestProductJson(id: Int): JSONArray {
        val articleUrl = "${BuildConfig.API_URL}/category/$id/items"
        return get(articleUrl)
                .jsonObject.getJSONArray(productsIdentifier)
    }

    private fun mapJsonOnModel(articleResponse: JSONArray): ArrayList<Product> {
        val articles = arrayListOf<Product>()
        for (i in 0 until articleResponse.length()) {
            articles.add(gson.fromJson(articleResponse.getString(i), Product::class.java))
        }
        return articles
    }
}
