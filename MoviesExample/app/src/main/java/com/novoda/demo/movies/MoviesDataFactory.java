package com.novoda.demo.movies;

import android.arch.paging.DataSource;

import com.novoda.demo.movies.model.Movie;

public class MoviesDataFactory extends DataSource.Factory<Integer, Movie> {

    private final MovieService movieService;

    MoviesDataFactory(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        return new MoviesDataSource(movieService);
    }
}
