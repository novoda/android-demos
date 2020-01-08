package com.novoda.demo.coroutines.simple

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StarWarsService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://swquotesapi.digitaljedi.dk/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service =
        retrofit.create<StarWarsQuotesService>(
            StarWarsQuotesService::class.java)

    private val quoteAdapter: MainViewModel.QuoteAdapter =
        MainViewModel.SimpleQuoteAdapter

    suspend fun fetchSW(): String = service.getRandomStarWarsQuote().extractToString()

    private fun Quote.extractToString(): String = quoteAdapter.convert(this)

}