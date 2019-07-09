package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Reducer

class SearchReducer: Reducer<SearchState, SearchChanges> {

    override fun reduce(state: SearchState, change: SearchChanges): SearchState =
            when (change) {
                SearchChanges.SearchInProgress -> state.copy(
                        searchResults = SearchResults(emptyList(), 0),
                        isLoading = true,
                        throwable = null
                )
                is SearchChanges.SearchCompleted -> state.copy(
                        searchResults = change.results,
                        isLoading = false,
                        throwable = null
                )
                is SearchChanges.SearchFailed -> state.copy(
                        searchResults = SearchResults(emptyList(), 0),
                        isLoading = false,
                        throwable = change.throwable
                )
                is SearchChanges.SearchQueryUpdate -> state.copy(
                        searchResults = SearchResults(emptyList(), 0),
                        isLoading = false,
                        throwable = null,
                        queryString = change.queryString
                )
            }
}
