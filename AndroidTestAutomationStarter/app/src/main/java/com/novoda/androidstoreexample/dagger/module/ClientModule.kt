package com.novoda.androidstoreexample.dagger.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class ClientModule {

    @Provides
    @Singleton
    fun provideClient(@Named("NETWORK_TIMEOUT") networkTimeout: Long): OkHttpClient {

        return OkHttpClient.Builder()
                .readTimeout(networkTimeout, TimeUnit.SECONDS)
                .connectTimeout(networkTimeout, TimeUnit.SECONDS)
                .build()
    }
}
