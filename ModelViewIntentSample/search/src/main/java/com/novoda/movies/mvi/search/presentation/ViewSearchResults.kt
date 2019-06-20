package com.novoda.movies.mvi.search.presentation

import java.net.URL

internal data class ViewSearchResults(
    val totalItemCount: Int,
    val items: List<ViewSearchItem>
)

internal data class ViewSearchItem(
    val id: String,
    val name: String,
    val thumbnailUrl: URL
)
