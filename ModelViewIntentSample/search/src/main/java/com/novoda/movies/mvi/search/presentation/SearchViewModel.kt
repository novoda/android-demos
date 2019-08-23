package com.novoda.movies.mvi.search.presentation

import android.arch.lifecycle.ViewModel
import com.novoda.movies.mvi.search.ActionProvider
import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.ViewRender
import com.novoda.movies.mvi.search.domain.ScreenState
import com.novoda.movies.mvi.search.domain.ScreenStateChanges
import com.novoda.movies.mvi.search.domain.SearchAction
import io.reactivex.disposables.Disposable

internal typealias SearchStore = BaseStore<SearchAction, ScreenState, ScreenStateChanges>

internal class SearchViewModel(private val store: SearchStore) : ViewModel() {

    private var wireDisposable: Disposable? = null
    private var bindDisposable: Disposable? = null

    fun bind(actionProvider: ActionProvider<SearchAction>, viewRender: ViewRender<ScreenState>) {
        bindDisposable = store.bind(actionProvider = actionProvider, viewRender = viewRender)
    }

    fun wire() {
        wireDisposable = store.wire()
    }

    fun unbind() = bindDisposable?.dispose()
    fun unwire() = wireDisposable?.dispose()
}


