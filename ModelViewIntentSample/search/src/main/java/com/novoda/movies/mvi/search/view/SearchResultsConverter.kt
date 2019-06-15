package com.novoda.movies.mvi.search.view

import com.novoda.movies.mvi.search.SearchResultItem
import com.novoda.movies.mvi.search.SearchResults

internal open class SearchResultsConverter {

    fun convert(searchResults: SearchResults): ViewSearchResults =
        ViewSearchResults(
            searchResults.totalResults,
            convertItems(searchResults.items)
        )

    open fun convertItems(list: List<SearchResultItem>): List<ViewSearchItem> {
        return list.map {
            it.toViewSearchItem()
        }
    }
}

private fun SearchResultItem.toViewSearchItem(): ViewSearchItem =
    ViewSearchItem(id.toString(), title, posterPath)
