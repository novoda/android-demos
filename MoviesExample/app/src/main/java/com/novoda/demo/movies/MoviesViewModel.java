package com.novoda.demo.movies;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.novoda.demo.movies.api.MoviesApi;
import com.novoda.demo.movies.api.VideosResponse;
import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesViewModel extends ViewModel {

    private final MovieService movieService;
    private MoviesApi moviesApi;
    private LiveData<MoviesSate> moviesLiveData;
    private MoviesSate moviesSate = new MoviesSate(new ArrayList<Movie>(), 1);

    MoviesViewModel(MoviesApi moviesApi) {
        this.moviesApi = moviesApi;
        movieService = new MovieService(moviesApi);
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
