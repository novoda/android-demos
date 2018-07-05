package com.novoda.androidstoreexample.mvp.presenter

interface ProductListPresenter : BasePresenter {

    fun loadProductList(category: Int)

    fun onProductClicked(productId: Int)
}