package com.novoda.kotlinapi

import com.novoda.kotlinapi.controller.ArticleController
import com.novoda.kotlinapi.controller.CategoryController
import com.novoda.kotlinapi.controller.HealthController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import java.lang.RuntimeException

fun main(args: Array<String>) {
    val app = Javalin.create().apply {
        exception(RuntimeException::class.java) { e, _ -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("not found") }
    }.start(4567)

    app.routes {
        path("/") {
            get(HealthController::apiRunning)
        }
        path("categories") {
            get(CategoryController::getAllCateories)
            path(":category-id") {
                get(CategoryController::getCategoryById)
                path("articles") {
                    get(ArticleController::getArticlesByCategory)
                }
            }
        }
        path("articles") {
            path(":article-id") {
                get(ArticleController::getArticleDetails)
            }
        }
    }
}
