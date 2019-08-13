package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Reducer
import com.novoda.movies.mvi.search.presentation.SearchResultsConverter
import com.novoda.movies.mvi.search.presentation.ViewSearchResults

internal class SearchReducer(
    private val searchResultsConverter: SearchResultsConverter
) : Reducer<ScreenState, ScreenStateChanges> {

    override fun reduce(state: ScreenState, change: ScreenStateChanges): ScreenState =
        when (change) {
            ScreenStateChanges.IndicateProgress -> ScreenState.Loading(
                queryString = state.queryString
            )
            is ScreenStateChanges.AddResults -> ScreenState.Content(
                queryString = state.queryString,
                results = searchResultsConverter.convert(change.results)
            )
            is ScreenStateChanges.HandleError -> ScreenState.Error(
                queryString = state.queryString,
                throwable = change.throwable
            )
            is ScreenStateChanges.UpdateSearchQuery -> ScreenState.Content(
                queryString = change.queryString,
                results = ViewSearchResults()
            )
        }
}

internal sealed class ScreenState {

    abstract val queryString: String

    data class Content(override val queryString: String, val results: ViewSearchResults) : ScreenState()
    data class Loading(override val queryString: String) : ScreenState()
    data class Error(override val queryString: String, val throwable: Throwable) : ScreenState()
}

sealed class ScreenStateChanges {

    object IndicateProgress : ScreenStateChanges()
    data class AddResults(val results: SearchResults) : ScreenStateChanges()
    data class HandleError(val throwable: Throwable) : ScreenStateChanges()
    data class UpdateSearchQuery(val queryString: String) : ScreenStateChanges()
}
