package com.novoda.demo.coroutines.simple

import retrofit2.http.GET

interface StarWarsQuotesService {

    @GET("api/SWQuote/RandomStarWarsQuote")
    suspend fun getRandomStarWarsQuote(): Quote

}
