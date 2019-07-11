package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.BaseStore
import com.novoda.movies.mvi.search.Endpoints
import com.novoda.movies.mvi.search.NetworkDependencyProvider
import com.novoda.movies.mvi.search.ProductionSchedulingStrategy
import com.novoda.movies.mvi.search.data.ApiSearchResultsConverter
import com.novoda.movies.mvi.search.data.SearchApi
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.presentation.SearchResultsConverter
import com.novoda.movies.mvi.search.presentation.SearchResultsPresenter

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

    fun provideSearchStore(): BaseStore<SearchAction, SearchState, SearchChanges> {
        return BaseStore(
                reducer = SearchReducer(),
                schedulingStrategy = ProductionSchedulingStrategy(),
                middlewares = listOf(SearchMiddleware(provideSearchBackend(),ProductionSchedulingStrategy().work)),
                initialValue = SearchState.Content(queryString = "", results = SearchResults())
        )
    }
}
