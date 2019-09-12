package com.novoda.movies.mvi.search

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BaseStore<A, S, C>(
        private val schedulingStrategy: SchedulingStrategy,
        private val reducer: Reducer<S, C>,
        private val middlewares: List<Middleware<A, S, C>>,
        private val initialValue: S
) : Store<A, S, C> {
    private val changes = PublishSubject.create<C>()

    private val stateSubject = BehaviorSubject.createDefault(initialValue)
    val state : Observable<S> = stateSubject.observeOn(schedulingStrategy.ui)

    private val viewActions: PublishSubject<A> = PublishSubject.create()

    override fun wire(): Disposable {
        val disposables = CompositeDisposable()

        val newState = changes.scan(
                initialValue, { state, change ->
            reducer.reduce(state, change)
        }
        )

        disposables.add(
                newState
                        .subscribeOn(schedulingStrategy.work)
                        .subscribe(stateSubject::onNext)
        )

        for (middleware in middlewares) {
            val observable = middleware
                    .bind(viewActions, stateSubject)
                    .subscribeOn(schedulingStrategy.work)
            disposables.add(observable.subscribe(changes::onNext))
        }

        return disposables
    }

    override fun bind(actions: Observable<A>): Disposable {
        val disposables = CompositeDisposable()
        disposables.add(actions.subscribe(viewActions::onNext))

        return disposables
    }
}
