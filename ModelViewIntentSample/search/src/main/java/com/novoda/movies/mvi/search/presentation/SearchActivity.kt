package com.novoda.movies.mvi.search.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.Dependencies
import com.novoda.movies.mvi.search.MVIView
import com.novoda.movies.mvi.search.R
import com.novoda.movies.mvi.search.domain.ScreenState
import com.novoda.movies.mvi.search.domain.ScreenStateChanges
import com.novoda.movies.mvi.search.domain.SearchAction
import com.novoda.movies.mvi.search.domain.SearchDependencyProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*

internal class SearchActivity : AppCompatActivity(), MVIView<SearchAction, ScreenState> {

    private lateinit var searchInput: SearchInputView
    private lateinit var resultsView: SearchResultsView

    lateinit var screenStore: BaseStore<SearchAction, ScreenState, ScreenStateChanges>

    override val actions: Observable<SearchAction>
        get() = searchInput.actions

    private var wireDisposable: Disposable? = null
    private var bindDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector().inject(this)
        setContentView(R.layout.activity_search)
        searchInput = search_input
        resultsView = search_results

        wireDisposable = screenStore.wire()
    }

    override fun onStart() {
        super.onStart()
        bindDisposable = screenStore.bind(this)
    }

    override fun render(state: ScreenState) {
        if (state.results != null) {
            searchInput.currentQuery = state.queryString
            resultsView.showResults(state.results!!)
        }

        Log.v("APP", "state: $state")
    }

    override fun onStop() {
        bindDisposable?.dispose()
        super.onStop()
    }

    override fun onDestroy() {
        wireDisposable?.dispose()
        super.onDestroy()
    }

    class Injector {
        fun inject(searchActivity: SearchActivity) {
            val dependencies = searchActivity.application as Dependencies
            val networkDependencyProvider = dependencies.networkDependencyProvider
            val searchDependencyProvider = SearchDependencyProvider(
                networkDependencyProvider,
                dependencies.endpoints
            )
            searchActivity.screenStore = searchDependencyProvider.provideSearchStore()
        }
    }
}
