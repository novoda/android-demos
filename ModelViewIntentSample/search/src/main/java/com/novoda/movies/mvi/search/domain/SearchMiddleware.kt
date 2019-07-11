package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Middleware
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.domain.SearchChanges.*
import com.novoda.movies.mvi.search.presentation.ViewSearchResults
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


internal sealed class SearchState {

    abstract val queryString: String

    data class Content(override val queryString: String, val results: ViewSearchResults) : SearchState()
    data class Loading(override val queryString: String) : SearchState()
    data class Error(override val queryString: String, val throwable: Throwable) : SearchState()
}

sealed class SearchChanges {

    object SearchInProgress : SearchChanges()
    data class SearchCompleted(val results: SearchResults) : SearchChanges()
    data class SearchFailed(val throwable: Throwable) : SearchChanges()
    data class SearchQueryUpdate(val queryString: String) : SearchChanges()
}
