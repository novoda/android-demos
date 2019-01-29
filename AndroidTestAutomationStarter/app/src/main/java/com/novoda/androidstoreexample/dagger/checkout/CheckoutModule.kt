package com.novoda.androidstoreexample.dagger.checkout

import com.novoda.androidstoreexample.mvp.presenter.BasketPresenter
import com.novoda.androidstoreexample.mvp.presenter.CheckoutPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.BasketPresenterImpl
import com.novoda.androidstoreexample.mvp.presenter.impl.CheckoutPresenterImpl
import com.novoda.androidstoreexample.mvp.view.BasketView
import com.novoda.androidstoreexample.mvp.view.CheckoutView
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule(private val checkoutView: CheckoutView) {

    @Provides
    fun providesView(): CheckoutView = checkoutView

    @Provides
    fun providePresenter(checkoutPresenter: CheckoutPresenterImpl): CheckoutPresenter {
        return checkoutPresenter
    }
}
