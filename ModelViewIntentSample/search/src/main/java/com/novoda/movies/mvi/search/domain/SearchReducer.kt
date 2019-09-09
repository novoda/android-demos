package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Reducer
import com.novoda.movies.mvi.search.domain.SearchReducer.Changes.*
import com.novoda.movies.mvi.search.presentation.SearchResultsConverter
import com.novoda.movies.mvi.search.presentation.SearchViewModel.State
import com.novoda.movies.mvi.search.presentation.ViewSearchResults

internal class SearchReducer(
        private val searchResultsConverter: SearchResultsConverter
) : Reducer<State, SearchReducer.Changes> {

    override fun reduce(state: State, change: Changes): State =
            when (change) {
                is ShowProgress -> state.showLoading()
                is HideProgress -> state.hideLoading()
                is AddResults -> state.addResults(change.results)
                is RemoveResults -> state.removeResults()
                is UpdateSearchQuery -> state.updateQuery(change.queryString)
                is HandleError -> state.toError(change.throwable)
            }

    private fun State.addResults(results: SearchResults): State {
        return copy(results = searchResultsConverter.convert(results))
    }

    sealed class Changes {
        object ShowProgress : Changes()
        object HideProgress : Changes()
        data class AddResults(val results: SearchResults) : Changes()
        object RemoveResults : Changes()
        data class HandleError(val throwable: Throwable) : Changes()
        data class UpdateSearchQuery(val queryString: String) : Changes()
    }

}

private fun State.removeResults(): State {
    return copy(results = ViewSearchResults.emptyResults)
}

private fun State.toError(throwable: Throwable): State {
    return copy(error = throwable)
}

private fun State.updateQuery(queryString: String): State {
    return copy(queryString = queryString)
}

private fun State.hideLoading(): State {
    return copy(loading = false)
}

private fun State.showLoading(): State {
    return copy(loading = true)
}
