package com.novoda.androidstoreexample.data.repositories

import com.novoda.androidstoreexample.data.dto.ArticleDTO
import com.novoda.androidstoreexample.data.dto.CategoryDTO
import com.novoda.androidstoreexample.data.models.Article
import com.novoda.androidstoreexample.data.models.Item

class ArticleRepository {
    private val categories = CategoryDTO().getAllCategories()
    private val articleDto = ArticleDTO()
    private val hatsCategoryName = "HATS"

    val standardArticle = Item(title = "Hat white", category = "HATS")

    fun getHat(id: Int = 0): Item {
        val categoryId = getCategoryIdForCategory(hatsCategoryName)
        val product = articleDto.getArticlesForCategory(categoryId)[id]
        return buildItem(product, hatsCategoryName)
    }

    fun getRandomHat(): Item {
        val categoryId = getCategoryIdForCategory(hatsCategoryName)
        val articles = articleDto.getArticlesForCategory(categoryId)
        val product = articles.shuffled().take(articles.size)[0]
        return buildItem(product, hatsCategoryName)
    }

    private fun buildItem(article: Article, categoryName: String): Item {
        return Item(title = article.title, price = article.price, category = categoryName, description = article.productDescription)
    }

    private fun getCategoryIdForCategory(categoryName: String): Int {
        return categories.filter { it.title == categoryName }[0].id
    }
}
