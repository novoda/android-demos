package com.novoda.movies.mvi.search.presentation

import android.arch.lifecycle.ViewModel
import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.Displayer
import com.novoda.movies.mvi.search.domain.SearchReducer
import io.reactivex.disposables.Disposable

internal typealias SearchStore = BaseStore<SearchActivity.Action, SearchActivity.State, SearchReducer.Changes>
internal typealias SearchDisplayer = Displayer<SearchActivity.Action, SearchActivity.State>

internal class SearchViewModel(private val store: SearchStore) : ViewModel() {
    private val wireDisposable = store.wire()
    private var bindDisposable: Disposable? = null

    fun bind(displayer: SearchDisplayer) {
        bindDisposable = store.bind(displayer = displayer)
    }

    override fun onCleared() {
        super.onCleared()

        unbind()
        wireDisposable.dispose()
    }

    fun unbind() = bindDisposable?.dispose()
}
