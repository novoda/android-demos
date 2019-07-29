package com.novoda.demo.movies

import com.novoda.demo.movies.api.MoviesApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class Dependencies(private val cacheDir: File) {

    fun provideMovieService(): MovieService {
        val cache = providesCache()
        val httpClient = providesOkHttp(cache)
        val retrofit = providesRetrofit(httpClient)
        val moviesApi = providesApi(retrofit)
        return MovieService(moviesApi)
    }

    private fun providesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)

    private fun providesRetrofit(httpClient: OkHttpClient) = Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    private fun providesOkHttp(cache: Cache) = OkHttpClient.Builder().cache(cache).build()

    private fun providesCache(): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(cacheDir, cacheSize.toLong())
    }

}
