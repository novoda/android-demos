package com.novoda.androidstoreexample.activities

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.dagger.checkout.CheckoutModule
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.mvp.presenter.CheckoutPresenter
import com.novoda.androidstoreexample.mvp.view.CheckoutView
import com.novoda.androidstoreexample.utilities.PRODUCT_ID_EXTRA
import kotlinx.android.synthetic.main.activity_checkout.*
import javax.inject.Inject
    
class CheckoutActivity : BaseActivity(), CheckoutView {
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

    override fun displaySuccessToast() {
        val text = "Purchase successful. Your stuff is on the way!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.setGravity(Gravity.TOP , 0, 0)


        toast.show()
    }

    override fun goToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectCheckout(CheckoutModule(this)).inject(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}
l