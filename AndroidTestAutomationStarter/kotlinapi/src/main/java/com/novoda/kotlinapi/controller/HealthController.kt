package com.novoda.kotlinapi.controller

import io.javalin.Context


object HealthController {
    fun apiRunning(context: Context) {
        context.result("Server is running")
    }
}
