package com.novoda.demo.movies.model

data class Video(
        val id: String? = null,
        val key: String? = null,
        val site: String? = null,
        val type: String? = null
) {

    fun trailerUrl(): String? {
        return if ("YouTube" != site || "Trailer" != type) {
            null
        } else "https://www.youtube.com/watch?v=" + key!!
    }
}
