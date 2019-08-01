package com.novoda.movies.mvi.search.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.novoda.movies.mvi.search.ActionProvider
import com.novoda.movies.mvi.search.Dependencies
import com.novoda.movies.mvi.search.R
import com.novoda.movies.mvi.search.ViewRender
import com.novoda.movies.mvi.search.domain.SearchAction
import com.novoda.movies.mvi.search.domain.SearchDependencyProvider
import com.novoda.movies.mvi.search.domain.SearchState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*

internal class SearchActivity : AppCompatActivity(),
        ActionProvider<SearchAction>,
        ViewRender<SearchState> {

    private lateinit var searchInput: SearchInputView
    private lateinit var resultsView: SearchResultsView

    private lateinit var viewModel: SearchViewModel

    override val actions: Observable<SearchAction>
        get() = searchInput.actions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, SearchViewModelFactory(this))
                .get(SearchViewModel::class.java)
        setContentView(R.layout.activity_search)
        searchInput = search_input
        resultsView = search_results

        viewModel.wire()
    }

    override fun onStart() {
        super.onStart()
        viewModel.bind(this, this)
    }

    override fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> {
                searchInput.currentQuery = state.queryString
                resultsView.showResults(state.results)
                loading_spinner.visibility = View.INVISIBLE
            }

            is SearchState.Loading -> loading_spinner.visibility = View.VISIBLE
            is SearchState.Error -> loading_spinner.visibility = View.VISIBLE
            //TODO: Handle Error
        }
        Log.v("APP", "state: $state")
    }

    override fun onStop() {
        viewModel.unbind()
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.unwire()
        super.onDestroy()
    }
}

internal class SearchViewModelFactory(
        private val searchActivity: SearchActivity) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val dependencies = searchActivity.application as Dependencies
        val networkDependencyProvider = dependencies.networkDependencyProvider
        val searchDependencyProvider = SearchDependencyProvider(
                networkDependencyProvider,
                dependencies.endpoints
        )

        return SearchViewModel(searchDependencyProvider.provideSearchStore())
                .let { modelClass.cast(it) }!!
    }
}
