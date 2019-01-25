package com.novoda.androidstoreexample.models

data class Order(val product: Product, val numberOfItems: Int) {
    val amount: Int = product.price * numberOfItems
}