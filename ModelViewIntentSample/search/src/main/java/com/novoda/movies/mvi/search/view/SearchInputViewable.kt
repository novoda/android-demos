package com.novoda.movies.mvi.search.view

internal interface SearchInputViewable {
    var currentQuery: String
    var onQuerySubmitted: () -> Unit
    var onQueryChanged: (query: String) -> Unit

    fun showKeyboard()
    var onQueryCleared: () -> Unit
}
