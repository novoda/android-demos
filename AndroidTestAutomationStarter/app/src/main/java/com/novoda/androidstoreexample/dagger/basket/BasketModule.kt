package com.novoda.androidstoreexample.dagger.basket

import com.novoda.androidstoreexample.mvp.presenter.BasketPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.BasketPresenterImpl
import com.novoda.androidstoreexample.mvp.view.BasketView
import dagger.Module
import dagger.Provides

@Module
class BasketModule(private val basketView: BasketView) {

    @Provides
    fun providesView(): BasketView = basketView

    @Provides
    fun providePresenter(basketPresenter: BasketPresenterImpl): BasketPresenter {
        return basketPresenter
    }
}