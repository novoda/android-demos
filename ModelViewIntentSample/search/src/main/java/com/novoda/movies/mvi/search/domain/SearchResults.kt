package com.novoda.movies.mvi.search.domain

import java.net.URL

data class SearchResults(
    val items: List<SearchResultItem> = listOf(),
    val totalResults: Int = 0
)

data class SearchResultItem(
    val id: Int,
    val title: String,
    val posterPath: URL
)
