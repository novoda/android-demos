package com.novoda.demo.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private MovieService movieService;
    private MoviesAdapter adapter;

    @BindView(R.id.movies_list)
    RecyclerView resultList;
    private MovieService.Callback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("Top Rated Movies");

        movieService = ((MoviesApplication) getApplication()).movieService();

        resultList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MoviesAdapter(new MoviesAdapter.Listener() {
            @Override
            public void onMovieSelected(final Movie movie) {
                startLoadingTrailer(movie);
            }

            @Override
            public void onPageLoadRequested(final int page) {
                movieService.loadMore();
            }
        });
        resultList.setAdapter(adapter);
        callback = new MovieService.Callback() {
            @Override
            public void onNewData(MoviesSate moviesSate) {
                adapter.setMoviesSate(moviesSate);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("Movies", "while loading movies", e);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        movieService.subscribe(callback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        movieService.unsubscribe(callback);
    }

    private void startLoadingTrailer(Movie movie) {
        movieService.loadTrailerFor(movie, new MovieService.TrailerCallback() {
            @Override
            public void onTrailerLoaded(Video video) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video.trailerUrl())));
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("Movies", "while loading videos", e);
            }
        });
    }
}
