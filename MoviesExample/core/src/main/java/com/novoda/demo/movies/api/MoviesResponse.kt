package com.novoda.demo.movies.api

import com.novoda.demo.movies.model.Movie

data class MoviesResponse(
        var page: Int = 0,
        var results: List<Movie> = emptyList(),
        var total_pages: Int = 0,
        var total_results: Int = 0
)
