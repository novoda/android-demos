package com.novoda.movies.mvi

import android.app.Application
import com.novoda.movies.mvi.search.Dependencies
import com.novoda.movies.mvi.search.Endpoints
import com.novoda.movies.mvi.search.NetworkDependencyProvider
import com.novoda.movies.mvi.search.Secrets


class MoviesApplication : Application(), Dependencies {
    private val secrets = Secrets(BuildConfig.API_KEY)

    override val endpoints = Endpoints(BuildConfig.BASE_URL, BuildConfig.IMAGE_BASE_URL)
    override val networkDependencyProvider = NetworkDependencyProvider(endpoints, secrets)
}
