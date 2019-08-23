package com.novoda.movies.mvi.search.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.novoda.movies.mvi.search.data.MovieDataSource
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.presentation.ViewSearchResults
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test

class SearchMiddlewareTest {

    private val dataSource: MovieDataSource = mock()
    private val searchMiddleware = SearchMiddleware(dataSource, Schedulers.trampoline())

    private val actions = PublishSubject.create<SearchAction>()
    private val state = PublishSubject.create<ScreenState>()
    private lateinit var changes: TestObserver<ScreenStateChanges>

    @Before
    fun setUp() {
        changes = searchMiddleware.bind(actions, state).test()
    }

    @Test
    fun `GIVEN state with query WHEN query changed THEN query is updated`() {
        state.onNext(ScreenState(queryString = "iron man", results = ViewSearchResults.emptyResults))

        actions.onNext(SearchAction.ChangeQuery(queryString = "superman"))

        changes.assertValue(ScreenStateChanges.UpdateSearchQuery("superman"))
    }

    @Test
    fun `WHEN query cleared THEN updated query is empty AND results are removed`() {
        state.onNext(ScreenState(queryString = "iron man", results = ViewSearchResults.emptyResults))

        actions.onNext(SearchAction.ClearQuery)

        changes.assertValues(
                ScreenStateChanges.UpdateSearchQuery(""),
                ScreenStateChanges.RemoveResults
        )
    }

    @Test
    fun `GIVEN dataSource has results WHEN execute search THEN search is in progress AND search is completed`() {
        val searchResults = SearchResults(items = listOf())
        state.onNext(ScreenState(queryString = "iron man", results = ViewSearchResults.emptyResults))
        dataSource.stub { on { search("iron man") } doReturn Single.just(searchResults) }

        actions.onNext(SearchAction.ExecuteSearch)

        changes.assertValues(
                ScreenStateChanges.ShowProgress,
                ScreenStateChanges.AddResults(results = searchResults),
                ScreenStateChanges.HideProgress
        )
    }

    @Test
    fun `GIVEN dataSource errors WHEN execute search THEN search is in progress AND search failed`() {
        val exception = Throwable()
        state.onNext(ScreenState(queryString = "iron man", results = ViewSearchResults.emptyResults))
        dataSource.stub { on { search("iron man") } doReturn (Single.error(exception)) }

        actions.onNext(SearchAction.ExecuteSearch)

        changes.assertValues(
                ScreenStateChanges.ShowProgress,
                ScreenStateChanges.HandleError(exception),
                ScreenStateChanges.HideProgress
        )
    }

}
