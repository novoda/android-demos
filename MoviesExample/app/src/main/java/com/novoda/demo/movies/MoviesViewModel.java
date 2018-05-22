package com.novoda.demo.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.novoda.demo.movies.api.MoviesApi;
import com.novoda.demo.movies.api.MoviesResponse;
import com.novoda.demo.movies.api.VideosResponse;
import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesViewModel extends ViewModel {

    private MoviesApi moviesApi;
    private MutableLiveData<MoviesSate> moviesLiveData;
    private MoviesSate moviesSate = new MoviesSate(new ArrayList<Movie>(), 1);

    MoviesViewModel(MoviesApi moviesApi) {
        this.moviesApi = moviesApi;
    }

    public LiveData<MoviesSate> getMovies() {
        if (moviesLiveData == null) {
            moviesLiveData = new MutableLiveData<>();
        }

        moviesApi.topRated(moviesSate.pageNumber()).enqueue(new retrofit2.Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response == null || response.body() == null || response.body().results == null) {
                    return;
                }
                List<Movie> movies = moviesSate.movies();
                movies.addAll(response.body().results);
                moviesSate = new MoviesSate(movies, moviesSate.pageNumber() + 1);
                moviesLiveData.postValue(moviesSate);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable e) {
                Log.e("Movies", "while loading movies", e);
            }
        });

        return moviesLiveData;
    }


    public LiveData<Video> loadTrailerFor(Movie movie) {
        final MutableLiveData<Video> liveData = new MutableLiveData<>();

        moviesApi.videos(movie.id).enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (response == null || response.body() == null || response.body().results == null) {
                    return;
                }
                List<Video> results = response.body().results;
                if (results.size() > 0) {
                    liveData.postValue(results.get(0));
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable e) {
                Log.e("Movies", "while loading videos", e);
            }
        });

        return liveData;
    }
}
