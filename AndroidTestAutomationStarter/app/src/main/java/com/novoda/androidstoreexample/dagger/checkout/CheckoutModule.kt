package com.novoda.androidstoreexample.dagger.checkout

import com.novoda.androidstoreexample.mvp.presenter.CheckoutPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.CheckoutPresenterImpl
import com.novoda.androidstoreexample.mvp.view.CheckoutView
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule(val checkoutView: CheckoutView) {
    @Provides
    fun providesView(): CheckoutView = checkoutView

    @Provides
    fun providePresenter(checkoutPresenter: CheckoutPresenterImpl): CheckoutPresenter {
        return checkoutPresenter
    }

}