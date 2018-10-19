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

    interface Callback {

        void onResponse(MoviesResponse response);

        void onError(Throwable e);
    }

    private final MoviesApi api;
    private final MutableLiveData<List<Video>> videosLiveData = new MutableLiveData<>();

    MovieService(MoviesApi api) {
        this.api = api;
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
}
