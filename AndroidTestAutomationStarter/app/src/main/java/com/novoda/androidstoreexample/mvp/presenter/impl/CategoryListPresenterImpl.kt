package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.models.Category
import com.novoda.androidstoreexample.services.CategoryResponse
import com.novoda.androidstoreexample.mvp.interactor.CategoryListInteractor
import com.novoda.androidstoreexample.mvp.listener.CategoryListListener
import com.novoda.androidstoreexample.mvp.presenter.CategoryListPresenter
import com.novoda.androidstoreexample.mvp.view.CategoryListView
import javax.inject.Inject

class CategoryListPresenterImpl : CategoryListPresenter {
    private val categoryListView: CategoryListView
    private val categoryListInteractor: CategoryListInteractor

    @Inject
    constructor(categoryListView: CategoryListView, categoryListInteractor: CategoryListInteractor) {
        this.categoryListView = categoryListView
        this.categoryListInteractor = categoryListInteractor
    }

    override fun cancel() {
        categoryListInteractor.cancel()
    }

    override fun onCategoryItemClicked(type: Int) {
        categoryListView.onItemClicked(type)
    }

    override fun loadCategoryList() {
        categoryListView.showProgress()
        categoryListInteractor.loadCategoryList(object : CategoryListListener {
            override fun onFailure(message: String) {
                categoryListView.hideProgress()
                categoryListView.showMessage(message)
            }

            override fun onSuccess(categoryResponse: CategoryResponse) {
                val categoryList: List<Category> = categoryResponse.categories.toList()
                categoryListView.showCategoryList(categoryList)
                categoryListView.hideProgress()
            }
        })
    }
}