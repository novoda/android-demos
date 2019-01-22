package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.models.Product
import com.novoda.androidstoreexample.mvp.interactor.ProductDetailsInteractor
import com.novoda.androidstoreexample.mvp.listener.ProductDetailsListener
import com.novoda.androidstoreexample.mvp.presenter.ProductDetailPresenter
import com.novoda.androidstoreexample.mvp.view.ProductDetailView
import com.novoda.androidstoreexample.services.BasketService
import com.novoda.androidstoreexample.services.ProductDetailsResponse
import javax.inject.Inject

class ProductDetailPresenterImpl @Inject constructor(private val productDetailView: ProductDetailView, private val productDetailsInteractor: ProductDetailsInteractor, private val basketService: BasketService) : ProductDetailPresenter {

    private lateinit var product: Product

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
                productDetailView.populateProduct(response.article)
                product = response.article
                productDetailView.hideProgress()
            }
        }, productId)
    }

    override fun addToBasket() {
        basketService.addToBasket(product)
    }
}
