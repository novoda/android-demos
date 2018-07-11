package com.novoda.androidstoreexample.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.adapters.ProductListAdapter
import com.novoda.androidstoreexample.dagger.categoryList.ProductListModule
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.models.Product
import com.novoda.androidstoreexample.mvp.presenter.ProductListPresenter
import com.novoda.androidstoreexample.mvp.view.ProductListView
import com.novoda.androidstoreexample.utilities.CATEGORY_ID_EXTRA
import com.novoda.androidstoreexample.utilities.PRODUCT_ID_EXTRA
import kotlinx.android.synthetic.main.activity_product_list.*
import javax.inject.Inject

class ProductListActivity : BaseActivity(), ProductListView {

    @Inject
    lateinit var presenter: ProductListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getIntExtra(CATEGORY_ID_EXTRA, -1)
        presenter.loadProductList(category)
    }

    override fun showProductList(products: List<Product>) {
        productListView.layoutManager = GridLayoutManager(this, 2)
        val productListAdapter = ProductListAdapter(this, products) { product ->
            presenter.onProductClicked(product)
        }
        productListView.adapter = productListAdapter
    }

    override fun getActivityLayout(): Int {
        return R.layout.activity_product_list
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectProducts(ProductListModule(this)).inject(this)
    }

    override fun onProductClicked(productId: Int) {
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra(PRODUCT_ID_EXTRA, productId)
        startActivity(intent)
    }

    override fun showProgress() {
        productListProgressView.visibility = VISIBLE
    }

    override fun hideProgress() {
        productListProgressView.visibility = GONE
    }

    override fun onBasketClicked(view: View) {
        val basketIntent = Intent(this, BasketActivity::class.java)
        startActivity(basketIntent)
    }
}
