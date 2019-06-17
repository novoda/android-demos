package com.novoda.androidstoreexample.activities

import android.os.Bundle
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.dagger.checkout.CheckoutModule
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.mvp.presenter.CheckoutPresenter
import kotlinx.android.synthetic.main.activity_checkout.*
import javax.inject.Inject
    
class CheckoutActivity : BaseActivity() {
    @Inject
    lateinit var presenter: CheckoutPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        buy_button.setOnClickListener {
            presenter.onBuyClick()
        }
    }

    override fun getActivityLayout(): Int {
        return R.layout.activity_checkout
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectCheckout(CheckoutModule(this)).inject(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}
