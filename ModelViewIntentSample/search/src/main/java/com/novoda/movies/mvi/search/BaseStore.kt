package com.novoda.movies.mvi.search

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class BaseStore<A, S, C>(
        private val reducer: Reducer<S, C>,
        private val middlewares: List<Middleware<A, S, C>>,
        initialValue: S
) : Store<A, S, C> {
    private val changes = BehaviorSubject.create<C>()
    private val state = BehaviorSubject.createDefault(initialValue)

    override fun wire(): Disposable {
        val disposables = CompositeDisposable()
        val newState = changes.withLatestFrom(state, BiFunction<C, S, S> { change, state ->
            reducer.reduce(state, change)
        })
        disposables.add(newState.subscribe(state::onNext))

        return disposables
    }

    override fun bind(view: MVIView<A, S>): Disposable {
        val disposables = CompositeDisposable()

        for (middleware in middlewares) {
            val observable = middleware.bind(view.actions, state)
            disposables.add(observable.subscribe(changes::onNext))
        }

        disposables.add(state.subscribe(view::render))

        return disposables
    }
}
