package com.novoda.movies.mvi.search.presentation

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.novoda.movies.mvi.search.R
import kotlinx.android.synthetic.main.search_results_view.view.*

internal class SearchResultsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var resultList: RecyclerView
    private lateinit var noResultsView: View

    private val adapter = SearchResultsAdapter()

    override fun onFinishInflate() {
        super.onFinishInflate()
        LayoutInflater.from(context)
                .inflate(R.layout.search_results_view, this, true)

        resultList = search_results_list
        noResultsView = search_results_no_results

        setupView()
    }

    private fun setupView() {
        resultList.adapter = adapter
        resultList.layoutManager = LinearLayoutManager(context)
    }

    fun showResults(results: ViewSearchResults) {
        adapter.bind(results)
        showAllExcept(noResultsView)
    }

    fun showTextInput() {
        hideAll()
    }

    fun showNoResults(attemptedQuery: String) {
        val text = context.getString(R.string.search_no_results_description, attemptedQuery)
        noResultsView.no_results_description.text = text
        hideAllExcept(noResultsView)
    }
}

inline fun ViewGroup.forEachIndexed(action: (index: Int, view: View) -> Unit) {
    for (index in 0 until childCount) {
        action(index, getChildAt(index))
    }
}

fun ViewGroup.hideAll() {
    children().forEach(View::hide)
}

fun ViewGroup.hideAllExcept(vararg views: View) {
    children().forEach(View::hide)
    views.forEach(View::show)
}

fun ViewGroup.showAllExcept(vararg views: View) {
    children().forEach(View::show)
    views.forEach(View::hide)
}

private fun ViewGroup.children() = object : Iterable<View> {
    override fun iterator() = object : Iterator<View> {
        var index = 0
        override fun hasNext() = index < childCount
        override fun next() = getChildAt(index++)
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
