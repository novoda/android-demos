package com.novoda.movies.mvi.search

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class BaseStore<A, S, C>(
    private val schedulingStrategy: SchedulingStrategy,
    private val reducer: Reducer<S, C>,
    private val middlewares: List<Middleware<A, S, C>>,
    initialValue: S
) : Store<A, S, C> {
    private val changes = BehaviorSubject.create<C>()
    private val state = BehaviorSubject.createDefault(initialValue)
    private val actions: PublishSubject<A> = PublishSubject.create()

    override fun wire(): Disposable {
        val disposables = CompositeDisposable()
        val newState = changes.withLatestFrom(state, BiFunction<C, S, S> { change, state ->
            reducer.reduce(state, change)
        })
        disposables.add(newState
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

    override fun bind(actionProvider: ActionProvider<A>, viewRender: ViewRender<S>): Disposable {
        val disposables = CompositeDisposable()

        disposables.add(actionProvider.actions.subscribe(actions::onNext))

        disposables.add(state
                .observeOn(schedulingStrategy.ui)
                .subscribe(viewRender::render)
        )

        return disposables
    }
}