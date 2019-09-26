package com.novoda.movies.mvi.search.domain

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.novoda.movies.mvi.search.data.SearchBackend
import com.novoda.movies.mvi.search.domain.SearchReducer.Changes
import com.novoda.movies.mvi.search.presentation.SearchViewModel.Action
import com.novoda.movies.mvi.search.presentation.SearchViewModel.State
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

    private val actions = PublishSubject.create<Action>()
    private val state = PublishSubject.create<State>()
    private lateinit var changes: TestObserver<Changes>

    @Before
    fun setUp() {
        changes = searchMiddleware.bind(actions, state).test()
    }

    @Test
    fun `GIVEN state with query WHEN query changed THEN query is updated`() {
        state.onNext(State(queryString = "iron man", results = ViewSearchResults.emptyResults))

        actions.onNext(Action.ChangeQuery(queryString = "superman"))

        changes.assertValue(Changes.UpdateSearchQuery("superman"))
    }

    @Test
    fun `WHEN query cleared THEN updated query is empty AND results are removed`() {
        state.onNext(State(queryString = "iron man", results = ViewSearchResults.emptyResults))

        actions.onNext(Action.ClearQuery)

        changes.assertValues(
                Changes.UpdateSearchQuery("", true),
                Changes.RemoveResults
        )
    }

    @Test
    fun `GIVEN dataSource has results WHEN execute search THEN search is in progress AND search is completed`() {
        val searchResults = SearchResults(items = listOf())
        state.onNext(State(queryString = "iron man", results = ViewSearchResults.emptyResults))
        backend.stub { on { search("iron man") } doReturn Single.just(searchResults) }

        actions.onNext(Action.ExecuteSearch)

        changes.assertValues(
                Changes.ShowProgress,
                Changes.AddResults(results = searchResults),
                Changes.HideProgress
        )
    }

    @Test
    fun `GIVEN dataSource errors WHEN execute search THEN search is in progress AND search failed`() {
        val exception = Throwable()
        state.onNext(State(queryString = "iron man", results = ViewSearchResults.emptyResults))
        backend.stub { on { search("iron man") } doReturn (Single.error(exception)) }

        actions.onNext(Action.ExecuteSearch)

        changes.assertValues(
                Changes.ShowProgress,
                Changes.HandleError(exception),
                Changes.HideProgress
        )
    }

}
