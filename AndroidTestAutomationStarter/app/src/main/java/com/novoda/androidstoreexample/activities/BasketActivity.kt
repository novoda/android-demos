package com.novoda.androidstoreexample.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.adapters.BasketAdapter
import com.novoda.androidstoreexample.dagger.basket.BasketModule
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.models.Order
import com.novoda.androidstoreexample.mvp.presenter.BasketPresenter
import com.novoda.androidstoreexample.mvp.view.BasketView
import kotlinx.android.synthetic.main.activity_basket.*
import javax.inject.Inject

class BasketActivity : BaseActivity(), BasketView {

    @Inject
    lateinit var presenter: BasketPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)
        presenter.loadBasket()
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showBasketItems(orders: List<Order>) {
        val basketAdapter = BasketAdapter(this, orders) {
        }
        basketList.layoutManager = LinearLayoutManager(this)
        basketList.adapter = basketAdapter
    }

    override fun getActivityLayout(): Int {
        return R.layout.activity_basket
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectBasket(BasketModule(this)).inject(this)
    }
}
