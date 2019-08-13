package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Reducer
import com.novoda.movies.mvi.search.presentation.SearchResultsConverter
import com.novoda.movies.mvi.search.presentation.ViewSearchResults

internal class SearchReducer(
    private val searchResultsConverter: SearchResultsConverter
) : Reducer<ScreenState, ScreenStateChanges> {

    override fun reduce(state: ScreenState, change: ScreenStateChanges): ScreenState =
        when (change) {
            ScreenStateChanges.ScreenStateInProgress -> ScreenState.Loading(
                queryString = state.queryString
            )
            is ScreenStateChanges.ScreenStateCompleted -> ScreenState.Content(
                queryString = state.queryString,
                results = searchResultsConverter.convert(change.results)
            )
            is ScreenStateChanges.ScreenStateFailed -> ScreenState.Error(
                queryString = state.queryString,
                throwable = change.throwable
            )
            is ScreenStateChanges.ScreenStateQueryUpdate -> ScreenState.Content(
                queryString = change.queryString,
                results = ViewSearchResults()
            )
        }
}
