package com.novoda.androidstoreexample.mvp.presenter.impl

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
        basketView.hideProgress()
    }
}