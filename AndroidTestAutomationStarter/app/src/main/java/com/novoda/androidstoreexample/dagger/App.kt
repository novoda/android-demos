package com.novoda.androidstoreexample.dagger

import android.app.Application
import com.novoda.androidstoreexample.dagger.component.AppComponent
import com.novoda.androidstoreexample.dagger.component.DaggerAppComponent

class App : Application() {

    companion object {
        @JvmStatic
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
    }
}