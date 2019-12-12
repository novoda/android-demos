package com.novoda.demo.coroutines.simple

/**
 *
// 20191212150705
// http://swquotesapi.digitaljedi.dk/api/SWQuote/RandomStarWarsQuote

{
"id": 25,
"starWarsQuote": "You were my brother, Anakin. I loved you. — Obi-Wan Kenobi",
"faction": 0
}
 */
data class Quote(
    val id: Int,
    val starWarsQuote: String,
    val faction: Int
)
