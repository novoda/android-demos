package com.novoda.movies.mvi.search.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


internal interface SearchApi {

    @GET("search/movie")
    fun search(
        @Query("query") query: String
    ): Single<ApiSearchResults>
}
