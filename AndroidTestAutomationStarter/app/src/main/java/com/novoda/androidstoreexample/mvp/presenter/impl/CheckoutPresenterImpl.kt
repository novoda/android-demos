package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.mvp.presenter.CheckoutPresenter
import com.novoda.androidstoreexample.mvp.view.CheckoutView
import com.novoda.androidstoreexample.services.BasketService
import javax.inject.Inject

class CheckoutPresenterImpl: CheckoutPresenter {
    private val checkoutView: CheckoutView
    private val basketService: BasketService

    @Inject
    constructor(basketService: BasketService, checkoutView: CheckoutView) {
        this.checkoutView = checkoutView
        this.basketService = basketService
    }
    override fun showTotal() {
        checkoutView.showTotal(basketService.getTotalAmount())
    }

    override fun cancel() {

    }
}