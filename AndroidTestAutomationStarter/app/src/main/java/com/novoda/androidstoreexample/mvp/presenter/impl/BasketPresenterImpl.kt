package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.models.Product
import com.novoda.androidstoreexample.mvp.presenter.BasketPresenter
import com.novoda.androidstoreexample.mvp.view.BasketView
import com.novoda.androidstoreexample.services.BasketService
import javax.inject.Inject

class BasketPresenterImpl : BasketPresenter {
    val basketView: BasketView
    val basketService: BasketService

    @Inject
    constructor(basketView: BasketView, basketService: BasketService) {
        this.basketView = basketView
        this.basketService = basketService
    }

    override fun loadBasket() {
        basketView.showProgress()
        basketView.showBasketItems(basketService.getBasket())
        basketView.showTotalAmountOfBasket(basketService.getTotalAmount())
        basketView.hideProgress()
    }

    override fun onBasketItemClicked(product: Product) {
        basketView.onProductClicked(product)
    }

    override fun onIncreaseItemClicked(product: Product) {
        basketService.increaseNumberOf(product)
        loadBasket()
    }

    override fun onDecreaseAmountClicked(product: Product) {
        basketService.decreaseNumberOf(product)
        loadBasket()
    }
}