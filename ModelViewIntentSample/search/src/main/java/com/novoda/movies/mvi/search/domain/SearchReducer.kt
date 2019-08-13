package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Reducer
import com.novoda.movies.mvi.search.presentation.SearchResultsConverter
import com.novoda.movies.mvi.search.presentation.ViewSearchResults

internal class SearchReducer(
    private val searchResultsConverter: SearchResultsConverter
) : Reducer<ScreenState, ScreenStateChanges> {

    override fun reduce(state: ScreenState, change: ScreenStateChanges): ScreenState =
        when (change) {
            ScreenStateChanges.ShowProgress -> state.showLoading()
            ScreenStateChanges.HideProgress -> state.hideLoading()
            is ScreenStateChanges.AddResults -> state.addResults(change.results)
            is ScreenStateChanges.UpdateSearchQuery -> state.updateQuery(change.queryString)
            is ScreenStateChanges.HandleError -> state.toError(change.throwable)
        }

    private fun ScreenState.addResults(results: SearchResults): ScreenState {
        return copy(results = searchResultsConverter.convert(results))
    }

}

private fun ScreenState.toError(throwable: Throwable): ScreenState {
    return copy(error = throwable)
}

private fun ScreenState.updateQuery(queryString: String): ScreenState {
    return copy(queryString = queryString)
}

private fun ScreenState.hideLoading(): ScreenState {
    return copy(loading = false)
}

private fun ScreenState.showLoading(): ScreenState {
    return copy(loading = true)
}


internal data class ScreenState(
    var queryString: String,
    var loading: Boolean = false,
    var results: ViewSearchResults? = null,
    var error: Throwable? = null
)

sealed class ScreenStateChanges {

    object ShowProgress : ScreenStateChanges()
    object HideProgress : ScreenStateChanges()
    data class AddResults(val results: SearchResults) : ScreenStateChanges()
    data class HandleError(val throwable: Throwable) : ScreenStateChanges()
    data class UpdateSearchQuery(val queryString: String) : ScreenStateChanges()
}
