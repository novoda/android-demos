package com.novoda.demo.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.novoda.demo.movies.api.MoviesApi;
import com.novoda.demo.movies.api.MoviesResponse;
import com.novoda.demo.movies.api.VideosResponse;
import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieService {

    private final MoviesApi api;
    private final MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Video>> videosLiveData = new MutableLiveData<>();

    public MovieService(MoviesApi api) {
        this.api = api;
    }

    public LiveData<List<Movie>> loadMore(int page) {
        api.topRated(page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response == null || response.body() == null || response.body().results == null) {
                    return;
                }
                moviesLiveData.postValue(response.body().results);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable e) {
                Log.e("Movies", "while loading movies", e);
            }
        });
        return moviesLiveData;
    }

    public LiveData<List<Video>> loadTrailerFor(Movie movie) {
        api.videos(movie.id).enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                if (response == null || response.body() == null || response.body().results == null) {
                    return;
                }
                videosLiveData.postValue(response.body().results);
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable e) {
                Log.e("Movies", "while loading videos", e);
            }
        });
        return videosLiveData;
    }
}
