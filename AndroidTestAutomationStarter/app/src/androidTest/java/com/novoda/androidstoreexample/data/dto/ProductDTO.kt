package com.novoda.androidstoreexample.data.dto

import com.novoda.androidstoreexample.models.Product
import com.squareup.moshi.Moshi
import khttp.get
import org.json.JSONArray

class ProductDTO {
    private val moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter(Product::class.java)

    fun getArticlesForCategory(id: Int): ArrayList<Product> {
        val articleJson = requestProductJson(id)
        return mapJsonOnModel(articleJson)
    }

    private fun requestProductJson(id: Int): JSONArray {
        val articleUrl = "${Constants.Urls.itemsPrefix}$id/${Constants.Urls.itemsSuffix}"
        return get(articleUrl)
                .jsonObject.getJSONArray(Constants.Identifier.articles)
    }

    private fun mapJsonOnModel(articleResponse: JSONArray): ArrayList<Product> {
        val articles = arrayListOf<Product>()
        for (i in 0 until articleResponse.length()) {
            articles.add(jsonAdapter.fromJson(articleResponse.getString(i)))
        }
        return articles
    }
}
