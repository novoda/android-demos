package com.novoda.demo.movies;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import java.util.List;

import static android.arch.lifecycle.Transformations.map;
import static android.arch.lifecycle.Transformations.switchMap;

public class MoviesViewModel extends ViewModel {

    private final MovieService movieService;
    private LiveData<PagedList<Movie>> paginatedMovies;
    private LiveData<NetworkStatus> networkState;

    MoviesViewModel(MovieService movieService) {
        this.movieService = movieService;
        MoviesDataFactory moviesDataFactory = new MoviesDataFactory(movieService);
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setPageSize(20)
                        .setPrefetchDistance(20)
                        .build();

        this.paginatedMovies = new LivePagedListBuilder<>(moviesDataFactory, pagedListConfig)
                .build();
        this.networkState = switchMap(moviesDataFactory.getDataSource(), toNetworkState());
    }

    private Function<MoviesDataSource, LiveData<NetworkStatus>> toNetworkState() {
        return new Function<MoviesDataSource, LiveData<NetworkStatus>>() {
            @Override
            public LiveData<NetworkStatus> apply(MoviesDataSource input) {
                return input.getNetworkState();
            }
        };
    }

    public LiveData<PagedList<Movie>> getPaginatedMovies() {
        return paginatedMovies;
    }

    LiveData<NetworkStatus> getNetworkState() {
        return networkState;
    }

    public LiveData<Video> loadTrailerFor(Movie movie) {
        return map(movieService.loadTrailerFor(movie), new Function<List<Video>, Video>() {
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
