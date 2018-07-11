package com.novoda.androidstoreexample.mvp.view

import android.view.View
import com.novoda.androidstoreexample.models.Product

interface ProductListView : BaseView {

    fun showProductList(products: List<Product>)

    fun onProductClicked(productId: Int)

    fun onBasketClicked(view: View)
}