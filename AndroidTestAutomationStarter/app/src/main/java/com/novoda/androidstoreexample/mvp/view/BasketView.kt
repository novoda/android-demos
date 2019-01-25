package com.novoda.androidstoreexample.mvp.view

import com.novoda.androidstoreexample.models.Order
import com.novoda.androidstoreexample.models.Product

interface BasketView : BaseView {
    fun showBasketItems(orders: List<Order>)
    fun onProductClicked(product: Product)
    fun showTotalAmountOfBasket(orders: Int)
}