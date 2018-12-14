package de.svenkroell.kotlinapi

import spark.Spark.*

fun main(args: Array<String>) {
    get("/") {request, response -> "Hello World" }
}