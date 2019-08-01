package com.novoda.movies.mvi.search.data

import com.novoda.movies.mvi.search.Endpoints
import com.novoda.movies.mvi.search.domain.SearchResultItem
import com.novoda.movies.mvi.search.domain.SearchResults
import java.net.URL

internal class ApiSearchResultsConverter(
    private val endpoints: Endpoints
) {

    fun convert(apiSearchResults: ApiSearchResults): SearchResults {
        return apiSearchResults.toSearchResults()
    }

    private fun ApiSearchResults.toSearchResults(): SearchResults {
        return SearchResults(
            items = results.map {
                it.toSearchResultItem()
            },
            totalResults = total_results
        )
    }

    private fun ApiSearchResultItem.toSearchResultItem(): SearchResultItem {
        return SearchResultItem(
            id,
            title,
            URL("${endpoints.baseImageUrl}$poster_path")
        )
    }
}
