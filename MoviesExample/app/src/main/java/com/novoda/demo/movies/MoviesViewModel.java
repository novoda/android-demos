package com.novoda.demo.movies;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import java.util.List;
import java.util.concurrent.Executors;

public class MoviesViewModel extends ViewModel {

    private final MovieService movieService;
    private LiveData<PagedList<Movie>> paginatedMovies;

    MoviesViewModel(MovieService movieService) {
        this.movieService = movieService;
        this.paginatedMovies = createPaginatedMovies(movieService);
    }

    private LiveData<PagedList<Movie>> createPaginatedMovies(MovieService movieService) {
        MoviesDataFactory moviesDataFactory = new MoviesDataFactory(movieService);
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(20)
                        .setPrefetchDistance(10)
                        .build();

        return new LivePagedListBuilder<>(moviesDataFactory, pagedListConfig)
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build();
    }

    public LiveData<PagedList<Movie>> getPaginatedMovies() {
        return paginatedMovies;
    }

    public LiveData<Video> loadTrailerFor(Movie movie) {
        return Transformations.map(movieService.loadTrailerFor(movie), new Function<List<Video>, Video>() {
            @Override
            public Video apply(List<Video> input) {
                if (input.size() > 0) {
                    return input.get(0);
                }
                return null;
            }
        });
    }
}
