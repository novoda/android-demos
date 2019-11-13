package com.novoda.demo.coroutines.simple

import kotlinx.coroutines.runBlocking
import org.junit.Test

class ServiceTest {

    private val service = MainViewModel.Service()

    // Test a suspend function waiting for it to finish
    @Test
    fun testBlocking() {
        runBlocking {
            val result = service.firstBackgroundJobAsync()
            check(result == "HI")
        }
    }

    @Test
    fun testSuspend() {
        runBlocking {
            val result = service.safeAsyncJob(false)
            check(result == "HI from another Job")
        }
    }

    @Test
    fun testSuspendCrashes() {
        runBlocking {
            val result = service.safeAsyncJob(true)
            check(result == "It burned properly")
        }
    }
}
