package de.svenkroell.kotlinapi.database

import de.svenkroell.kotlinapi.model.Category

class InMemoryDatabase {
    fun categories(): ArrayList<Category> {
        return arrayListOf(
                Category(id = 0, title = "SHIRTS", image = "shirtimage"),
                Category(id = 1, title = "HOODIES", image = "hoodieimage"),
                Category(id = 2, title = "HATS", image = "hatimage"),
                Category(id = 3, title = "DIGITAL GOODS", image = "digitalgoodsimage")
        )
    }
}
