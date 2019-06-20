package com.novoda.movies.mvi.search.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.novoda.movies.mvi.search.Dependencies
import com.novoda.movies.mvi.search.R
import com.novoda.movies.mvi.search.domain.SearchDependencyProvider
import kotlinx.android.synthetic.main.activity_search.*

internal class SearchActivity : AppCompatActivity() {

    private lateinit var searchInput: SearchInputView
    private lateinit var resultsView: SearchResultsView

    lateinit var presenter: SearchResultsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector().inject(this)
        setContentView(R.layout.activity_search)
        searchInput = search_input
        resultsView = search_results
    }

    override fun onStart() {
        super.onStart()
        presenter.bind(searchInput, resultsView)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbind()
    }

    class Injector {
        fun inject(searchActivity: SearchActivity) {
            val dependencies = searchActivity.application as Dependencies
            val networkDependencyProvider = dependencies.networkDependencyProvider
            val searchDependencyProvider =
                SearchDependencyProvider(
                    networkDependencyProvider,
                    dependencies.endpoints
                )
            searchActivity.presenter = searchDependencyProvider.provideSearchResultsPresenter()
        }
    }
}
