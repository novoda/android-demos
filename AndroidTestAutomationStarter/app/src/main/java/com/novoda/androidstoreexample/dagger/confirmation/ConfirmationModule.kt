package com.novoda.androidstoreexample.dagger.confirmation

import com.novoda.androidstoreexample.mvp.presenter.ConfirmationPresenter
import com.novoda.androidstoreexample.mvp.presenter.impl.ConfirmationPresenterImpl
import com.novoda.androidstoreexample.mvp.view.ConfirmationView
import dagger.Module
import dagger.Provides

@Module
class ConfirmationModule(private val confirmationView: ConfirmationView) {

    @Provides
    fun providesView(): ConfirmationView = confirmationView

    @Provides
    fun providePresenter(confirmationPresenter: ConfirmationPresenterImpl): ConfirmationPresenter {
        return confirmationPresenter
    }
}
