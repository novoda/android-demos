package com.novoda.movies.mvi.search.domain

import com.novoda.movies.mvi.search.Middleware
import io.reactivex.Observable
import io.reactivex.functions.BiFunction


internal class SearchMiddleware : Middleware<SearchAction, SearchState, SearchChanges> {

    override fun bind(actions: Observable<SearchAction>, state: Observable<SearchState>): Observable<SearchChanges> {
        return actions
                .withLatestFrom(state, actionToState())
                .switchMap { (action, state) -> handle(action, state) }
    }

    private fun actionToState(): BiFunction<SearchAction, SearchState, Pair<SearchAction, SearchState>> =
            BiFunction { action, state -> action to state }

    private fun handle(action: SearchAction, state: SearchState): Observable<SearchChanges> =
            when (action) {
                is SearchAction.ChangeQuery -> TODO()
                is SearchAction.ExecuteSearch -> TODO()
                is SearchAction.ClearQuery -> TODO()
            }
}


internal sealed class SearchAction {
    data class ChangeQuery(val queryString: String) : SearchAction()
    object ExecuteSearch : SearchAction()
    object ClearQuery : SearchAction()
}

internal sealed class SearchState {
    abstract val queryString: String

    object Initial : SearchState() {
        override val queryString: String
            get() = ""
    }

    data class TextInput(override val queryString: String) : SearchState()
    data class Content(
            override val queryString: String,
            val searchResults: SearchResults
    ) : SearchState()

    data class Loading(override val queryString: String) : SearchState()
    data class Error(override val queryString: String, val throwable: Throwable) : SearchState()
}

internal sealed class SearchChanges {

    object SearchInProgress : SearchChanges()
    data class SearchCompleted(val results: SearchResults) : SearchChanges()
    data class SearchFailed(val throwable: Throwable) : SearchChanges()
}
