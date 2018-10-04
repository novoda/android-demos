package com.novoda.androidstoreexample.data.dto

import com.google.gson.Gson
import com.novoda.androidstoreexample.data.models.Article
import com.novoda.testautomationstarter.test.BuildConfig
import khttp.get

class ArticleDTO {
    private val gson = Gson()

    fun getArticlesForCategory(id: Int): ArrayList<Article> {
        val articles = arrayListOf<Article>()
        val articleResponse = get("${BuildConfig.API_URL}/category/$id/items")
                .jsonObject.getJSONArray("products")
        for (i in 0 until articleResponse.length()) {
            articles.add(gson.fromJson(articleResponse.getString(i), Article::class.java))
        }
        return articles
    }
}