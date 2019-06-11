package com.novoda.movies.mvi.search

import com.novoda.movies.mvi.search.SearchResultsModel.State
import com.novoda.movies.mvi.search.SearchResultsModel.State.*
import com.novoda.movies.mvi.search.api.SearchBackend
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

internal class RealSearchResultsModel(
    private val backend: SearchBackend,
    private val schedulingStrategy: SchedulingStrategy
) : SearchResultsModel {
    private val state: Subject<State> = BehaviorSubject.createDefault(Initial)

    private val actions: Subject<Action> = PublishSubject.create()
    init {
        actions
            .withLatestFrom(state, actionToState())
            .switchMap { (action, state) ->
                handle(action, state)
            }.subscribe(state)

    }

    private fun actionToState(): BiFunction<Action, State, Pair<Action, State>> {
        return BiFunction { action, state ->
            action to state
        }
    }

    private fun handle(action: Action, state: State): Observable<State> {
        return when (action) {
            is Action.ChangeQuery -> textInput(action.queryString)
            is Action.ClearQuery -> textInputWithEmptyQuery()
            is Action.ExecuteSearch -> processQuery(state.queryString)
        }
    }

    private fun textInputWithEmptyQuery(): Observable<State> =
        Observable.just(TextInput("") as State)

    private fun textInput(queryString: String) =
        Observable.just(TextInput(queryString) as State)

    private fun processQuery(query: String): Observable<State> {
        return backend.search(query)
            .toObservable()
            .map { results -> Content(query, results) as State }
            .startWith(Loading(query))
            .onErrorReturn { throwable -> Error(query, throwable) }
            .subscribeOn(schedulingStrategy.work)
    }

    override fun state(): Observable<State> = state
        .subscribeOn(schedulingStrategy.work)
        .observeOn(schedulingStrategy.ui)

    override fun executeSearch() = actions.onNext(Action.ExecuteSearch)

    override fun queryChanged(queryString: String) =
        actions.onNext(Action.ChangeQuery(queryString))

    override fun clearQuery() {
        actions.onNext(Action.ClearQuery)
    }

    private sealed class Action {
        data class ChangeQuery(val queryString: String) : Action()
        object ExecuteSearch : Action()
        object ClearQuery : Action()
    }
}

