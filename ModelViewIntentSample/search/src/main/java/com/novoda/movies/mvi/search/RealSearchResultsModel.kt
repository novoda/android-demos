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

    private val stateChange: Subject<StateChange> = PublishSubject.create()

    init {
        stateChange.scan(
            Initial as State, asState()
        ).doOnNext {
            state.onNext(it)
        }.subscribe()
    }

    private fun asState(): BiFunction<State, in StateChange, State> {
        return BiFunction { state, changes ->
            return@BiFunction when (changes) {
                StateChange.IndicateProgress -> Loading(state.queryString)
                is StateChange.ChangeQuery -> TextInput(changes.queryString)
                is StateChange.ShowResults -> Content(state.queryString, changes.searchResults)
                is StateChange.ShowError -> Error(state.queryString, changes.throwable)
            }
        }
    }

    override fun state(): Observable<State> = state
        .observeOn(schedulingStrategy.ui)

    override fun executeSearch() {
        Observable.just(true)
            .withLatestFrom(state, queryString())
            .flatMap { query ->
                backend.search(query)
                    .toObservable()
                    .map { results -> StateChange.ShowResults(results) as StateChange }
                    .startWith(StateChange.IndicateProgress)
                    .onErrorReturn { throwable -> StateChange.ShowError(throwable) }
                    .subscribeOn(schedulingStrategy.work)
            }.doOnNext { change ->
                this.stateChange.onNext(change)
            }.subscribe()

    }

    private fun queryString(): BiFunction<Boolean, State, String> {
        return BiFunction { _, state ->
            state.queryString
        }
    }

    override fun queryChanged(queryString: String) =
        stateChange.onNext(StateChange.ChangeQuery(queryString = queryString))

    override fun clearQuery() {
        stateChange.onNext(StateChange.ChangeQuery(queryString = ""))
    }

    private sealed class StateChange {
        object IndicateProgress : StateChange()
        data class ChangeQuery(val queryString: String) : StateChange()
        data class ShowResults(val searchResults: SearchResults) : StateChange()
        data class ShowError(val throwable: Throwable) : StateChange()
    }
}
