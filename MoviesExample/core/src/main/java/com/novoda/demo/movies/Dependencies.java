package com.novoda.demo.movies;

import com.novoda.demo.movies.api.MoviesApi;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dependencies {

    private final File cacheDir;

    public Dependencies(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    public MovieService provideMovieService() {
        Cache cache = providesCache();
        OkHttpClient httpClient = providesOkHttp(cache);
        Retrofit retrofit = providesRetrofit(httpClient);
        MoviesApi moviesApi = providesApi(retrofit);
        return new MovieService(moviesApi);
    }

    private MoviesApi providesApi(Retrofit retrofit) {
        return retrofit.create(MoviesApi.class);
    }

    private Retrofit providesRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
    }

    private OkHttpClient providesOkHttp(Cache cache) {
        return new OkHttpClient.Builder()
                    .cache(cache)
                    .build();
    }

    private Cache providesCache() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(cacheDir, cacheSize);
    }

}
