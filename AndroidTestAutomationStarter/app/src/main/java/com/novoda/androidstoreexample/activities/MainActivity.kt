package com.novoda.androidstoreexample.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.novoda.androidstoreexample.R
import com.novoda.androidstoreexample.adapters.CategoryAdapter
import com.novoda.androidstoreexample.dagger.categoryList.CategoryListModule
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.models.Category
import com.novoda.androidstoreexample.mvp.presenter.CategoryListPresenter
import com.novoda.androidstoreexample.mvp.view.CategoryListView
import com.novoda.androidstoreexample.utilities.CATEGORY_ID_EXTRA
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), CategoryListView {

    private lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var presenter: CategoryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.loadCategoryList()
    }

    override fun showCategoryList(categories: List<Category>) {
        categoryListView.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(this, categories) { category ->
            presenter.onCategoryItemClicked(category)
        }
        categoryListView.adapter = categoryAdapter
    }

    override fun showProgress() {
        mainActivityProgressBar.visibility = VISIBLE
    }

    override fun hideProgress() {
        mainActivityProgressBar.visibility = GONE
    }

    override fun getActivityLayout(): Int {
        return R.layout.activity_main
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.injectCategory(CategoryListModule(this)).inject(this)
    }

    override fun onItemClicked(type: Int) {
        val intent = Intent(this, ProductListActivity::class.java).apply { putExtra(CATEGORY_ID_EXTRA, type) }
        startActivity(intent)
    }

    override fun onBasketClicked(view: View) {
        val basketIntent = Intent(this, BasketActivity::class.java)
        startActivity(basketIntent)
    }
}
