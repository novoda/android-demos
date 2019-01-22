package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.mvp.interactor.ProductListInteractor
import com.novoda.androidstoreexample.mvp.listener.ProductListListener
import com.novoda.androidstoreexample.mvp.presenter.ProductListPresenter
import com.novoda.androidstoreexample.mvp.view.ProductListView
import com.novoda.androidstoreexample.services.ProductResponse
import javax.inject.Inject

class ProductListPresenterImpl : ProductListPresenter {
    private val productListInteractor: ProductListInteractor
    private val productListView: ProductListView

    @Inject
    constructor(productListInteractor: ProductListInteractor, productListView: ProductListView) {
        this.productListInteractor = productListInteractor
        this.productListView = productListView
    }

    override fun cancel() {
        productListInteractor.cancel()
    }

    override fun loadProductList(category: Int) {
        productListView.showProgress()
        productListInteractor.loadProductList(object : ProductListListener {
            override fun onFailure(message: String) {
                productListView.hideProgress()
                productListView.showMessage(message)
            }

            override fun onSuccess(productResponse: ProductResponse) {
                val productList = productResponse.articles.toList()
                productListView.showProductList(productList)
                productListView.hideProgress()
            }
        }, category)
    }

    override fun onProductClicked(productId: Int) {
        productListView.onProductClicked(productId)
    }
}