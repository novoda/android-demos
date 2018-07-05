package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.models.Product
import com.novoda.androidstoreexample.mvp.interactor.ProductDetailsInteractor
import com.novoda.androidstoreexample.mvp.listener.ProductDetailsListener
import com.novoda.androidstoreexample.mvp.presenter.ProductDetailPresenter
import com.novoda.androidstoreexample.mvp.view.ProductDetailView
import com.novoda.androidstoreexample.services.BasketService
import com.novoda.androidstoreexample.services.ProductDetailsResponse
import javax.inject.Inject

class ProductDetailPresenterImpl : ProductDetailPresenter {
    private val productDetailView: ProductDetailView
    private val productDetailsInteractor: ProductDetailsInteractor
    private val basketService: BasketService

    private lateinit var product: Product

    @Inject
    constructor(productDetailView: ProductDetailView, productDetailsInteractor: ProductDetailsInteractor, basketService: BasketService) {
        this.productDetailView = productDetailView
        this.productDetailsInteractor = productDetailsInteractor
        this.basketService = basketService
    }

    override fun cancel() {
        productDetailsInteractor.cancel()
    }

    override fun loadProductDetails(productId: Int) {
        productDetailView.showProgress()
        productDetailsInteractor.loadProductDetails(object : ProductDetailsListener {
            override fun onFailure(message: String) {
                productDetailView.hideProgress()
                productDetailView.showMessage(message)
            }

            override fun onSuccess(response: ProductDetailsResponse) {
                productDetailView.populateProduct(response.item)
                product = response.item
                productDetailView.hideProgress()
            }
        }, productId)
    }

    override fun addToBasket() {
        basketService.addToBasket(product)
    }
}