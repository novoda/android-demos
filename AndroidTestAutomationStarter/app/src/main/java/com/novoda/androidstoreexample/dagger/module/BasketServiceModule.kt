package com.novoda.androidstoreexample.dagger.module

import com.novoda.androidstoreexample.services.BasketService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BasketServiceModule {

    @Provides
    @Singleton
    fun providesBasket(): BasketService {
        return BasketService()
    }
}