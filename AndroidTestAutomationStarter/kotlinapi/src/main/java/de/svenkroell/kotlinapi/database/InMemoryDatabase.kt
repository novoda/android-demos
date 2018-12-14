package de.svenkroell.kotlinapi.database

import de.svenkroell.kotlinapi.model.Article
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

    fun articles(): HashMap<Int, ArrayList<Article>> {
        return hashMapOf(2 to arrayListOf(
                Article(id=0, title = "Hat Green", price = "$18", image = "hat1", productDescription = descriptionOne),
                Article(id=1, title = "Hat Back", price = "$12", image = "hat2", productDescription = descriptionTwo),
                Article(id=2, title = "Hat White", price = "$11", image = "hat3", productDescription = descriptionThree),
                Article(id=3, title = "Hat Blue", price = "$29", image = "hat4", productDescription = descriptionFour)))
    }

    private val descriptionOne = "Bacon ipsum dolor amet in meatball shank prosciutto, anim pork loin nisi bresaola. Buffalo beef salami shank pancetta tail sunt prosciutto, excepteur drumstick ground round anim nulla sint jerky."
    private val descriptionTwo = "Velit cupidatat deserunt pork chop cillum, sirloin elit chicken brisket officia excepteur chuck."
    private val descriptionThree = "Jerky laboris veniam, laborum pancetta labore sint buffalo dolor quis strip steak sirloin ground round."
    private val descriptionFour = "Chuck consectetur chicken drumstick short ribs in tenderloin frankfurter eiusmod lorem. Dolore magna excepteur venison. Tempor enim rump ut fugiat minim esse."

}
