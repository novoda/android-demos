package com.novoda.demo.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

public class MoviesViewModel extends ViewModel {

    private MovieService movieService;
    private MutableLiveData<MoviesSate> moviesLiveData;

    MoviesViewModel(MovieService movieService) {
        this.movieService = movieService;
    }

    public LiveData<MoviesSate> getMovies() {
        if (moviesLiveData == null) {
            moviesLiveData = new MutableLiveData<>();
        }
        movieService.loadMore(new MovieService.Callback() {
            @Override
            public void onNewData(MoviesSate moviesSate) {
                moviesLiveData.postValue(moviesSate);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("Movies", "while loading movies", e);
            }
        });

        return moviesLiveData;
    }

    public LiveData<Video> getTrailer(Movie movie) {
        final MutableLiveData<Video> liveData = new MutableLiveData<>();
        movieService.loadTrailerFor(movie, new MovieService.TrailerCallback() {
            @Override
            public void onTrailerLoaded(Video video) {
                liveData.postValue(video);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("Movies", "while loading videos", e);
            }
        });

        return liveData;
    }
}
