package com.novoda.movies.mvi.search.presentation

import java.net.URL

internal data class ViewSearchResults(
    val totalItemCount: Int,
    val items: List<ViewSearchItem>
) {
    companion object {
        val emptyResults = ViewSearchResults(0, emptyList())
    }
}

internal data class ViewSearchItem(
    val id: String,
    val name: String,
    val thumbnailUrl: URL
)
