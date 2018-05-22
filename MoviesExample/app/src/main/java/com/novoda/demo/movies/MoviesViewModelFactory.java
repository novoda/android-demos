package com.novoda.demo.movies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.novoda.demo.movies.api.MoviesApi;

public class MoviesViewModelFactory implements ViewModelProvider.Factory{

    private MoviesApi moviesApi;

    MoviesViewModelFactory(MoviesApi moviesApi) {
        this.moviesApi = moviesApi;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoviesViewModel.class)) {
            return (T) new MoviesViewModel(moviesApi);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
