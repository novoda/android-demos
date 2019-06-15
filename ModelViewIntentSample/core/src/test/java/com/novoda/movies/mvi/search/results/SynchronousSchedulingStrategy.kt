package com.novoda.movies.mvi.search.results

import com.novoda.movies.mvi.search.SchedulingStrategy
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SynchronousSchedulingStrategy : SchedulingStrategy {
    override val work: Scheduler = Schedulers.trampoline()
    override val ui: Scheduler = Schedulers.trampoline()
}
