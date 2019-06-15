package com.novoda.movies.mvi.search.view

internal interface SearchResultsViewable {
    fun showResults(results: ViewSearchResults)
    fun showTextInput()
    fun showNoResults(attemptedQuery: String)
}
