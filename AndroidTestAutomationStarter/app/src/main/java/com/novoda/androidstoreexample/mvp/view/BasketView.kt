package com.novoda.androidstoreexample.mvp.view

import com.novoda.androidstoreexample.models.Order

interface BasketView : BaseView {
    fun showBasketItems(orders: List<Order>)
}