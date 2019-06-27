package com.novoda.movies.mvi.search.domain

import java.net.URL

data class SearchResults(
    val items: List<SearchResultItem>,
    val totalResults: Int
)

data class SearchResultItem(
    val id: Int,
    val title: String,
    val posterPath: URL
)
