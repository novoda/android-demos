package de.svenkroell.kotlinapi

import de.svenkroell.kotlinapi.data.CategoryDao
import io.javalin.Javalin


fun main(args: Array<String>) {
    val categoryDao = CategoryDao()
    val app = Javalin.create().apply {
        exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start(7000)
    app.get("/categories") { context ->
        context.json(hashMapOf("categories" to categoryDao.listCategories()))
    }

    app.get("/categories/:category-id") { context ->
        context.json(hashMapOf("category" to categoryDao.findCategoryById(context.pathParam("category-id").toInt())))
    }
}