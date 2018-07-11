package com.novoda.androidstoreexample.dagger.productDetails

import com.novoda.androidstoreexample.mvp.interactor.ProductDetailsInteractor
import com.novoda.androidstoreexample.mvp.interactor.impl.ProductDetailsInteractorImpl
import com.novoda.androidstoreexample.mvp.presenter.ProductDetailPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.ProductDetailPresenterImpl
import com.novoda.androidstoreexample.mvp.view.ProductDetailView
import dagger.Module
import dagger.Provides

@Module
class ProductDetailsModule(val productDetailView: ProductDetailView) {

    @Provides
    fun providesView(): ProductDetailView = productDetailView

    @Provides
    fun providePresenter(productDetailsPresenter: ProductDetailPresenterImpl): ProductDetailPresenter {
        return productDetailsPresenter
    }

    @Provides
    fun providesInteractor(productDetailsInteractor: ProductDetailsInteractorImpl): ProductDetailsInteractor {
        return productDetailsInteractor
    }
}
