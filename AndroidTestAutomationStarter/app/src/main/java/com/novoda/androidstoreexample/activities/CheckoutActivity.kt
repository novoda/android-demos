package com.novoda.androidstoreexample.activities

import android.os.Bundle
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.dagger.checkout.CheckoutModule
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.mvp.presenter.CheckoutPresenter
import com.novoda.androidstoreexample.mvp.view.CheckoutView
import kotlinx.android.synthetic.main.activity_checkout.*
import javax.inject.Inject

class CheckoutActivity : BaseActivity(), CheckoutView {


    @Inject
    lateinit var presenter: CheckoutPresenter

    override fun showTotal(total: Int) {
        total_price_of_items.text = total.toString()
    }

    override fun showProgress() {}

    override fun hideProgress() {}

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectCheckout(CheckoutModule(this)).inject(this)
    }

    override fun getActivityLayout(): Int {
        return R.layout.activity_checkout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        presenter.showTotal()
    }
}
