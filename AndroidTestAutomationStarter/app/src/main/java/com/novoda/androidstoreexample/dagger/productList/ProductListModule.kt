package com.novoda.androidstoreexample.dagger.categoryList

import dagger.Module
import dagger.Provides
import com.novoda.androidstoreexample.mvp.interactor.ProductListInteractor
import com.novoda.androidstoreexample.mvp.interactor.impl.ProductListInteractorImpl
import com.novoda.androidstoreexample.mvp.presenter.ProductListPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.ProductListPresenterImpl
import com.novoda.androidstoreexample.mvp.view.ProductListView

@Module
class ProductListModule {
    val productListView: ProductListView

    constructor(productListView: ProductListView) {
        this.productListView = productListView
    }

    @Provides
    fun providesView(): ProductListView = productListView

    @Provides
    fun providePresenter(productListPresenter: ProductListPresenterImpl): ProductListPresenter {
        return productListPresenter
    }

    @Provides
    fun providesInteractor(productListInteractor: ProductListInteractorImpl): ProductListInteractor {
        return productListInteractor
    }
}