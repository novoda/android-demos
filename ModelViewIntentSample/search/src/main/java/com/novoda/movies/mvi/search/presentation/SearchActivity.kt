package com.novoda.movies.mvi.search.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.Dependencies
import com.novoda.movies.mvi.search.MVIView
import com.novoda.movies.mvi.search.R
import com.novoda.movies.mvi.search.domain.SearchAction
import com.novoda.movies.mvi.search.domain.SearchChanges
import com.novoda.movies.mvi.search.domain.SearchDependencyProvider
import com.novoda.movies.mvi.search.domain.SearchMiddleware
import com.novoda.movies.mvi.search.domain.SearchReducer
import com.novoda.movies.mvi.search.domain.SearchState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*

internal class SearchActivity : AppCompatActivity(), MVIView<SearchAction, SearchState> {

    private lateinit var searchInput: SearchInputView
    private lateinit var resultsView: SearchResultsView

    private val searchStore = BaseStore(
            SearchReducer(),
            listOf(SearchMiddleware()),
            SearchState.initialState()
    )

    override val actions: Observable<SearchAction>
        get() = searchInput.actions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector().inject(this)
        setContentView(R.layout.activity_search)
        searchInput = search_input
        resultsView = search_results
    }

    override fun onStart() {
        super.onStart()
        searchStore.wire() //TODO: Move to a place where it survives activity lifecycle
        searchStore.bind(this)
    }

    override fun render(state: SearchState) {
        Log.v("APP", "state: $state")
    }

    override fun onStop() {
        searchStore.unbind()
        super.onStop()
    }

    class Injector {
        fun inject(searchActivity: SearchActivity) {
            val dependencies = searchActivity.application as Dependencies
            val networkDependencyProvider = dependencies.networkDependencyProvider
            SearchDependencyProvider(
                networkDependencyProvider,
                dependencies.endpoints
            )
        }
    }
}
