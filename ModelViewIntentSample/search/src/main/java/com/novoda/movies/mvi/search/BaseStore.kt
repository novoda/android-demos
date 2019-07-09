package com.novoda.movies.mvi.search

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class BaseStore<A, S, C>(
        private val schedulingStrategy: SchedulingStrategy,
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
        disposables.add(newState
                .subscribeOn(schedulingStrategy.work)
                .subscribe(state::onNext)
        )

        return disposables
    }

    override fun bind(view: MVIView<A, S>): Disposable {
        val disposables = CompositeDisposable()

        for (middleware in middlewares) {
            val observable = middleware
                    .bind(view.actions, state)
                    .subscribeOn(schedulingStrategy.work)
            disposables.add(observable.subscribe(changes::onNext))
        }

        disposables.add(state
                .observeOn(schedulingStrategy.ui)
                .subscribe(view::render)
        )

        return disposables
    }

    fun unbind() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
