package com.novoda.movies.mvi.search

import io.reactivex.Scheduler

interface SchedulingStrategy {
    val work: Scheduler
    val ui: Scheduler
}
