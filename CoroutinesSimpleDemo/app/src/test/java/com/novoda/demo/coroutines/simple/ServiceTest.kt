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

}
