package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Middleware
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.domain.ScreenStateChanges.*
import com.novoda.movies.mvi.search.presentation.ViewSearchResults
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction


internal class SearchMiddleware(
        private val backend: SearchBackend,
        private val workScheduler: Scheduler
) : Middleware<SearchAction, ScreenState, ScreenStateChanges> {

    override fun bind(actions: Observable<SearchAction>, state: Observable<ScreenState>): Observable<ScreenStateChanges> {
        return actions
                .withLatestFrom(state, actionToState())
                .switchMap { (action, state) -> handle(action, state) }
    }

    private fun actionToState(): BiFunction<SearchAction, ScreenState, Pair<SearchAction, ScreenState>> =
            BiFunction { action, state -> action to state }

    private fun handle(action: SearchAction, state: ScreenState): Observable<ScreenStateChanges> =
            when (action) {
                is SearchAction.ChangeQuery -> Observable.just(ScreenStateQueryUpdate(action.queryString))
                is SearchAction.ExecuteSearch -> processAction(state)
                is SearchAction.ClearQuery -> Observable.just(ScreenStateQueryUpdate(""))
            }

    private fun processAction(state: ScreenState): Observable<ScreenStateChanges> {
        return backend.search(state.queryString)
                .toObservable()
                .map { searchResult -> ScreenStateCompleted(searchResult) as ScreenStateChanges }
                .startWith(ScreenStateInProgress)
                .onErrorReturn { throwable -> ScreenStateFailed(throwable) }
                .subscribeOn(workScheduler)
    }
}

internal sealed class SearchAction {
    data class ChangeQuery(val queryString: String) : SearchAction()
    object ExecuteSearch : SearchAction()
    object ClearQuery : SearchAction()
}


internal sealed class ScreenState {

    abstract val queryString: String

    data class Content(override val queryString: String, val results: ViewSearchResults) : ScreenState()
    data class Loading(override val queryString: String) : ScreenState()
    data class Error(override val queryString: String, val throwable: Throwable) : ScreenState()
}

sealed class ScreenStateChanges {

    object ScreenStateInProgress : ScreenStateChanges()
    data class ScreenStateCompleted(val results: SearchResults) : ScreenStateChanges()
    data class ScreenStateFailed(val throwable: Throwable) : ScreenStateChanges()
    data class ScreenStateQueryUpdate(val queryString: String) : ScreenStateChanges()
}
