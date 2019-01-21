package com.novoda.androidstoreexample

import com.novoda.androidstoreexample.dagger.module.BasketServiceModule
import com.novoda.androidstoreexample.models.Product
import com.novoda.androidstoreexample.services.BasketService

class EspressoBasketModule : BasketServiceModule() {
    override fun providesBasket(): BasketService {
        val basketService = BasketService()
        basketService.addToBasket(Product(id = 0, title = "Hat Green", price = "18", image = "hat1", productDescription = "Test"))
        basketService.addToBasket(Product(id = 0, title = "Hat Green", price = "18", image = "hat1", productDescription = "Test"))
        basketService.addToBasket(Product(id = 0, title = "Hat Green", price = "18", image = "hat1", productDescription = "Test"))
        return basketService
    }
}