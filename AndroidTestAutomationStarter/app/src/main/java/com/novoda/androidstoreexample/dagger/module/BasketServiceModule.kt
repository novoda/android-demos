package com.novoda.androidstoreexample.dagger.module

import com.novoda.androidstoreexample.services.BasketService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class BasketServiceModule {

    @Provides
    @Singleton
    open fun providesBasket(): BasketService {
        return BasketService()
    }
}