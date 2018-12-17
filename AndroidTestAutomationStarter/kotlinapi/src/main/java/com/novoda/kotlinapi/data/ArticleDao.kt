package com.novoda.kotlinapi.data

import com.novoda.kotlinapi.database.InMemoryDatabase
import com.novoda.kotlinapi.model.Article
import java.util.ArrayList

class ArticleDao {
    private val inMemoryDatabase = InMemoryDatabase()

    fun listArticlesPerCategory(id: Int): ArrayList<Article>? {
        return inMemoryDatabase.articles()[id]
    }

    fun findArticleById(id: Int): Article? {
        return inMemoryDatabase.articles().values.flatten().find { it.id == id }
    }
}
