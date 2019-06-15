package com.novoda.movies.mvi.search.api


internal data class ApiSearchResults(
    val results: List<ApiSearchResultItem>,
    val total_results: Int
)

internal data class ApiSearchResultItem(
    val id: Int,
    val title: String,
    val poster_path: String
)
