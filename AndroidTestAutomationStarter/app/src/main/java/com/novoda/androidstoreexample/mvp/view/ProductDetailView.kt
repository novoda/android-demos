package com.novoda.androidstoreexample.mvp.view

import android.view.View
import com.novoda.androidstoreexample.models.Product

interface ProductDetailView : BaseView {
    fun populateProduct(product: Product)

    fun onBasketClicked(view: View)
}