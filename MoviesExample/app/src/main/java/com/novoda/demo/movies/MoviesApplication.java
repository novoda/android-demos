package com.novoda.demo.movies;

import android.app.Application;

import com.novoda.demo.movies.Dependencies;
import com.novoda.demo.movies.MovieService;

public class MoviesApplication extends Application {

    private MovieService movieService;

    @Override
    public void onCreate() {
        super.onCreate();
        Dependencies dependencies = new Dependencies(getCacheDir());
        movieService = dependencies.provideMovieService();
    }

    public MovieService movieService() {
        return movieService;
    }
}
