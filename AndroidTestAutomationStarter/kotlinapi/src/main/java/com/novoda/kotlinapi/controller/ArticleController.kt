package com.novoda.kotlinapi.controller

import com.novoda.kotlinapi.data.ArticleDao
import io.javalin.Context

object ArticleController {
    private val articleDao = ArticleDao()

    fun getArticlesByCategory(context: Context) {
        context.json(hashMapOf("articles" to articleDao.listArticlesPerCategory(context.pathParam("category-id").toInt())))
    }

    fun getArticleDetails(context: Context) {
        context.json(hashMapOf("article" to articleDao.findArticleById(context.pathParam("article-id").toInt())))
    }
}
