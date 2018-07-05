package com.novoda.androidstoreexample.dagger.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ClientModule {

    @Provides
    @Singleton
    fun provideClient(networkTimeout: Long): OkHttpClient {

        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        okHttpClientBuilder.readTimeout(networkTimeout, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(networkTimeout, TimeUnit.SECONDS)

        return okHttpClientBuilder.build()
    }
}