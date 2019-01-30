package com.novoda.androidstoreexample.mvp.presenter.impl

import com.novoda.androidstoreexample.mvp.presenter.ConfirmationPresenter
import com.novoda.androidstoreexample.mvp.view.ConfirmationView
import javax.inject.Inject

class ConfirmationPresenterImpl @Inject constructor(private val confirmationView: ConfirmationView) : ConfirmationPresenter
{

    override fun onHomeClicked() {
        confirmationView.goToHomeScreen()
    }

}
