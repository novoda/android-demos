package com.novoda.movies.mvi.search

import android.util.Log
import com.novoda.movies.mvi.search.SearchResultsModel.State
import com.novoda.movies.mvi.search.view.SearchInputViewable
import com.novoda.movies.mvi.search.view.SearchResultsConverter
import com.novoda.movies.mvi.search.view.SearchResultsViewable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal class SearchResultsPresenter(
    private val model: SearchResultsModel,
    private val converter: SearchResultsConverter
) {
    private val disposables = CompositeDisposable()

    fun bind(
        inputView: SearchInputViewable,
        resultsView: SearchResultsViewable
    ) {
        inputView.onQuerySubmitted = ::executeSearch
        inputView.onQueryCleared = ::clearQuery
        inputView.onQueryChanged = ::changeQuery

        model.state()
            .subscribeToState(
                onTextInput = {
                    resultsView.showTextInput()
                },
                onContent = {
                    displayResults(it, resultsView)
                },
                onInitial = {
                    showInitial(inputView, resultsView)
                },
                onEachState = {
                    renderInput(it.queryString, inputView)
                },
                onError = {
                    Log.e("Movies", "Error", it)
                },
                onLoading = {
                    Log.i("Movies", "Loading Movies")
                }
            ).disposedBy(disposables)
    }

    private fun clearQuery() = model.clearQuery()

    private fun executeSearch() = model.executeSearch()

    private fun changeQuery(queryString: String) = model.queryChanged(queryString)

    private fun showInitial(inputView: SearchInputViewable, resultsView: SearchResultsViewable) {
        resultsView.showTextInput()
        inputView.showKeyboard()
    }

    private fun displayResults(state: State.Content, resultsView: SearchResultsViewable) {
        val viewResults = converter.convert(state.searchResults)
        if (viewResults.totalItemCount > 0) {
            resultsView.showResults(viewResults)
        } else {
            resultsView.showNoResults(state.queryString)
        }
    }

    private fun renderInput(queryString: String, inputView: SearchInputViewable) {
        inputView.currentQuery = queryString
    }

    fun unbind() {
        disposables.clear()
    }
}

@Suppress("LongParameterList")
private fun Observable<State>.subscribeToState(
    onTextInput: () -> Unit,
    onContent: (State.Content) -> Unit,
    onLoading: () -> Unit,
    onError: (throwable: Throwable) -> Unit,
    onInitial: () -> Unit,
    onEachState: (State) -> Unit
): Disposable {
    return subscribe { state ->
        when (state) {
            is State.TextInput -> onTextInput()
            is State.Content -> onContent(state)
            is State.Loading -> onLoading()
            is State.Error -> onError(state.throwable)
            is State.Initial -> onInitial()
        }
        onEachState(state)
    }
}

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable): Disposable =
    apply { compositeDisposable.add(this) }

