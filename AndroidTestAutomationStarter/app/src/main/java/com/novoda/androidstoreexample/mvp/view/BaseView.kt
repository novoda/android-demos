package com.novoda.androidstoreexample.mvp.view

interface BaseView {
    fun showProgress()

    fun hideProgress()

    fun showMessage(message: String)
}