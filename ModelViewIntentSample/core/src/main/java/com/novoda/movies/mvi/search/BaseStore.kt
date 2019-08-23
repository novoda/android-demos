package com.novoda.movies.mvi.search

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
    private val state = BehaviorSubject.createDefault(initialValue)
    private val actions: PublishSubject<A> = PublishSubject.create()

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
                .subscribe(state::onNext)
        )

        for (middleware in middlewares) {
            val observable = middleware
                .bind(actions, state)
                .subscribeOn(schedulingStrategy.work)
            disposables.add(observable.subscribe(changes::onNext))
        }

        return disposables
    }

    override fun bind(view: MVIView<A, S>): Disposable {
        val disposables = CompositeDisposable()

        disposables.add(view.actions.subscribe(actions::onNext))

        disposables.add(
            state
                .observeOn(schedulingStrategy.ui)
                .subscribe(view::render)
        )

        return disposables
    }
}
