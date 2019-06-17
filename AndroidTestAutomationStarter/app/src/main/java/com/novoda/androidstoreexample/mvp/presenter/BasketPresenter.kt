package com.novoda.androidstoreexample.mvp.presenter

import com.novoda.androidstoreexample.models.Product

interface BasketPresenter {

    fun loadBasket()

    fun onBasketItemClicked(product: Product)

    fun onIncreaseItemClicked(product: Product)

    fun onDecreaseAmountClicked(product: Product)

    fun onCheckoutClicked()
}
