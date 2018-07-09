package com.novoda.androidstoreexample

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.RecyclerView
import com.novoda.androidstoreexample.adapters.CategoryAdapter
import com.novoda.androidstoreexample.adapters.ProductListAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher

object ViewMatchers {

    @JvmStatic
    fun withCategoryTitle(title: String): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, CategoryAdapter.Holder>(CategoryAdapter.Holder::class.java) {
            override fun matchesSafely(item: CategoryAdapter.Holder): Boolean {
                return item.categoryName?.let { it.text.toString().equals(title, true) } ?: false
            }

            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }
        }
    }

    @JvmStatic
    fun withProductTitle(title: String): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, ProductListAdapter.Holder>(ProductListAdapter.Holder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: ProductListAdapter.Holder?): Boolean {
                return item?.productName?.let { it.text.toString().equals(title, true) } ?: false
            }
        }
    }
}
