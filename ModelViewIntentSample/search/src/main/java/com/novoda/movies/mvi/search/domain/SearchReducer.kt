package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Reducer

class SearchReducer : Reducer<SearchState, SearchChanges> {

    override fun reduce(state: SearchState, change: SearchChanges): SearchState =
        when (change) {
            SearchChanges.SearchInProgress -> SearchState.Loading(queryString = state.queryString)
            is SearchChanges.SearchCompleted -> SearchState.Content(queryString = state.queryString, results = change.results)
            is SearchChanges.SearchFailed -> SearchState.Error(queryString = state.queryString, throwable = change.throwable)
            is SearchChanges.SearchQueryUpdate -> SearchState.Content(
                queryString = change.queryString,
                results = SearchResults()
            )
        }
}
