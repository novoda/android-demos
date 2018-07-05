package com.novoda.androidstoreexample

import com.novoda.androidstoreexample.dagger.module.HostModule

class EspressoHostModule(val port: Int) : HostModule() {
    override fun provideBaseUrl(): String {
        return "http://127.0.0.1:$port"
    }
}