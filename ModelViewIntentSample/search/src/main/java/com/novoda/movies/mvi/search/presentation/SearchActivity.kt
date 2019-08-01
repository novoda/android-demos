package com.novoda.movies.mvi.search.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.novoda.movies.mvi.search.*
import com.novoda.movies.mvi.search.domain.SearchAction
import com.novoda.movies.mvi.search.domain.SearchChanges
import com.novoda.movies.mvi.search.domain.SearchDependencyProvider
import com.novoda.movies.mvi.search.domain.SearchState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*

internal class SearchActivity : AppCompatActivity(),
        ActionProvider<SearchAction>,
        ViewRender<SearchState> {

    private lateinit var searchInput: SearchInputView
    private lateinit var resultsView: SearchResultsView

    lateinit var searchStore: BaseStore<SearchAction, SearchState, SearchChanges>

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

        wireDisposable = searchStore.wire()
    }

    override fun onStart() {
        super.onStart()
        bindDisposable = searchStore.bind(actionProvider = this, viewRender = this)
    }

    override fun render(state: SearchState) {
        when (state) {
            is SearchState.Content -> {
                searchInput.currentQuery = state.queryString
                resultsView.showResults(state.results)
            }

            //TODO: Handle Loading
            //TODO: Handle Error
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
            searchActivity.searchStore = searchDependencyProvider.provideSearchStore()
        }
    }
}
