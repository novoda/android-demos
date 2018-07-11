package com.novoda.androidstoreexample.services

import com.novoda.androidstoreexample.models.Order
import com.novoda.androidstoreexample.models.Product

class BasketService {
    val basket = mutableMapOf<Product, Int>()

    fun addToBasket(product: Product) {
        if (basket.containsKey(product)) {
            basket[product] = basket[product]!! + 1
        } else {
            basket[product] = 1
        }
    }

    fun getBasket(): List<Order> {
        val orders = mutableListOf<Order>()
        basket.forEach {
            val order = Order(it.key, it.value)
            orders.add(order)
        }
        return orders
    }
}