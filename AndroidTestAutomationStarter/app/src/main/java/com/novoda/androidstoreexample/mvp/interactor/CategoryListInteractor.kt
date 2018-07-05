package com.novoda.androidstoreexample.mvp.interactor

import com.novoda.androidstoreexample.mvp.listener.CategoryListListener

interface CategoryListInteractor : BaseInteractor {
    fun loadCategoryList(categoryListListener: CategoryListListener)
}