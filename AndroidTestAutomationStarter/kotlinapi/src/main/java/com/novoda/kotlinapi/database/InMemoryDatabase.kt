package com.novoda.kotlinapi.database

import com.novoda.kotlinapi.model.Article
import com.novoda.kotlinapi.model.Category

class InMemoryDatabase {
    private val descriptionOne = "Bacon ipsum dolor amet in meatball shank prosciutto, anim pork loin nisi bresaola. Buffalo beef salami shank pancetta tail sunt prosciutto, excepteur drumstick ground round anim nulla sint jerky."
    private val descriptionTwo = "Velit cupidatat deserunt pork chop cillum, sirloin elit chicken brisket officia excepteur chuck."
    private val descriptionThree = "Jerky laboris veniam, laborum pancetta labore sint buffalo dolor quis strip steak sirloin ground round."
    private val descriptionFour = "Chuck consectetur chicken drumstick short ribs in tenderloin frankfurter eiusmod lorem. Dolore magna excepteur venison. Tempor enim rump ut fugiat minim esse."
    private val articles = hashMapOf(
            0 to arrayListOf(
                    Article(id = 4, title = "Hat Green", price = "$18", image = "shirt1", productDescription = descriptionOne),
                    Article(id = 5, title = "Hat Green", price = "$18", image = "shirt2", productDescription = descriptionOne),
                    Article(id = 6, title = "Hat Green", price = "$18", image = "shirt3", productDescription = descriptionOne),
                    Article(id = 7, title = "Hat Green", price = "$18", image = "shirt4", productDescription = descriptionOne)
            ),
            1 to arrayListOf(
                    Article(id = 8, title = "Hat Green", price = "$18", image = "hoodie1", productDescription = descriptionOne),
                    Article(id = 9, title = "Hat Green", price = "$18", image = "hoodie2", productDescription = descriptionOne),
                    Article(id = 10, title = "Hat Green", price = "$18", image = "hoodie3", productDescription = descriptionOne),
                    Article(id = 11, title = "Hat Green", price = "$18", image = "hoodie4", productDescription = descriptionOne)
            ),
            2 to arrayListOf(
                    Article(id = 0, title = "Hat Green", price = "$18", image = "hat1", productDescription = descriptionOne),
                    Article(id = 1, title = "Hat Back", price = "$12", image = "hat2", productDescription = descriptionTwo),
                    Article(id = 2, title = "Hat White", price = "$11", image = "hat3", productDescription = descriptionThree),
                    Article(id = 3, title = "Hat Blue", price = "$29", image = "hat4", productDescription = descriptionFour)),
            3 to arrayListOf(
                    Article(id = 12, title = "Hat Green", price = "$18", image = "hat1", productDescription = descriptionOne)
            ))

    private val categories = arrayListOf(
            Category(id = 0, title = "SHIRTS", image = "shirtimage"),
            Category(id = 1, title = "HOODIES", image = "hoodieimage"),
            Category(id = 2, title = "HATS", image = "hatimage"),
            Category(id = 3, title = "DIGITAL GOODS", image = "digitalgoodsimage")
    )

    init {
        checkDatabaseIntegrity()
    }

    fun categories(): ArrayList<Category> {
        return categories
    }

    fun articles(): HashMap<Int, ArrayList<Article>> {
        return articles
    }

    private fun checkDatabaseIntegrity() {
        if (categories().distinctBy { it.id }.size != categories().size) {
            var errorMessage = "Error: "
            findDuplicateCategoryId().forEach { id, number ->
                errorMessage += "Category $id has been found $number times\n"
            }
            throw RuntimeException(errorMessage)
        }
        if (articles.flatMap { it.value }.size != articles.flatMap { it.value }.distinctBy { it.id }.size) {
            var errorMessage = "Error: "
            findDuplicateArticleId().forEach { id, number ->
                errorMessage += "Article $id has been found $number times\n"
            }
            throw RuntimeException(errorMessage)
        }
    }

    private fun findDuplicateCategoryId(): Map<Int, Int> {
        return categories.groupingBy { it.id }.eachCount().filter { it.value > 1 }
    }

    private fun findDuplicateArticleId(): Map<Int, Int> {
        return articles.flatMap { it.value }.groupingBy { it.id }.eachCount().filter { it.value > 1 }
    }
}
