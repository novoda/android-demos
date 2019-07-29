package com.novoda.demo.movies

import com.novoda.demo.movies.model.Movie

data class MoviesState(private val movies: List<Movie>, private val pageNumber: Int) {

    val isEmpty: Boolean
        get() = movies.isEmpty()

    fun movies(): List<Movie>? = movies

    fun pageNumber(): Int = pageNumber

    fun size(): Int = movies.size

    fun get(position: Int): Movie = movies[position]
    
}
