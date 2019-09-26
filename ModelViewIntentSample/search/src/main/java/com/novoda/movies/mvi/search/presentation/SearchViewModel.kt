package com.novoda.movies.mvi.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.domain.SearchReducer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal typealias SearchStore = BaseStore<SearchViewModel.Action, SearchViewModel.State, SearchReducer.Changes>

internal class SearchViewModel(private val store: SearchStore) : ViewModel() {
    private val viewModelScopeDisposable = CompositeDisposable()
    private var bindDisposable: Disposable? = null
    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun bind(actions: Observable<Action>) {
        bindDisposable = store.bind(actions)
    }

    init {
        viewModelScopeDisposable.add(store.wire())
        viewModelScopeDisposable.add(store.state.subscribe { state -> _state.postValue(state) })
    }

    fun unbind() = bindDisposable?.dispose()

    override fun onCleared() {
        super.onCleared()

        viewModelScopeDisposable.clear()
    }

    internal data class State(
        var queryString: String,
        var shouldUpdateDisplayedQuery: Boolean = false,
        var loading: Boolean = false,
        var results: ViewSearchResults,
        var error: Throwable? = null
    )

    internal sealed class Action {
        data class ChangeQuery(val queryString: String) : Action()
        object ExecuteSearch : Action()
        object ClearQuery : Action()
    }
}
