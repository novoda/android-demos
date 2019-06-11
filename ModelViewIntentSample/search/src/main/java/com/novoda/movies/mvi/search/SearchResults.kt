package com.novoda.movies.mvi.search

import java.net.URL

internal data class SearchResults(
    val items: List<SearchResultItem>,
    val totalResults: Int
)

internal data class SearchResultItem(
    val id: Int,
    val title: String,
    val posterPath: URL
)
