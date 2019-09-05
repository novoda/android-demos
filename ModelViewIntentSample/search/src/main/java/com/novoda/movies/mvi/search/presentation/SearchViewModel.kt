package com.novoda.movies.mvi.search.presentation

import android.arch.lifecycle.ViewModel
import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.Displayer
import com.novoda.movies.mvi.search.domain.ScreenState
import com.novoda.movies.mvi.search.domain.ScreenStateChanges
import com.novoda.movies.mvi.search.domain.SearchAction
import io.reactivex.disposables.Disposable

internal typealias SearchStore = BaseStore<SearchAction, ScreenState, ScreenStateChanges>

internal class SearchViewModel(private val store: SearchStore) : ViewModel() {

    private var wireDisposable: Disposable? = null
    private var bindDisposable: Disposable? = null

    init {
        wireDisposable = store.wire()
    }

    fun bind(displayer: Displayer<SearchAction, ScreenState>) {
        bindDisposable = store.bind(displayer = displayer)
    }

    override fun onCleared() {
        super.onCleared()

        unbind()
        wireDisposable?.dispose()
    }

    fun unbind() = bindDisposable?.dispose()
}


