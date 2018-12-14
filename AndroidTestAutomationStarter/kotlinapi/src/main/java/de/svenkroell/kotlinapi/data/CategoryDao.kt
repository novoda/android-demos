package de.svenkroell.kotlinapi.data

import de.svenkroell.kotlinapi.database.InMemoryDatabase
import de.svenkroell.kotlinapi.model.Category

class CategoryDao {
    private val database = InMemoryDatabase()

    fun listCategories(): ArrayList<Category> {
        return database.categories()
    }

    fun findCategoryById(id: Int): Category? {
        return database.categories().find { it.id == id }
    }
}