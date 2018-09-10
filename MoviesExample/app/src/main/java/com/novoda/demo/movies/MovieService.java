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
import retrofit2.Response;

public class MovieService {

    private final MoviesApi api;
    private final MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Video>> videosLiveData = new MutableLiveData<>();

    public MovieService(MoviesApi api) {
        this.api = api;
    }

    public LiveData<List<Movie>> loadMore(int page) {
        loadMore(page, new Callback() {
            @Override
            public void onResponse(MoviesResponse response) {
                moviesLiveData.postValue(response.results);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("Movies", "while loading movies", e);
            }
        });

        return moviesLiveData;
    }

    public void loadMore(int page, final Callback callback) {
        api.topRated(page).enqueue(new retrofit2.Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response == null || response.body() == null || response.body().results == null) {
                    return;
                }
                callback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable e) {
                callback.onError(e);
            }
        });
    }

    public LiveData<List<Video>> loadTrailerFor(Movie movie) {
        api.videos(movie.id).enqueue(new retrofit2.Callback<VideosResponse>() {
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

    interface Callback {

        void onResponse(MoviesResponse response);

        void onError(Throwable e);
    }
}
