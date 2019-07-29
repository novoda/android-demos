package com.novoda.demo.movies

import com.novoda.demo.movies.model.Movie

data class MoviesState(private val data: List<Movie>, private val pageNumber: Int) {

    private val movies = data.toMutableList()

    val isEmpty: Boolean
        get() = movies.isEmpty()

    fun movies(): MutableList<Movie> = movies

    fun pageNumber(): Int = pageNumber

    fun size(): Int = movies.size

    fun get(position: Int): Movie = movies[position]

}
