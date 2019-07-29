package com.novoda.demo.movies

import com.novoda.demo.movies.api.MoviesApi
import com.novoda.demo.movies.api.MoviesResponse
import com.novoda.demo.movies.model.Movie
import com.novoda.demo.movies.model.Video
import retrofit2.Call
import retrofit2.Response
import rx.Observable
import rx.Observer
import rx.schedulers.Schedulers
import java.util.ArrayList

class MovieService(private val api: MoviesApi) {
    private var moviesState = MoviesState(ArrayList(), 1)

    private var callback: Callback? = null

    fun subscribe(callback: Callback) {
        this.callback = callback
        callback.onNewData(moviesState)
        if (moviesState.isEmpty) {
            loadMore()
        }
    }

    fun unsubscribe(callback: Callback) {
        this.callback = null
    }

    fun loadMore() {
        api.topRated(moviesState.pageNumber()).enqueue(object : retrofit2.Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>?) {
                if (response?.body() == null || response.body()?.results == null) {
                    return
                }
                val movies = moviesState.movies()
                movies.addAll(response.body()!!.results)
                moviesState = MoviesState(movies, moviesState.pageNumber() + 1)
                callback!!.onNewData(moviesState)
            }

            override fun onFailure(call: Call<MoviesResponse>, throwable: Throwable) {
                callback!!.onFailure(throwable)
            }
        })
    }

    fun loadTrailerFor(movie: Movie, trailerCallback: TrailerCallback) {
        api.videos(movie.id)
                .flatMapObservable { videosResponse -> Observable.from(videosResponse.results) }
                .takeFirst { video -> video.trailerUrl() != null }
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<Video> {
                    override fun onCompleted() {
                        // noop
                    }

                    override fun onError(e: Throwable) {
                        trailerCallback.onFailure(e)
                    }

                    override fun onNext(video: Video) {
                        trailerCallback.onTrailerLoaded(video)
                    }
                })
    }

    interface Callback {
        fun onNewData(moviesSate: MoviesState)

        fun onFailure(e: Throwable)
    }

    interface TrailerCallback {
        fun onTrailerLoaded(video: Video)

        fun onFailure(e: Throwable)
    }

}
