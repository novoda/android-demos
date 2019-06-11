package com.novoda.movies.mvi.search

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val DEFAULT_TIMEOUT: Int = 30

class NetworkDependencyProvider(
    private val endpoints: Endpoints,
    private val secrets: Secrets
) {

    private fun provideOkHttp(): OkHttpClient {
        val timeUnit = TimeUnit.SECONDS
        val defaultTimeout = DEFAULT_TIMEOUT.toLong()
        val builder = OkHttpClient.Builder()
            .connectTimeout(defaultTimeout, timeUnit)
            .readTimeout(defaultTimeout, timeUnit)
            .writeTimeout(defaultTimeout, timeUnit)
            .addInterceptor(authenticationInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS })

        return builder.build()
    }

    private fun authenticationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val url = request.url().newBuilder().addQueryParameter("api_key", secrets.apiKey).build()
            val newRequest = request.newBuilder().url(url).build()
            chain.proceed(newRequest)
        }
    }

    fun provideRetrofit(): Retrofit {
        val okHttpClient = provideOkHttp()
        return Retrofit.Builder()
            .baseUrl(endpoints.baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}
