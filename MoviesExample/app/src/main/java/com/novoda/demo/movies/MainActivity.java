package com.novoda.demo.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.novoda.demo.movies.api.MoviesApi;
import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private MoviesAdapter adapter;

    @BindView(R.id.movies_list)
    RecyclerView resultList;
    private MoviesViewModel moviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("Top Rated Movies");

        MoviesApi movieService = ((MoviesApplication) getApplication()).moviesApi();
        moviesViewModel = ViewModelProviders.of(this, new MoviesViewModelFactory(movieService)).get(MoviesViewModel.class);

        resultList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MoviesAdapter(new MoviesAdapter.Listener() {
            @Override
            public void onMovieSelected(final Movie movie) {
                startLoadingTrailer(movie);
            }

            @Override
            public void onPageLoadRequested(final int page) {
                moviesViewModel.loadMore();
            }
        });
        resultList.setAdapter(adapter);

        moviesViewModel.moviesLiveData().observe(this, new Observer<MoviesSate>() {
            @Override
            public void onChanged(MoviesSate moviesSate) {
                adapter.setMoviesSate(moviesSate);
            }
        });
    }

    private void startLoadingTrailer(Movie movie) {
        moviesViewModel.loadTrailerFor(movie).observe(this, new Observer<Video>() {
            @Override
            public void onChanged(@Nullable Video video) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video.trailerUrl())));
            }
        });
    }
}
