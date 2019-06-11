package com.novoda.movies.mvi.search.results

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.novoda.movies.mvi.search.RealSearchResultsModel
import com.novoda.movies.mvi.search.SearchResults
import com.novoda.movies.mvi.search.SearchResultsModel.State.*
import com.novoda.movies.mvi.search.api.SearchBackend
import io.reactivex.Single
import org.junit.Test

class RealSearchResultsModelTest {

    private val queryString = "gateau"
    private val backend: SearchBackend = mock()


    @Test
    fun `should emit initial state without initial query when initial query empty`() {
        model()
            .state()
            .test()
            .assertValue(Initial)
    }

    @Test
    fun `should show text input when user changes query`() {
        val searchResults = results()
        givenBackendSucceedWith(searchResults)
        val model = model()
        val state = model.state().test()

        model.queryChanged(queryString)

        state.assertValues(
            Initial,
            TextInput(queryString)
        )
    }

    @Test
    fun `should start with loading`() {
        givenBackendReturn(Single.never())
        val model = model()
        val state = model.state()

        model.queryChanged(queryString)
        model.executeSearch()

        state.test()
            .assertValue(Loading(queryString))
    }

    @Test
    fun `should emit result after loading`() {
        val searchResults = results()
        givenBackendSucceedWith(searchResults)
        val model = model()
        val state = model.state().test()

        model.queryChanged(queryString)
        model.executeSearch()

        state.assertValues(
            Initial,
            TextInput(queryString),
            Loading(queryString),
            Content(queryString, searchResults)
        )
    }

    @Test
    fun `should emit latest state`() {
        val searchResults = results()
        givenBackendSucceedWith(searchResults)
        val model = model()

        model.queryChanged(queryString)
        model.executeSearch()

        model.state()
            .test()
            .assertValues(Content(queryString, searchResults))
    }

    @Test
    fun `should emit error when search failed`() {
        val exception = Exception()
        givenBackendFailWith(exception)
        val model = model()

        model.queryChanged(queryString)
        model.executeSearch()

        model.state()
            .test()
            .assertValue(Error(queryString, exception))
    }

    @Test
    fun `should recover from error`() {
        val exception = Exception()
        givenBackendFailWith(exception)
        val model = model()
        val state = model.state().test()
        model.queryChanged(queryString)
        model.executeSearch()

        val searchResults = results()
        givenBackendSucceedWith(searchResults)
        model.executeSearch()

        state.assertValues(
            Initial,
            TextInput(queryString),
            Loading(queryString),
            Error(queryString, exception),
            Loading(queryString),
            Content(queryString, searchResults)
        )
    }

    private fun model(): RealSearchResultsModel {
        return RealSearchResultsModel(
            backend,
            SynchronousSchedulingStrategy()
        )
    }

    private fun results(): SearchResults = SearchResults(
        listOf(mock(), mock()),
        totalResults = 10
    )

    private fun givenBackendSucceedWith(searchResults: SearchResults) {
        givenBackendReturn(Single.just(searchResults))
    }

    private fun givenBackendReturn(single: Single<SearchResults>) {
        whenever(backend.search(queryString)).thenReturn(single)
    }

    private fun givenBackendFailWith(exception: Exception) {
        givenBackendReturn(Single.error(exception))
    }
}
