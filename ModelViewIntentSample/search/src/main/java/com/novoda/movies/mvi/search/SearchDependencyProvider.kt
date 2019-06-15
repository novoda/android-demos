package com.novoda.movies.mvi.search

import com.novoda.movies.mvi.search.api.ApiSearchResultsConverter
import com.novoda.movies.mvi.search.api.SearchApi
import com.novoda.movies.mvi.search.api.SearchBackend
import com.novoda.movies.mvi.search.view.SearchResultsConverter

internal class SearchDependencyProvider(
    private val networkDependencyProvider: NetworkDependencyProvider,
    private val endpoints: Endpoints
) {

    private fun provideSearchResultsModel(): SearchResultsModel {
        return RealSearchResultsModel(
            provideSearchBackend(),
            ProductionSchedulingStrategy()
        )
    }

    private fun provideSearchBackend(): SearchBackend {
        val searchApi = networkDependencyProvider.provideRetrofit().create(SearchApi::class.java)
        return SearchBackend(
            searchApi,
            ApiSearchResultsConverter(endpoints)
        )
    }

    fun provideSearchResultsPresenter(): SearchResultsPresenter {
        return SearchResultsPresenter(
            provideSearchResultsModel(),
            SearchResultsConverter()
        )
    }
}
