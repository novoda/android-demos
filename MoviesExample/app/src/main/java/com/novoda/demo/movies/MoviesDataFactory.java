package com.novoda.demo.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.novoda.demo.movies.model.Movie;

public class MoviesDataFactory extends DataSource.Factory<Integer, Movie> {

    private final MovieService movieService;
    private final MutableLiveData<MoviesDataSource> dataSource;

    MoviesDataFactory(MovieService movieService) {
        this.movieService = movieService;
        this.dataSource = new MutableLiveData<>();
    }

    LiveData<MoviesDataSource> getDataSource() {
        return dataSource;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        MoviesDataSource newDataSource = new MoviesDataSource(movieService);
        this.dataSource.postValue(newDataSource);
        return newDataSource;
    }
}
