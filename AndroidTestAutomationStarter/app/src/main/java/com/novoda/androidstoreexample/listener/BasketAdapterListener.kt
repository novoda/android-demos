package com.novoda.androidstoreexample.listener

import com.novoda.androidstoreexample.models.Product

interface BasketAdapterListener {
    fun onProductImageClicked(product: Product)

    fun onIncreaseAmountClicked(product: Product)

    fun onDecreaseAmountClicked(product: Product)
}
