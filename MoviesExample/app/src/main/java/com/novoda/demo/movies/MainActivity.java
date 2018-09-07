package com.novoda.demo.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.novoda.demo.movies.databinding.ActivityMainBinding;
import com.novoda.demo.movies.model.Movie;
import com.novoda.demo.movies.model.Video;

public class MainActivity extends AppCompatActivity {

    private MoviesAdapter adapter;

    RecyclerView resultList;
    private MoviesViewModel moviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle("Top Rated Movies");

        MovieService movieService = ((MoviesApplication) getApplication()).movieService();
        moviesViewModel = ViewModelProviders.of(this, new MoviesViewModelFactory(movieService)).get(MoviesViewModel.class);

        resultList = findViewById(R.id.movies_list);

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

        viewDataBinding.setViewmodel(moviesViewModel);
        viewDataBinding.setLifecycleOwner(this);
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
