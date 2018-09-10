package com.novoda.demo.movies;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MoviesViewModel extends ViewModel {

    private final MovieService movieService;
    private MoviesSate moviesSate = new MoviesSate(new ArrayList<Movie>(), 1);
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
                        .setPrefetchDistance(30)
                        .build();

        return new LivePagedListBuilder<>(moviesDataFactory, pagedListConfig)
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build();
    }

    public LiveData<PagedList<Movie>> getPaginatedMovies() {
        return paginatedMovies;
    }

    public LiveData<MoviesSate> moviesLiveData() {
        return Transformations.map(movieService.loadMore(moviesSate.pageNumber()),
                new Function<List<Movie>, MoviesSate>() {
                    @Override
                    public MoviesSate apply(List<Movie> input) {
                        List<Movie> movies = moviesSate.movies();
                        movies.addAll(input);
                        moviesSate = new MoviesSate(movies, moviesSate.pageNumber() + 1);
                        return moviesSate;
                    }
                });
    }

    public void loadMore() {
        movieService.loadMore(moviesSate.pageNumber());
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
