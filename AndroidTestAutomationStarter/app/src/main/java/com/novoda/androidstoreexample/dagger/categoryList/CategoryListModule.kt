package com.novoda.androidstoreexample.dagger.categoryList

import com.novoda.androidstoreexample.mvp.interactor.CategoryListInteractor
import com.novoda.androidstoreexample.mvp.interactor.impl.CategoryListInteractorImpl
import com.novoda.androidstoreexample.mvp.presenter.CategoryListPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.CategoryListPresenterImpl
import com.novoda.androidstoreexample.mvp.view.CategoryListView
import dagger.Module
import dagger.Provides

@Module
class CategoryListModule(val categoryListView: CategoryListView) {

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
