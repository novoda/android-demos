package com.novoda.demo.movies;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.novoda.demo.movies.api.MoviesResponse;
import com.novoda.demo.movies.model.Movie;

import java.util.List;

public class MoviesDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE = 1;
    private static final int SECOND_PAGE = 2;

    private final MovieService movieService;

    MoviesDataSource(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        movieService.loadMore(FIRST_PAGE, new MovieService.Callback() {

            @Override
            public void onResponse(MoviesResponse response) {
                callback.onResult(response.results, null, SECOND_PAGE);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Movies", "while loading initial movies", e);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        //not used
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Movie> callback) {
        final Integer currentPage = params.key;
        movieService.loadMore(FIRST_PAGE, new MovieService.Callback() {

            @Override
            public void onResponse(MoviesResponse response) {
                boolean isLastPage = currentPage == response.total_results;
                Integer nextPage = isLastPage ? null : currentPage + 1;
                List<Movie> results = response.results;
                callback.onResult(results, nextPage);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Movies", "while loading initial movies", e);
            }
        });
    }
}
