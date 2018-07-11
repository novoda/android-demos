package com.novoda.androidstoreexample.dagger.module

import com.novoda.androidstoreexample.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
open class HostModule {
    companion object {
        const val NETWORK_TIMEOUT_SECONDS = 60L
    }

    @Provides
    @Named("NETWORK_TIMEOUT")
    fun provideNetworkTimeout(): Long = NETWORK_TIMEOUT_SECONDS

    @Provides
    open fun provideBaseUrl(): String {
        return BuildConfig.API_URL
    }
}
