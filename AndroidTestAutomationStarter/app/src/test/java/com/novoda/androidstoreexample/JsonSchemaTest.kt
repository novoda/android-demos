package com.novoda.androidstoreexample

import io.restassured.RestAssured
import io.restassured.RestAssured.get
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.io.File

class JsonSchemaTest {
    @Before
    fun setUp() {
        RestAssured.baseURI = BuildConfig.API_URL
    }

    @Test
    fun categegorySchema_matchesToTheServerResponse() {
        get("categories")
                .then()
                .body(matchesJsonSchemaInClasspath("schemas/category-schema.json"))
    }

    @Test
    fun category() {
        val file = File("app/src/androidTest/assets/categories/categories.json")
        val json = file.inputStream().bufferedReader().use { it.readText() }

        assertThat(json, matchesJsonSchemaInClasspath("schemas/category-schema.json"))
    }
}
