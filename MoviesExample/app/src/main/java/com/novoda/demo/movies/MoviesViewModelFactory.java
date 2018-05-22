package com.novoda.demo.movies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MoviesViewModelFactory implements ViewModelProvider.Factory{

    private MovieService movieService;

    MoviesViewModelFactory(MovieService movieService) {
        this.movieService = movieService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviesViewModel.class)) {
            return (T) new MoviesViewModel(movieService);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
