package com.novoda.kotlinapi.data

import com.novoda.kotlinapi.database.InMemoryDatabase
import com.novoda.kotlinapi.model.Category

class CategoryDao {
    private val database = InMemoryDatabase()

    fun listCategories(): ArrayList<Category> {
        return database.categories()
    }

    fun findCategoryById(id: Int): Category? {
        return database.categories().find { it.id == id }
    }
}
