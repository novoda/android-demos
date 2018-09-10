package com.novoda.demo.movies;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.novoda.demo.movies.api.MoviesApi;
import com.novoda.demo.movies.api.MoviesResponse;
import com.novoda.demo.movies.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE = 1;
    private static final int SECOND_PAGE = 2;

    private final MoviesApi moviesApi;

    public MoviesDataSource(MoviesApi moviesApi) {
        this.moviesApi = moviesApi;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        moviesApi.topRated(FIRST_PAGE).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> results = response.body().results;
                callback.onResult(results, null, SECOND_PAGE);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Movies", "while loading initial movies", t);
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
        moviesApi.topRated(currentPage).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                boolean isLastPage = currentPage == response.body().total_results;
                Integer nextPage = isLastPage ? null : currentPage + 1;
                List<Movie> results = response.body().results;
                callback.onResult(results, nextPage);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Movies", "while loading movies", t);
            }
        });
    }
}
