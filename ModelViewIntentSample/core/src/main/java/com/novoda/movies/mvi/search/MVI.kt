package com.novoda.movies.mvi.search

import io.reactivex.Observable
import io.reactivex.disposables.Disposable

interface Reducer<S, C> {
    fun reduce(state: S, change: C): S
}

interface Middleware<A, S, C> {
    fun bind(actions: Observable<A>, state: Observable<S>): Observable<C>
}

interface Store<A, S, C> {
    fun wire(): Disposable
    fun bind(actions: Observable<A>): Disposable
}
