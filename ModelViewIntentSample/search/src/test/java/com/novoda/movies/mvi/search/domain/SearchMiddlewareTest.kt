package com.novoda.movies.mvi.search.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.presentation.ViewSearchResults
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class SearchMiddlewareTest {

    private val backend: SearchBackend = mock()
    private val searchMiddleware = SearchMiddleware(backend, Schedulers.trampoline())

    private val actions = PublishSubject.create<SearchAction>()
    private val state = PublishSubject.create<SearchState>()
    private lateinit var changes: TestObserver<SearchChanges>

    @Before
    fun setUp() {
        changes = searchMiddleware.bind(actions, state).test()
    }

    @Test
    fun `GIVEN state with query WHEN query changed THEN query is updated`() {
        state.onNext(SearchState.Loading(queryString = "iron man"))

        actions.onNext(SearchAction.ChangeQuery(queryString = "superman"))

        changes.assertValue(SearchChanges.SearchQueryUpdate("superman"))
    }

    @Test
    fun `GIVEN state with query WHEN query cleared THEN updated query is empty`() {
        state.onNext(SearchState.Loading(queryString = "iron man"))

        actions.onNext(SearchAction.ClearQuery)

        changes.assertValue(SearchChanges.SearchQueryUpdate(""))
    }

    @Test
    fun `GIVEN backend has results WHEN execute search THEN search is in progress AND search is completed`() {
        val searchResults = SearchResults(items = listOf())
        state.onNext(SearchState.Content(queryString = "iron man", results = ViewSearchResults()))
        backend.stub { on { search("iron man") } doReturn Single.just(searchResults) }

        actions.onNext(SearchAction.ExecuteSearch)

        changes.assertValues(
            SearchChanges.SearchInProgress,
            SearchChanges.SearchCompleted(results = searchResults)
        )
    }

    @Test
    fun `GIVEN backend errors WHEN execute search THEN search is in progress AND search failed`() {
        val exception = IllegalStateException("backend is down")
        state.onNext(SearchState.Content(queryString = "iron man", results = ViewSearchResults()))
        backend.stub { on { search("iron man") } doReturn (Single.error(exception)) }

        actions.onNext(SearchAction.ExecuteSearch)

        changes.assertValues(
            SearchChanges.SearchInProgress,
            SearchChanges.SearchFailed(exception)
        )
    }
}
