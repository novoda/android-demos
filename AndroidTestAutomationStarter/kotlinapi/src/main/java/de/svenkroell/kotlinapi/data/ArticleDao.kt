package de.svenkroell.kotlinapi.data

import de.svenkroell.kotlinapi.database.InMemoryDatabase
import de.svenkroell.kotlinapi.model.Article
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