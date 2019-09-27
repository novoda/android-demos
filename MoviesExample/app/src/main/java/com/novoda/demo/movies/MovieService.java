package com.novoda.demo.movies;

import com.novoda.demo.movies.api.MoviesApi;
import com.novoda.demo.movies.api.MoviesResponse;
import com.novoda.demo.movies.api.VideosResponse;
import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;
import com.novoda.demo.movies.repository.VideosRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MovieService {

    private final MoviesApi api;
    private final VideosRepository repository = new VideosRepository();
    private MoviesSate moviesSate = new MoviesSate(new ArrayList<Movie>(), 1);

    private Callback callback;

    public MovieService(MoviesApi api) {
        this.api = api;
    }

    public void subscribe(Callback callback) {
        this.callback = callback;
        callback.onNewData(moviesSate);
        if (moviesSate.isEmpty()) {
            loadMore();
        }
    }

    public void unsubscribe(Callback callback) {
        this.callback = null;
    }

    public void loadMore() {
        api.topRated(moviesSate.pageNumber()).enqueue(new retrofit2.Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response == null || response.body() == null || response.body().results == null) {
                    return;
                }
                List<Movie> movies = moviesSate.movies();
                movies.addAll(response.body().results());
                moviesSate = new MoviesSate(movies, moviesSate.pageNumber() + 1);
                callback.onNewData(moviesSate);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    public void loadTrailerFor(Movie movie, final TrailerCallback trailerCallback) {
        api.videos(movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapObservable(new Func1<VideosResponse, Observable<Video>>() {
                    @Override
                    public Observable<Video> call(final VideosResponse videosResponse) {
                        List<Video> results = videosResponse.results();
                        repository.store(results);
                        return Observable.from(results);
                    }
                })
                .takeFirst(new Func1<Video, Boolean>() {
                    @Override
                    public Boolean call(final Video video) {
                        return video.trailerUrl() != null;
                    }
                })
                .subscribe(new Observer<Video>() {
                    @Override
                    public void onCompleted() {
                        // noop
                    }

                    @Override
                    public void onError(final Throwable e) {
                        trailerCallback.onFailure(e);
                    }

                    @Override
                    public void onNext(final Video video) {
                        trailerCallback.onTrailerLoaded(video);
                    }
                });
         }

    interface Callback {
        void onNewData(MoviesSate moviesSate);

        void onFailure(Throwable e);
    }

    interface TrailerCallback {
        void onTrailerLoaded(Video video);

        void onFailure(Throwable e);
    }

}

