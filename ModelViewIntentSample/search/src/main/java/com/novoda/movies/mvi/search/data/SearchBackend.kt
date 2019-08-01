package com.novoda.movies.mvi.search.data

import com.novoda.movies.mvi.search.domain.SearchResults
import io.reactivex.Single

interface MovieDataSource {
    fun search(query: String): Single<SearchResults>
}

internal class SearchBackend(
    private val searchApi: SearchApi,
    private val searchConverter: ApiSearchResultsConverter
): MovieDataSource {

    override fun search(query: String): Single<SearchResults> {
        return searchApi
            .search(query)
            .map(searchConverter::convert)
    }
}
