package com.novoda.androidstoreexample.mvp.view

import android.view.View
import com.novoda.androidstoreexample.models.Order
import com.novoda.androidstoreexample.models.Product

interface BasketView : BaseView {
    fun showBasketItems(orders: List<Order>)
    fun onProductClicked(product: Product)
    fun onCheckoutClicked(view: View)
    fun showTotalAmountOfBasket(orders: Int)
}
