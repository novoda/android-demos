package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.Endpoints
import com.novoda.movies.mvi.search.NetworkDependencyProvider
import com.novoda.movies.mvi.search.ProductionSchedulingStrategy
import com.novoda.movies.mvi.search.data.ApiSearchResultsConverter
import com.novoda.movies.mvi.search.data.SearchApi
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.presentation.SearchResultsConverter
import com.novoda.movies.mvi.search.presentation.ViewSearchResults

internal class SearchDependencyProvider(
        private val networkDependencyProvider: NetworkDependencyProvider,
        private val endpoints: Endpoints
) {

    private fun provideSearchBackend(): SearchBackend {
        val searchApi = networkDependencyProvider.provideRetrofit().create(SearchApi::class.java)
        return SearchBackend(
                searchApi,
                ApiSearchResultsConverter(endpoints)
        )
    }

    fun provideSearchStore(): BaseStore<SearchAction, ScreenState, ScreenStateChanges> {
        return BaseStore(
            reducer = SearchReducer(provideSearchResultsConverter()),
            schedulingStrategy = ProductionSchedulingStrategy(),
            middlewares = listOf(SearchMiddleware(provideSearchBackend(), ProductionSchedulingStrategy().work)),
            initialValue = ScreenState.Content(queryString = "", results = ViewSearchResults())
        )
    }

    private fun provideSearchResultsConverter() = SearchResultsConverter()
}
