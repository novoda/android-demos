package com.novoda.androidstoreexample.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.dagger.confirmation.ConfirmationModule
import com.novoda.androidstoreexample.mvp.presenter.ConfirmationPresenter
import com.novoda.androidstoreexample.mvp.view.ConfirmationView
import kotlinx.android.synthetic.main.activity_purchase_confirmation.*
import javax.inject.Inject

class ConfirmationActivity : BaseActivity(), ConfirmationView {
    override fun getActivityLayout(): Int {
        return R.layout.activity_purchase_confirmation
    }

    @Inject
    lateinit var presenter: ConfirmationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_confirmation)
        home_button.setOnClickListener {
            presenter.onHomeClicked()
        }
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectConfirmation(ConfirmationModule(this)).inject(this)
    }


    override fun onHomeClicked(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

}
