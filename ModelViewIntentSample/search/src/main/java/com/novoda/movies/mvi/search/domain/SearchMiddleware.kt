package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Middleware
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.domain.SearchChanges.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction


internal class SearchMiddleware(
    private val backend: SearchBackend,
    private val workScheduler: Scheduler
) : Middleware<SearchAction, SearchState, SearchChanges> {

    override fun bind(actions: Observable<SearchAction>, state: Observable<SearchState>): Observable<SearchChanges> {
        return actions
            .withLatestFrom(state, actionToState())
            .switchMap { (action, state) -> handle(action, state) }
    }

    private fun actionToState(): BiFunction<SearchAction, SearchState, Pair<SearchAction, SearchState>> =
        BiFunction { action, state -> action to state }

    private fun handle(action: SearchAction, state: SearchState): Observable<SearchChanges> =
        when (action) {
            is SearchAction.ChangeQuery -> Observable.just(SearchQueryUpdate(action.queryString))
            is SearchAction.ExecuteSearch -> processAction(state)
            is SearchAction.ClearQuery -> TODO()
        }

    private fun processAction(state: SearchState): Observable<SearchChanges> {
        return backend.search(state.queryString)
            .toObservable()
            .map { searchResult -> SearchCompleted(searchResult) as SearchChanges }
            .startWith(SearchInProgress)
            .onErrorReturn { throwable -> SearchFailed(throwable) }
            .subscribeOn(workScheduler)
    }
}

internal sealed class SearchAction {
    data class ChangeQuery(val queryString: String) : SearchAction()
    object ExecuteSearch : SearchAction()
    object ClearQuery : SearchAction()
}

data class SearchState(
    val queryString: String,
    val searchResults: SearchResults,
    val isLoading: Boolean,
    val throwable: Throwable?
) {
    companion object {
        fun initialState(): SearchState {
            return SearchState("", SearchResults(emptyList(), 0), false, null)
        }
    }
}

sealed class SearchChanges {

    object SearchInProgress : SearchChanges()
    data class SearchCompleted(val results: SearchResults) : SearchChanges()
    data class SearchFailed(val throwable: Throwable) : SearchChanges()
    data class SearchQueryUpdate(val queryString: String) : SearchChanges()
}
