package com.novoda.androidstoreexample.dagger.module

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import com.novoda.androidstoreexample.dagger.App
import javax.inject.Singleton

@Module
class AppModule  {

    @Provides
    @Singleton
    fun provideContext(): Context = App.instance

    @Provides
    @Singleton
    fun providingResources(context: Context): Resources = context.resources

}