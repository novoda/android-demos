package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Reducer

class SearchReducer: Reducer<SearchState, SearchChanges> {

    override fun reduce(state: SearchState, change: SearchChanges): SearchState =
            when (change) {
                SearchChanges.SearchInProgress -> state
                is SearchChanges.SearchCompleted -> state
                is SearchChanges.SearchFailed -> state
                is SearchChanges.SearchQueryUpdate -> state.copy(queryString = change.queryString)
            }
}
