package com.novoda.androidstoreexample.dagger.module

import dagger.Module
import dagger.Provides
import com.novoda.androidstoreexample.BuildConfig
import javax.inject.Singleton

@Module
open class HostModule {
    val NETWORK_TIMEOUT_SECONDS = 60L

    @Provides
    @Singleton
    fun provideNetworkTimeout(): Long = NETWORK_TIMEOUT_SECONDS

    @Provides
    @Singleton
    open fun provideBaseUrl(): String {
        return BuildConfig.API_URL
    }
}