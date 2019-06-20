package com.novoda.movies.mvi.search.presentation

internal interface SearchResultsViewable {
    fun showResults(results: ViewSearchResults)
    fun showTextInput()
    fun showNoResults(attemptedQuery: String)
}
