package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.mvp.presenter.CheckoutPresenter
import com.novoda.androidstoreexample.mvp.view.CheckoutView
import com.novoda.androidstoreexample.services.BasketService
import javax.inject.Inject


class CheckoutPresenterImpl @Inject constructor(private val checkoutView: CheckoutView, private val basketService: BasketService) : CheckoutPresenter
{

    override fun onBuyClick() {
        basketService.emptyBasket()
        checkoutView.goToConfirmationScreen()
    }

}
