package com.novoda.kotlinapi.controller

import com.novoda.kotlinapi.data.CategoryDao
import io.javalin.Context

object CategoryController {
    private val categoryDao = CategoryDao()

    fun getAllCateories(context: Context) {
        context.json(hashMapOf("categories" to categoryDao.listCategories()))
    }

    fun getCategoryById(context: Context) {
        context.json(hashMapOf("category" to categoryDao.findCategoryById(context.pathParam("category-id").toInt())))
    }
}
