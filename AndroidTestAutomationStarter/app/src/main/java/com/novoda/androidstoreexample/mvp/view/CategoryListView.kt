package com.novoda.androidstoreexample.mvp.view

import android.view.View
import com.novoda.androidstoreexample.models.Category

interface CategoryListView : BaseView {

    fun showCategoryList(categories: List<Category>)

    fun onItemClicked(type: Int)

    fun onBasketClicked(view: View)
}