package com.novoda.androidstoreexample.data.dto

import com.google.gson.Gson
import com.novoda.androidstoreexample.data.models.Article
import com.novoda.testautomationstarter.test.BuildConfig
import khttp.get
import org.json.JSONArray

class ArticleDTO {
    private val gson = Gson()
    private val productsIdentifier = "products"

    fun getArticlesForCategory(id: Int): ArrayList<Article> {
        val articleJson = requestProductJson(id)
        return mapJsonOnModel(articleJson)
    }

    private fun requestProductJson(id: Int): JSONArray {
        val articleUrl = "${BuildConfig.API_URL}/category/$id/items"
        return get(articleUrl)
                .jsonObject.getJSONArray(productsIdentifier)
    }

    private fun mapJsonOnModel(articleResponse: JSONArray): ArrayList<Article> {
        val articles = arrayListOf<Article>()
        for (i in 0 until articleResponse.length()) {
            articles.add(gson.fromJson(articleResponse.getString(i), Article::class.java))
        }
        return articles
    }
}
