package com.novoda.demo.movies;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import java.util.ArrayList;
import java.util.List;

public class MoviesViewModel extends ViewModel {

    private final MovieService movieService;
    private LiveData<MoviesSate> moviesLiveData;
    private MoviesSate moviesSate = new MoviesSate(new ArrayList<Movie>(), 1);

    MoviesViewModel(MovieService movieService) {
        this.movieService = movieService;
    }

    public LiveData<MoviesSate> moviesLiveData() {
        if (moviesLiveData == null) {
            moviesLiveData = Transformations.map(movieService.loadMore(moviesSate.pageNumber()),
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
        return moviesLiveData;
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
