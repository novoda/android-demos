package com.novoda.androidstoreexample.models

data class Category(val id: Int, val title: String, val image: String) {
    override fun toString(): String {
        return title
    }
}
