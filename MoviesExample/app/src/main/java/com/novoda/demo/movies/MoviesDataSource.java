package com.novoda.demo.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
    private MutableLiveData<NetworkStatus> networkState;

    MoviesDataSource(MovieService movieService) {
        this.movieService = movieService;
        this.networkState = new MutableLiveData<>();
    }

    LiveData<NetworkStatus> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Movie> callback) {
        networkState.postValue(NetworkStatus.LOADING);
        movieService.loadMore(FIRST_PAGE, new MovieService.Callback() {

            @Override
            public void onResponse(MoviesResponse response) {
                networkState.postValue(NetworkStatus.LOADED);
                callback.onResult(response.results, null, SECOND_PAGE);
            }

            @Override
            public void onError(Throwable e) {
                networkState.postValue(NetworkStatus.ERROR);
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
        networkState.postValue(NetworkStatus.LOADING);
        final Integer currentPage = params.key;
        movieService.loadMore(currentPage, new MovieService.Callback() {

            @Override
            public void onResponse(MoviesResponse response) {
                networkState.postValue(NetworkStatus.LOADED);
                boolean isLastPage = currentPage == response.total_results;
                Integer nextPage = isLastPage ? null : currentPage + 1;
                List<Movie> results = response.results;
                callback.onResult(results, nextPage);
            }

            @Override
            public void onError(Throwable e) {
                networkState.postValue(NetworkStatus.ERROR);
                Log.e("Movies", "while loading initial movies", e);
            }
        });
    }
}
