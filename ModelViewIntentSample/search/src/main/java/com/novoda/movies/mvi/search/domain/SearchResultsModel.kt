package com.novoda.movies.mvi.search.domain

import io.reactivex.Observable


internal interface SearchResultsModel {

    fun state(): Observable<State>
    fun executeSearch()
    fun queryChanged(queryString: String)
    fun clearQuery()

    sealed class State {
        abstract val queryString: String

        object Initial : State() {
            override val queryString: String
                get() = ""
        }

        data class TextInput(override val queryString: String) : State()
        data class Content(
            override val queryString: String,
            val searchResults: SearchResults
        ) : State()

        data class Loading(override val queryString: String) : State()
        data class Error(override val queryString: String, val throwable: Throwable) : State()
    }
}
