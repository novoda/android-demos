package com.novoda.kotlinapi

import com.novoda.kotlinapi.data.ArticleDao
import com.novoda.kotlinapi.data.CategoryDao
import io.javalin.Javalin


fun main(args: Array<String>) {
    val categoryDao = CategoryDao()
    val articleDao = ArticleDao()
    val app = Javalin.create().apply {
        exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start(4567)

    app.get("/categories") { context ->
        context.json(hashMapOf("categories" to categoryDao.listCategories()))
    }

    app.get("/categories/:category-id") { context ->
        context.json(hashMapOf("category" to categoryDao.findCategoryById(context.pathParam("category-id").toInt())))
    }

    app.get("/categories/:category-id/articles") {
        it.json(hashMapOf("articles" to articleDao.listArticlesPerCategory(it.pathParam("category-id").toInt())))
    }

    app.get("/articles/:article-id") {
        it.json(hashMapOf("article" to articleDao.findArticleById(it.pathParam("article-id").toInt())))
    }

}