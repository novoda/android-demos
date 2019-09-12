package com.novoda.movies.mvi.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.domain.SearchReducer
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

internal typealias SearchStore = BaseStore<SearchViewModel.Action, SearchViewModel.State, SearchReducer.Changes>

internal class SearchViewModel(private val store: SearchStore) : ViewModel() {
    private val wireDisposable = store.wire()
    private var bindDisposable: Disposable? = null
    private var stateDisposable: Disposable? = null

    init {
        stateDisposable = store
            .state
            .subscribe { state ->
                _state.postValue(state)
            }
    }


    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state


    fun bind(actions: Observable<Action>) {
        bindDisposable = store.bind(actions)
    }

    override fun onCleared() {
        super.onCleared()

        unbind()
        wireDisposable.dispose()
        stateDisposable?.dispose()
    }

    fun unbind() = bindDisposable?.dispose()

    internal data class State(
        var queryString: String,
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
