package com.novoda.demo.movies.model

import com.google.gson.annotations.SerializedName

data class Movie(
        val id: String? = null,
        val title: String? = null,
        val poster_path: String? = null,
        val backdrop_path: String? = null,
        @SerializedName("vote_average")
        val rating: Double = 0.toDouble()
) {

    fun posterUrl(): String = "http://image.tmdb.org/t/p/w92" + poster_path!!
    fun backDropUrl(): String = "http://image.tmdb.org/t/p/w300" + poster_path!!
}
