package com.novoda.androidstoreexample.dagger.categoryList

import dagger.Module
import dagger.Provides
import com.novoda.androidstoreexample.mvp.interactor.CategoryListInteractor
import com.novoda.androidstoreexample.mvp.interactor.impl.CategoryListInteractorImpl
import com.novoda.androidstoreexample.mvp.presenter.CategoryListPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.CategoryListPresenterImpl
import com.novoda.androidstoreexample.mvp.view.CategoryListView

@Module
class CategoryListModule {
    val categoryListView: CategoryListView

    constructor(categoryListView: CategoryListView) {
        this.categoryListView = categoryListView
    }

    @Provides
    fun providesView(): CategoryListView = categoryListView

    @Provides
    fun providePresenter(categoryListPresenter: CategoryListPresenterImpl): CategoryListPresenter {
        return categoryListPresenter
    }

    @Provides
    fun providesInteractor(categoryListInteractor: CategoryListInteractorImpl): CategoryListInteractor {
        return categoryListInteractor
    }
}