package com.novoda.movies.mvi.search.presentation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.novoda.movies.mvi.search.Dependencies
import com.novoda.movies.mvi.search.Displayer
import com.novoda.movies.mvi.search.R
import com.novoda.movies.mvi.search.domain.SearchDependencyProvider
import com.novoda.movies.mvi.search.presentation.SearchActivity.State
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*

internal class SearchActivity : AppCompatActivity(),
        Displayer<SearchActivity.Action, State> {

    private lateinit var searchInput: SearchInputView
    private lateinit var resultsView: SearchResultsView

    private lateinit var viewModel: SearchViewModel

    override val actions: Observable<Action>
        get() = searchInput.actions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, SearchViewModelFactory(this))
                .get(SearchViewModel::class.java)
        setContentView(R.layout.activity_search)
        searchInput = search_input
        resultsView = search_results
    }

    override fun onStart() {
        super.onStart()
        viewModel.bind(this)
    }

    override fun render(state: State) {
        searchInput.currentQuery = state.queryString
        resultsView.showResults(state.results)
        error_view.visibility = if (state.error != null) VISIBLE else INVISIBLE
        loading_spinner.visibility = if (state.loading) VISIBLE else INVISIBLE


        Log.v("APP_STATE", "state: $state")
    }

    override fun onStop() {
        viewModel.unbind()
        super.onStop()
    }

    internal data class State(
            var queryString: String,
            var loading: Boolean = false,
            var results: ViewSearchResults,
            var error: Throwable? = null
    )

    internal sealed class Action {
        data class ChangeQuery(val queryString: String) : Action()
        object ExecuteSearch : Action()
        object ClearQuery : Action()
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
