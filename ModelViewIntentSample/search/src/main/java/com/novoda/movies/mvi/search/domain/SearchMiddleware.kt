package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Middleware
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.domain.ScreenStateChanges.AddResults
import com.novoda.movies.mvi.search.domain.ScreenStateChanges.HandleError
import com.novoda.movies.mvi.search.domain.ScreenStateChanges.HideProgress
import com.novoda.movies.mvi.search.domain.ScreenStateChanges.RemoveResults
import com.novoda.movies.mvi.search.domain.ScreenStateChanges.ShowProgress
import com.novoda.movies.mvi.search.domain.ScreenStateChanges.UpdateSearchQuery
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
                is SearchAction.ChangeQuery -> Observable.just(UpdateSearchQuery(action.queryString))
                is SearchAction.ExecuteSearch -> processAction(state)
                is SearchAction.ClearQuery -> processClearQuery()
            }

    private fun processClearQuery(): Observable<ScreenStateChanges> {
        val updateSearch = Observable.just(UpdateSearchQuery("") as ScreenStateChanges)
        val removeResults = Observable.just(RemoveResults)
        return updateSearch.concatWith(removeResults)
    }

    private fun processAction(state: ScreenState): Observable<ScreenStateChanges> {
        val loadContent = backend.search(state.queryString)
                .toObservable()
                .map { searchResult -> AddResults(searchResult) as ScreenStateChanges }
                .startWith(ShowProgress)
                .onErrorReturn { throwable -> HandleError(throwable) }
        val hideProgress = Observable.just(HideProgress)

        return loadContent
                .concatWith(hideProgress)
                .subscribeOn(workScheduler)
    }
}

internal sealed class SearchAction {
    data class ChangeQuery(val queryString: String) : SearchAction()
    object ExecuteSearch : SearchAction()
    object ClearQuery : SearchAction()
}


