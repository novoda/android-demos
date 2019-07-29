package com.novoda.demo.movies.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Single

const val API_KEY = "ENTER_YOUR_API_KEY"

interface MoviesApi {

    @GET("movie/top_rated?api_key=$API_KEY")
    fun topRated(@Query("page") page: Int): Call<MoviesResponse>

    @GET("movie/{id}/videos?api_key=$API_KEY")
    fun videos(@Path("id") movieId: String): Single<VideosResponse>

}
