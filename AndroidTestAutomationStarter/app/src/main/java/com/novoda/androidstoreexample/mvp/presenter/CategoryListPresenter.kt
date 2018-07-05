package com.novoda.androidstoreexample.mvp.presenter

interface CategoryListPresenter : BasePresenter {

    fun loadCategoryList()

    fun onCategoryItemClicked(type: Int)
}